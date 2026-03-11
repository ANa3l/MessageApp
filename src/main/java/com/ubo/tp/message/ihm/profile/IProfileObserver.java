package main.java.com.ubo.tp.message.ihm.profile;

/**
 * Interface pour observer les actions du composant Profil.
 */
public interface IProfileObserver {

    /**
     * Notification de demande de deconnexion.
     */
    void notifyLogoutRequest();

    /**
     * Notification de demande d'affichage du profil.
     */
    void notifyProfileRequest();
}
