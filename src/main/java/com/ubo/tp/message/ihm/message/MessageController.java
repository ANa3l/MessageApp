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
     * Définit la conversation active (destinataire + utilisateur connecté).
     */
    public void setRecipient(IMessageRecipient recipient, User connectedUser) {
        this.mRecipient = recipient;
        this.mConnectedUser = connectedUser;

        String title;
        if (recipient instanceof Channel) {
            title = "# " + ((Channel) recipient).getName();
        } else if (recipient instanceof User) {
            User u = (User) recipient;
            title = u.getName() + "  @" + u.getUserTag();
        } else {
            title = recipient.getUuid().toString();
        }
        mView.setHeader(title);
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
     * Envoie le message saisi dans le champ de saisie.
     */
    public void handleSend() {
        if (mRecipient == null || mConnectedUser == null) return;
        String text = mView.getMessageText();
        if (text.isEmpty()) return;
        mDataManager.sendMessage(new Message(mConnectedUser, mRecipient.getUuid(), text));
        mView.clearInput();
        refreshMessages();
    }

    private void refreshMessages() {
        if (mRecipient == null || mConnectedUser == null) return;
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
     * - Canal : le destinataire du message est l'UUID du canal.
     * - Utilisateur : échange bidirectionnel entre moi et l'autre.
     */
    private boolean isInConversation(Message msg, UUID recipientUuid) {
        UUID sender = msg.getSender().getUuid();
        UUID recipient = msg.getRecipient();
        if (mRecipient instanceof Channel) {
            return recipient.equals(recipientUuid);
        }
        // User-to-user : messages dans les deux sens
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

    @Override public void notifyUserAdded(User addedUser) {}
    @Override public void notifyUserDeleted(User deletedUser) {}
    @Override public void notifyUserModified(User modifiedUser) {}
    @Override public void notifyChannelAdded(Channel addedChannel) {}
    @Override public void notifyChannelDeleted(Channel deletedChannel) {
        if (mRecipient instanceof Channel
                && mRecipient.getUuid().equals(deletedChannel.getUuid())) {
            mRecipient = null;
            for (IMessageObserver o : mObservers) {
                o.notifyConversationClosed();
            }
        }
    }
    @Override public void notifyChannelModified(Channel modifiedChannel) {}
}
