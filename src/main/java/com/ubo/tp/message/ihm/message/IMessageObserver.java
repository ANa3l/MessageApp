package main.java.com.ubo.tp.message.ihm.message;

import java.util.UUID;

/**
 * Interface pour observer les evenements du composant message.
 */
public interface IMessageObserver {

    /**
     * La conversation active a ete invalidee (canal supprime, etc.).
     */
    void notifyConversationClosed();

    /**
     * Notification de nouveau message (DM, mention, canal).
     * @param title Titre de la notification
     * @param text Apercu du message
     * @param senderUuid UUID de l'expediteur
     * @param channelUuid UUID du canal, ou null si DM
     */
    void notifyNewNotification(String title, String text, UUID senderUuid, UUID channelUuid);

    /**
     * Un message non lu a ete recu d'un utilisateur (DM).
     */
    void notifyUnreadMessage(UUID senderUuid);

    /**
     * Un message non lu a ete recu dans un canal.
     */
    void notifyUnreadChannel(UUID channelUuid);
}
