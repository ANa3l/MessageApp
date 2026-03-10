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
import main.java.com.ubo.tp.message.ihm.profile.IProfileObserver;
import main.java.com.ubo.tp.message.ihm.profile.ProfileComponent;
import main.java.com.ubo.tp.message.ihm.profile.editor.IProfileEditorObserver;
import main.java.com.ubo.tp.message.ihm.profile.editor.ProfileEditorComponent;
import main.java.com.ubo.tp.message.ihm.channel.ChannelComponent;
import main.java.com.ubo.tp.message.ihm.channel.IChannelObserver;
import main.java.com.ubo.tp.message.ihm.channel.creator.ChannelCreatorComponent;
import main.java.com.ubo.tp.message.ihm.channel.creator.IChannelCreatorObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
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
    private ProfileComponent mProfileComponent;
    private ProfileEditorComponent mProfileEditorComponent;
    private UserComponent mUserComponent;
    private ChannelComponent mChannelComponent;
    private ChannelCreatorComponent mChannelCreatorComponent;
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

        // Composant Profil
        mProfileComponent = new ProfileComponent();
        mProfileComponent.addObserver(new IProfileObserver() {
            @Override
            public void notifyLogoutRequest() {
                mSession.disconnect();
            }

            @Override
            public void notifyProfileRequest() {
                showProfileEditor();
            }
        });

        // Composant ProfileEditor
        mProfileEditorComponent = new ProfileEditorComponent(mDataManager);
        mProfileEditorComponent.addObserver(new IProfileEditorObserver() {
            @Override
            public void notifyNameChanged(String newName) {
                // Mettre a jour l'avatar du ProfileComponent
                User user = mSession.getConnectedUser();
                if (user != null) {
                    mProfileComponent.setConnectedUser(user);
                }
            }

            @Override
            public void notifyAccountDeleted() {
                mSession.disconnect();
            }

            @Override
            public void notifyBackToHome() {
                mHomeView.resetCenterContent();
            }
        });

        // Composant User
        mUserComponent = new UserComponent(mDataManager, mDatabase);

        // Composant Channel
        mChannelComponent = new ChannelComponent(mDataManager, mDatabase);
        mChannelComponent.addObserver(new IChannelObserver() {
            @Override
            public void notifyChannelSelected(Channel selectedChannel) {
                // TODO étape suivante : afficher les messages du canal
            }

            @Override
            public void notifyCreateChannelRequest() {
                showChannelCreator();
            }
        });

        // Composant ChannelCreator
        mChannelCreatorComponent = new ChannelCreatorComponent(mDataManager);
        mChannelCreatorComponent.addObserver(new IChannelCreatorObserver() {
            @Override
            public void notifyChannelCreated(Channel channel) {
                mHomeView.resetCenterContent();
            }

            @Override
            public void notifyCreationCancelled() {
                mHomeView.resetCenterContent();
            }
        });

        // Vue Home
        mHomeView = new HomeView(mProfileComponent, mUserComponent, mChannelComponent);

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
        mProfileComponent.setConnectedUser(connectedUser);
        mProfileEditorComponent.setConnectedUser(connectedUser);
        mUserComponent.setConnectedUser(connectedUser);
        mChannelComponent.setConnectedUser(connectedUser);
        mChannelCreatorComponent.setConnectedUser(connectedUser);
        mHomeView.resetCenterContent();
        removeAll();
        add(mHomeView, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Affiche l'editeur de profil dans la zone centrale.
     */
    private void showProfileEditor() {
        mHomeView.setCenterContent(mProfileEditorComponent.getView());
    }

    /**
     * Affiche le formulaire de creation de canal dans la zone centrale.
     */
    private void showChannelCreator() {
        mHomeView.setCenterContent(mChannelCreatorComponent.getView());
    }
}