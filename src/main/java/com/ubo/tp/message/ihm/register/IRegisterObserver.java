package main.java.com.ubo.tp.message.ihm.register;

/**
 * Interface pour observer les événements du composant Register.
 */
public interface IRegisterObserver {
    
    /**
     * Notification de compte créé avec succès.
     */
    void notifyAccountCreated();
    
    /**
     * Notification de retour vers le login.
     */
    void notifyBackToLogin();
}
