package main.java.com.ubo.tp.message.ihm;

import java.util.UUID;

import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.core.session.ISessionObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Logger console pour les événements de la base de données et de la session.
 */
public class ConsoleLogger implements IDatabaseObserver, ISessionObserver {

    // === Événements Session ===

    @Override
    public void notifyLogin(User connectedUser) {
        System.out.println("[SESSION] Connexion : " + connectedUser.getName() + " (@" + connectedUser.getUserTag() + ")");
    }

    @Override
    public void notifyLogout() {
        System.out.println("[SESSION] Déconnexion");
    }

    // === Événements Database ===

    @Override
    public void notifyMessageAdded(Message addedMessage) {
        System.out.println("[DB] Message ajouté : " + addedMessage.getText());
    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {
        System.out.println("[DB] Message supprimé : " + deletedMessage.getRecipient());
    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {
        System.out.println("[DB] Message modifié : " + modifiedMessage.getRecipient());
    }

    @Override
    public void notifyUserAdded(User addedUser) {
        System.out.println("[DB] Utilisateur ajouté : " + addedUser.getName());
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {
        System.out.println("[DB] Utilisateur supprimé : " + deletedUser.getName());
    }

    @Override
    public void notifyUserModified(User modifiedUser) {
        System.out.println("[DB] Utilisateur modifié : " + modifiedUser.getName());
    }

    @Override
    public void notifyChannelAdded(Channel addedChannel) {
        System.out.println("[DB] Canal ajouté : " + addedChannel.getName());
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {
        System.out.println("[DB] Canal supprimé : " + deletedChannel.getName());
    }

    @Override
    public void notifyChannelModified(Channel modifiedChannel) {
        System.out.println("[DB] Canal modifié : " + modifiedChannel.getName());
    }

    @Override
    public void notifyUserOnline(UUID userUuid) {
        System.out.println("[DB] Utilisateur en ligne : " + userUuid);
    }

    @Override
    public void notifyUserOffline(UUID userUuid) {
        System.out.println("[DB] Utilisateur hors ligne : " + userUuid);
    }
}