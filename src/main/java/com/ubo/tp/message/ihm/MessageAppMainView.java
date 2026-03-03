package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.login.ILoginObserver;
import main.java.com.ubo.tp.message.ihm.login.LoginComponent;
import main.java.com.ubo.tp.message.ihm.register.IRegisterObserver;
import main.java.com.ubo.tp.message.ihm.register.RegisterComponent;

/**
 * Classe de la vue principale de l'application.
 */
public class MessageAppMainView extends JPanel {
    
    private DataManager mDataManager;
    private LoginComponent mLoginComponent;
    private RegisterComponent mRegisterComponent;
    
    /**
     * Constructeur.
     *
     * @param dataManager
     */
    public MessageAppMainView(DataManager dataManager) {
        this.mDataManager = dataManager;
        initComponents();
    }
    
    /**
     * Initialisation des composants de la vue.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Composant Login
        mLoginComponent = new LoginComponent(mDataManager);
        mLoginComponent.addObserver(new ILoginObserver() {
            @Override
            public void notifyLogin(User connectedUser) {
                System.out.println("[APP] Utilisateur connecté : " + connectedUser.getName());
                // TODO : Afficher la vue principale
            }
            
            @Override
            public void notifyRegisterRequest() {
                showRegisterView();
            }
        });
        
        // Composant Register
        mRegisterComponent = new RegisterComponent(mDataManager);
        mRegisterComponent.addObserver(new IRegisterObserver() {
            @Override
            public void notifyAccountCreated() {
                showLoginView();
            }
            
            @Override
            public void notifyBackToLogin() {
                showLoginView();
            }
        });
        
        // Affichage initial
        showLoginView();
    }
    
    /**
     * Affiche la vue de connexion.
     */
    public void showLoginView() {
        removeAll();
        add(mLoginComponent.getView(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * Affiche la vue de création de compte.
     */
    public void showRegisterView() {
        removeAll();
        add(mRegisterComponent.getView(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}