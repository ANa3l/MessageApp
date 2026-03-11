package main.java.com.ubo.tp.message.ihm.profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Controleur du composant profil.
 * Gere les actions du menu et notifie les observers.
 */
public class ProfileController {

    private List<IProfileObserver> mObservers;

    /**
     * Constructeur.
     */
    public ProfileController() {
        this.mObservers = new ArrayList<>();
    }

    /**
     * Ajoute un observateur.
     */
    public void addObserver(IProfileObserver observer) {
        mObservers.add(observer);
    }

    /**
     * Appele lors du clic sur "Se deconnecter".
     */
    public void handleLogout() {
        for (IProfileObserver observer : mObservers) {
            observer.notifyLogoutRequest();
        }
    }

    /**
     * Appele lors du clic sur "Profil".
     */
    public void handleProfile() {
        for (IProfileObserver observer : mObservers) {
            observer.notifyProfileRequest();
        }
    }
}
