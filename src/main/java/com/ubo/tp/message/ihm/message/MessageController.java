package main.java.com.ubo.tp.message.ihm.message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.IMessageRecipient;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Contrôleur de la vue de conversation.
 * Implémente IDatabaseObserver pour réagir aux nouveaux messages en temps réel.
 */
public class MessageController implements IDatabaseObserver {

    private final MessageView mView;
    private final DataManager mDataManager;

    private IMessageRecipient mRecipient;
    private User mConnectedUser;
    private final List<IMessageObserver> mObservers = new ArrayList<>();

    public MessageController(MessageView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
    }

    public void addObserver(IMessageObserver observer) {
        mObservers.add(observer);
    }

    /**
     * Définit l'utilisateur connecté (appelé dès la connexion pour les
     * notifications).
     */
    public void setConnectedUser(User connectedUser) {
        this.mConnectedUser = connectedUser;
    }

    /**
     * Définit la conversation active (destinataire + utilisateur connecté).
     */
    public void setRecipient(IMessageRecipient recipient, User connectedUser) {
        this.mRecipient = recipient;
        this.mConnectedUser = connectedUser;
        mView.exitSearchMode();

        String title;
        String recipientName;
        if (recipient instanceof Channel) {
            title = "# " + ((Channel) recipient).getName();
            recipientName = ((Channel) recipient).getName();
        } else if (recipient instanceof User) {
            User u = (User) recipient;
            title = u.getName() + "  @" + u.getUserTag();
            recipientName = u.getName();
        } else {
            title = recipient.getUuid().toString();
            recipientName = title;
        }
        mView.setHeader(title);
        mView.setRecipientName(recipientName);
        mView.setSettingsVisible(recipient instanceof Channel);
        refreshMessages();
    }

    /**
     * Retourne le destinataire courant.
     */
    public IMessageRecipient getRecipient() {
        return mRecipient;
    }

    /**
     * Recherche les messages de la conversation courante contenant le texte donné.
     * Retourne les résultats triés du plus récent au plus ancien (comme WhatsApp).
     */
    public List<Message> searchMessages(String query) {
        List<Message> results = new ArrayList<>();
        if (mRecipient == null || mConnectedUser == null || query == null || query.trim().isEmpty()) {
            return results;
        }
        String lowerQuery = query.trim().toLowerCase();
        UUID recipientUuid = mRecipient.getUuid();
        for (Message msg : mDataManager.getMessages()) {
            if (isInConversation(msg, recipientUuid)
                    && msg.getText().toLowerCase().contains(lowerQuery)) {
                results.add(msg);
            }
        }
        // Tri du plus récent au plus ancien
        results.sort((a, b) -> Long.compare(b.getEmissionDate(), a.getEmissionDate()));
        return results;
    }

    /**
     * Envoie le message saisi dans le champ de saisie.
     */
    public void handleSend() {
        if (mRecipient == null || mConnectedUser == null)
            return;
        String text = mView.getMessageText();
        if (text.isEmpty() || text.length() > 200)
            return;
        mDataManager.sendMessage(new Message(mConnectedUser, mRecipient.getUuid(), text));
        mView.clearInput();
        refreshMessages();
    }

    /**
     * Supprime un message (uniquement si l'utilisateur en est l'auteur).
     */
    public void handleDeleteMessage(Message message) {
        if (mConnectedUser == null)
            return;
        if (message.getSender().getUuid().equals(mConnectedUser.getUuid())) {
            mDataManager.deleteMessage(message);
        }
    }

    /**
     * Retourne la liste des utilisateurs mentionnables dans la conversation
     * courante.
     */
    public List<User> getMentionableUsers() {
        List<User> users = new ArrayList<>();
        if (mRecipient instanceof Channel) {
            Channel ch = (Channel) mRecipient;
            if (ch.isPrivate()) {
                users.addAll(ch.getUsers());
            } else {
                users.addAll(mDataManager.getUsers());
            }
        } else if (mRecipient instanceof User) {
            users.add((User) mRecipient);
        }
        // Retirer l'utilisateur connecté et les inconnus
        List<User> filtered = new ArrayList<>();
        for (User u : users) {
            if (!u.getUuid().equals(mConnectedUser.getUuid())
                    && !u.getUuid().equals(main.java.com.ubo.tp.message.common.Constants.UNKNONWN_USER_UUID)) {
                filtered.add(u);
            }
        }
        return filtered;
    }

    private void refreshMessages() {
        if (mRecipient == null || mConnectedUser == null)
            return;
        UUID recipientUuid = mRecipient.getUuid();
        List<Message> conversation = new ArrayList<>();
        for (Message msg : mDataManager.getMessages()) {
            if (isInConversation(msg, recipientUuid)) {
                conversation.add(msg);
            }
        }
        conversation.sort((a, b) -> Long.compare(a.getEmissionDate(), b.getEmissionDate()));
        mView.updateMessages(conversation, mConnectedUser.getUuid());
    }

    /**
     * Détermine si un message appartient à la conversation active.
     */
    private boolean isInConversation(Message msg, UUID recipientUuid) {
        UUID sender = msg.getSender().getUuid();
        UUID recipient = msg.getRecipient();
        if (mRecipient instanceof Channel) {
            return recipient.equals(recipientUuid);
        }
        UUID myUuid = mConnectedUser.getUuid();
        return (sender.equals(myUuid) && recipient.equals(recipientUuid))
                || (sender.equals(recipientUuid) && recipient.equals(myUuid));
    }

    // === IDatabaseObserver ===

    @Override
    public void notifyMessageAdded(Message addedMessage) {
        if (mRecipient != null && isInConversation(addedMessage, mRecipient.getUuid())) {
            refreshMessages();
        }
        // MSG-010 : Notification si DM ou mention
        checkNotification(addedMessage);
    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {
        if (mRecipient != null && isInConversation(deletedMessage, mRecipient.getUuid())) {
            refreshMessages();
        }
    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {
        notifyMessageAdded(modifiedMessage);
    }

    /**
     * Trouve un canal par son UUID dans la base.
     */
    private Channel findChannelByUuid(UUID uuid) {
        for (Channel ch : mDataManager.getChannels()) {
            if (ch.getUuid().equals(uuid)) {
                return ch;
            }
        }
        return null;
    }

    /**
     * Verifie si l'utilisateur connecte a acces a un canal.
     */
    private boolean isUserInChannel(Channel channel) {
        if (!channel.isPrivate()) {
            return true;
        }
        if (channel.getCreator().getUuid().equals(mConnectedUser.getUuid())) {
            return true;
        }
        for (User u : channel.getUsers()) {
            if (u.getUuid().equals(mConnectedUser.getUuid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si un nouveau message doit déclencher une notification.
     * Gère : DM, @mention, message canal.
     */
    private void checkNotification(Message msg) {
        if (mConnectedUser == null)
            return;
        UUID myUuid = mConnectedUser.getUuid();
        if (msg.getSender().getUuid().equals(myUuid))
            return;

        if (mRecipient != null && isInConversation(msg, mRecipient.getUuid()))
            return;

        UUID recipientUuid = msg.getRecipient();
        boolean isDm = recipientUuid.equals(myUuid);
        boolean isMention = msg.getText().contains("@" + mConnectedUser.getUserTag());
        String senderName = msg.getSender().getName();

        if (isDm) {
            String title = isMention ? "Message prive (mention)" : "Message prive";
            String text = senderName + ": " + msg.getText();
            for (IMessageObserver o : mObservers) {
                o.notifyNewNotification(title, text, msg.getSender().getUuid(), null);
                o.notifyUnreadMessage(msg.getSender().getUuid());
            }
        } else {
            Channel targetChannel = findChannelByUuid(recipientUuid);
            if (targetChannel != null && isUserInChannel(targetChannel)) {
                // Badge non-lu pour tout message dans un canal (CHN-009)
                for (IMessageObserver o : mObservers) {
                    o.notifyUnreadChannel(targetChannel.getUuid());
                }
                // Notification popup uniquement si mention (MSG-010)
                if (isMention) {
                    String title = "Mention dans #" + targetChannel.getName();
                    String text = senderName + ": " + msg.getText();
                    for (IMessageObserver o : mObservers) {
                        o.notifyNewNotification(title, text, msg.getSender().getUuid(), targetChannel.getUuid());
                    }
                }
            }
        }
    }

    @Override
    public void notifyUserAdded(User addedUser) {
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {
    }

    @Override
    public void notifyUserModified(User modifiedUser) {
    }

    @Override
    public void notifyChannelAdded(Channel addedChannel) {
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {
        if (mRecipient instanceof Channel
                && mRecipient.getUuid().equals(deletedChannel.getUuid())) {
            mRecipient = null;
            for (IMessageObserver o : mObservers) {
                o.notifyConversationClosed();
            }
        }
    }

    @Override
    public void notifyChannelModified(Channel modifiedChannel) {
    }
}
