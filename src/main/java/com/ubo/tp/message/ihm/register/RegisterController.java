package main.java.com.ubo.tp.message.ihm.register;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Controleur du composant de creation de compte.
 * Gere la logique d'inscription et notifie les observers.
 */
public class RegisterController {

    private RegisterView mView;
    private DataManager mDataManager;
    private List<IRegisterObserver> mObservers;

    /**
     * Constructeur.
     */
    public RegisterController(RegisterView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
        this.mObservers = new ArrayList<>();
    }

    /**
     * Gestion de la creation de compte.
     * SRS-MAP-USR-001 : Enregistrement compte (nom, tag)
     * SRS-MAP-USR-002 : Tag et nom obligatoires
     * SRS-MAP-USR-003 : Tag unique
     */
    public void handleCreateAccount() {
        String tag = mView.getTagValue();
        String name = mView.getNameValue();
        String password = mView.getPasswordValue();

        // SRS-MAP-USR-002 : Validation
        if (tag.isEmpty() || name.isEmpty()) {
            showError("Le tag et le nom sont obligatoires.");
            return;
        }

        // SRS-MAP-USR-003 : Unicite du tag
        if (findUserByTag(tag) != null) {
            showError("Ce tag existe deja.");
            return;
        }

        // Creation de l'utilisateur
        User newUser = new User(tag, password, name);
        mDataManager.sendUser(newUser);

        showInfo("Compte cree avec succes !");
        mView.clearFields();

        for (IRegisterObserver observer : mObservers) {
            observer.notifyAccountCreated();
        }
    }

    /**
     * Gestion du retour a la connexion.
     */
    public void handleBackToLogin() {
        for (IRegisterObserver observer : mObservers) {
            observer.notifyBackToLogin();
        }
    }

    /**
     * Ajoute un observateur.
     */
    public void addObserver(IRegisterObserver observer) {
        mObservers.add(observer);
    }
    
    /**
     * Recherche un utilisateur par tag.
     */
    private User findUserByTag(String tag) {
        for (User user : mDataManager.getUsers()) {
            if (user.getUserTag().equals(tag)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Affiche un message d'erreur.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(mView, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Affiche un message d'information.
     */
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(mView, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}