package main.java.com.ubo.tp.message.ihm.profile.editor;

/**
 * Interface pour observer les actions de l'editeur de profil.
 */
public interface IProfileEditorObserver {

    /**
     * Notification de modification du nom.
     */
    void notifyNameChanged(String newName);

    /**
     * Notification de suppression du compte.
     */
    void notifyAccountDeleted();

    /**
     * Notification de retour a l'accueil.
     */
    void notifyBackToHome();
}
