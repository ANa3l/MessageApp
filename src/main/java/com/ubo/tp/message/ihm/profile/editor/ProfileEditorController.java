package main.java.com.ubo.tp.message.ihm.profile.editor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Controleur de l'editeur de profil.
 * Gere la modification du nom (SRS-MAP-USR-009) et la suppression du compte (SRS-MAP-USR-010).
 */
public class ProfileEditorController {

    private ProfileEditorView mView;
    private DataManager mDataManager;
    private User mConnectedUser;
    private List<IProfileEditorObserver> mObservers;

    /**
     * Constructeur.
     */
    public ProfileEditorController(ProfileEditorView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
        this.mObservers = new ArrayList<>();
    }

    /**
     * Definit l'utilisateur connecte.
     */
    public void setConnectedUser(User user) {
        this.mConnectedUser = user;
        mView.setUser(user.getName(), user.getUserTag());
    }

    /**
     * Gere la sauvegarde du nom.
     */
    public void handleSave() {
        String newName = mView.getNameValue();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(mView,
                "Le nom ne peut pas etre vide.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (mConnectedUser != null) {
            mConnectedUser.setName(newName);
            mDataManager.sendUser(mConnectedUser);

            // Rafraichir la vue avec le nouveau nom
            mView.setUser(newName, mConnectedUser.getUserTag());

            JOptionPane.showMessageDialog(mView,
                "Nom modifie avec succes.",
                "Profil",
                JOptionPane.INFORMATION_MESSAGE);

            for (IProfileEditorObserver observer : mObservers) {
                observer.notifyNameChanged(newName);
            }
        }
    }

    /**
     * Gere la suppression du compte.
     */
    public void handleDelete() {
        int confirm = JOptionPane.showConfirmDialog(mView,
            "Etes-vous sur de vouloir supprimer votre compte ?\nCette action est irreversible.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION && mConnectedUser != null) {
            // Supprimer le fichier user du repertoire d'echange
            mDataManager.deleteUser(mConnectedUser);

            for (IProfileEditorObserver observer : mObservers) {
                observer.notifyAccountDeleted();
            }
        }
    }

    /**
     * Gere le retour a l'accueil.
     */
    public void handleBack() {
        for (IProfileEditorObserver observer : mObservers) {
            observer.notifyBackToHome();
        }
    }

    /**
     * Ajoute un observateur.
     */
    public void addObserver(IProfileEditorObserver observer) {
        mObservers.add(observer);
    }
}
