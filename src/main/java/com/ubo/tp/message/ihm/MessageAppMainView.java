package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.login.ILoginObserver;
import main.java.com.ubo.tp.message.ihm.login.LoginComponent;
import main.java.com.ubo.tp.message.ihm.register.IRegisterObserver;
import main.java.com.ubo.tp.message.ihm.register.RegisterComponent;

/**
 * Vue principale de l'application.
 */
public class MessageAppMainView extends JPanel {

    private DataManager mDataManager;
    private Session mSession;
    private LoginComponent mLoginComponent;
    private RegisterComponent mRegisterComponent;
    private HomeView mHomeView;

    /**
     * Constructeur.
     */
    public MessageAppMainView(DataManager dataManager, Session session) {
        this.mDataManager = dataManager;
        this.mSession = session;
        initComponents();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Composant Login
        mLoginComponent = new LoginComponent(mDataManager);
        mLoginComponent.addObserver(new ILoginObserver() {
            @Override
            public void notifyLogin(User connectedUser) {
                handleLogin(connectedUser);
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

        // Vue Home
        mHomeView = new HomeView();

        // Affichage initial
        showLoginView();
    }

    /**
     * Gestion de la connexion réussie.
     */
    private void handleLogin(User connectedUser) {
        mSession.connect(connectedUser);
        showHomeView(connectedUser);
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

    /**
     * Affiche la vue d'accueil après connexion.
     */
    public void showHomeView(User connectedUser) {
        mHomeView.setConnectedUser(connectedUser);
        removeAll();
        add(mHomeView, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}