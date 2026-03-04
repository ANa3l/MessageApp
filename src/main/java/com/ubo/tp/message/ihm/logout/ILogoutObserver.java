package main.java.com.ubo.tp.message.ihm.logout;

/**
 * Interface pour observer les événements de déconnexion.
 */
public interface ILogoutObserver {

    /**
     * Notification de déconnexion.
     */
    void notifyLogout();
}
