package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.core.session.ISessionObserver;
import main.java.com.ubo.tp.message.core.session.Session;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.login.ILoginObserver;
import main.java.com.ubo.tp.message.ihm.login.LoginComponent;
import main.java.com.ubo.tp.message.ihm.logout.ILogoutObserver;
import main.java.com.ubo.tp.message.ihm.logout.LogoutComponent;
import main.java.com.ubo.tp.message.ihm.register.IRegisterObserver;
import main.java.com.ubo.tp.message.ihm.register.RegisterComponent;
import main.java.com.ubo.tp.message.ihm.user.UserComponent;

/**
 * Vue principale de l'application.
 */
public class MessageAppMainView extends JPanel {

    private DataManager mDataManager;
    private IDatabase mDatabase;
    private Session mSession;
    private LoginComponent mLoginComponent;
    private RegisterComponent mRegisterComponent;
    private LogoutComponent mLogoutComponent;
    private UserComponent mUserComponent;
    private HomeView mHomeView;

    /**
     * Constructeur.
     */
    public MessageAppMainView(DataManager dataManager, IDatabase database, Session session) {
        this.mDataManager = dataManager;
        this.mDatabase = database;
        this.mSession = session;
        initComponents();
        initSessionObserver();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Composant Login
        mLoginComponent = new LoginComponent(mDataManager);
        mLoginComponent.addObserver(new ILoginObserver() {
            @Override
            public void notifyLogin(User connectedUser) {
                // La session déclenche la navigation
                mSession.connect(connectedUser);
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

        // Composant Logout
        mLogoutComponent = new LogoutComponent();
        mLogoutComponent.addObserver(new ILogoutObserver() {
            @Override
            public void notifyLogout() {
                // La session déclenche la navigation
                mSession.disconnect();
            }
        });

        // Composant User
        mUserComponent = new UserComponent(mDataManager, mDatabase);

        // Vue Home
        mHomeView = new HomeView(mLogoutComponent, mUserComponent);

        // Affichage initial
        showLoginView();
    }

    /**
     * Observe la session pour naviguer automatiquement.
     */
    private void initSessionObserver() {
        mSession.addObserver(new ISessionObserver() {
            @Override
            public void notifyLogin(User connectedUser) {
                showHomeView(connectedUser);
            }

            @Override
            public void notifyLogout() {
                showLoginView();
            }
        });
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