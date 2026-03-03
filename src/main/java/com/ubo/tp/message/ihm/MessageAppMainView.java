package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.ihm.login.LoginController;
import main.java.com.ubo.tp.message.ihm.login.LoginView;
import main.java.com.ubo.tp.message.ihm.login.RegisterController;
import main.java.com.ubo.tp.message.ihm.login.RegisterView;

/**
 * Classe de la vue principale de l'application.
 */
public class MessageAppMainView extends JPanel {
    
    private DataManager mDataManager;
    private LoginView mLoginView;
    private LoginController mLoginController;
    private RegisterView mRegisterView;
    private RegisterController mRegisterController;
    
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
        
     // Vue de login
        mLoginView = new LoginView();
        mLoginController = new LoginController(mLoginView, mDataManager, this);
        mRegisterView = new RegisterView();
        mRegisterController = new RegisterController(mRegisterView, mDataManager, this);
        
        add(mLoginView, BorderLayout.CENTER);
       
    }
    
    /**
     * Affiche la vue de connexion.
     */
    public void showLoginView() {
        removeAll();
        add(mLoginView, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    /**
     * Affiche la vue de création de compte.
     */
    public void showRegisterView() {
        removeAll();
        add(mRegisterView, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}