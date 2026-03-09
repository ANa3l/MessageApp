package main.java.com.ubo.tp.message.ihm.user;

import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Interface pour observer les événements du composant User.
 */
public interface IUserObserver {

    /**
     * Notification de sélection d'un utilisateur.
     */
    void notifyUserSelected(User selectedUser);
}
