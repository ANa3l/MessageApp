package main.java.com.ubo.tp.message.ihm.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.MessageAppMainView;

/**
 * Contrôleur du composant de login.
 */
public class LoginController {
    
    private LoginView mView;
    private DataManager mDataManager;
    private MessageAppMainView mMainView;
    
    /**
     * Constructeur.
     */
    public LoginController(LoginView view, DataManager dataManager, MessageAppMainView mainView) {
        this.mView = view;
        this.mDataManager = dataManager;
        this.mMainView = mainView;
        initListeners();
    }
    
    /**
     * Initialisation des listeners.
     */
    private void initListeners() {
        mView.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        mView.addCreateAccountListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mMainView.showRegisterView();
            }
        });
    }
    
    /**
     * Gestion de la connexion.
     * SRS-MAP-USR-004 : Connexion sur compte existant
     */
    private void handleLogin() {
        String tag = mView.getTagValue();
        String password = mView.getPasswordValue(); // ← Ajout
        
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
        
        // Vérification tag + mot de passe
        if (user != null && user.getUserPassword().equals(password)) {
            showInfo("Bienvenue " + user.getName() + " !");
            mView.clearFields();
            // TODO : Gérer la session et passer à la vue principale
        } else {
            showError("Tag ou mot de passe incorrect.");
        }
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