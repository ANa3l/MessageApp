package main.java.com.ubo.tp.message.ihm.register;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Contrôleur du composant de création de compte.
 */
public class RegisterController {
    
    private RegisterView mView;
    private DataManager mDataManager;
    private RegisterComponent mComponent;
    
    /**
     * Constructeur.
     */
    public RegisterController(RegisterView view, DataManager dataManager, RegisterComponent component) {
        this.mView = view;
        this.mDataManager = dataManager;
        this.mComponent = component;
        initListeners();
    }
    
    /**
     * Initialisation des listeners.
     */
    private void initListeners() {
        mView.addCreateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateAccount();
            }
        });
        
        mView.addBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mComponent.notifyBackToLogin();
            }
        });
    }
    
    /**
     * Gestion de la création de compte.
     * SRS-MAP-USR-001 : Enregistrement compte (nom, tag)
     * SRS-MAP-USR-002 : Tag et nom obligatoires
     * SRS-MAP-USR-003 : Tag unique
     */
    private void handleCreateAccount() {
        String tag = mView.getTagValue();
        String name = mView.getNameValue();
        String password = mView.getPasswordValue();
        
        // SRS-MAP-USR-002 : Validation
        if (tag.isEmpty() || name.isEmpty()) {
            showError("Le tag et le nom sont obligatoires.");
            return;
        }
        
        // SRS-MAP-USR-003 : Unicité du tag
        if (findUserByTag(tag) != null) {
            showError("Ce tag existe déjà.");
            return;
        }
        
        // Création de l'utilisateur
        User newUser = new User(tag, password, name);
        mDataManager.sendUser(newUser);
        
        showInfo("Compte créé avec succès !");
        mView.clearFields();
        mComponent.notifyAccountCreated();
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