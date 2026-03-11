package main.java.com.ubo.tp.message.ihm.login;

import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Interface pour observer les événements du composant Login.
 */
public interface ILoginObserver {
    
    /**
     * Notification de connexion réussie.
     */
    void notifyLogin(User connectedUser);
    
    /**
     * Notification de demande de création de compte.
     */
    void notifyRegisterRequest();
}
