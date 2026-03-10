package main.java.com.ubo.tp.message.ihm.login;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Controleur du composant de login.
 * Gere la logique de connexion et notifie les observers.
 */
public class LoginController {

    private LoginView mView;
    private DataManager mDataManager;
    private List<ILoginObserver> mObservers;

    /**
     * Constructeur.
     */
    public LoginController(LoginView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
        this.mObservers = new ArrayList<>();
    }

    /**
     * Gestion de la connexion.
     * SRS-MAP-USR-004 : Connexion sur compte existant
     */
    public void handleLogin() {
        String tag = mView.getTagValue();
        String password = mView.getPasswordValue();

        // Validation
        if (tag.isEmpty()) {
            showError("Le tag est obligatoire.");
            return;
        }

        if (password.isEmpty()) {
            showError("Le mot de passe est obligatoire.");
            return;
        }

        User user = findUserByTag(tag);

        // Verification tag + mot de passe
        if (user != null && user.getUserPassword().equals(password)) {
            mView.clearFields();
            for (ILoginObserver observer : mObservers) {
                observer.notifyLogin(user);
            }
        } else {
            showError("Tag ou mot de passe incorrect.");
        }
    }

    /**
     * Gestion de la demande de creation de compte.
     */
    public void handleRegisterRequest() {
        for (ILoginObserver observer : mObservers) {
            observer.notifyRegisterRequest();
        }
    }

    /**
     * Ajoute un observateur.
     */
    public void addObserver(ILoginObserver observer) {
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
    
}