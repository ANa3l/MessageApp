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
import main.java.com.ubo.tp.message.ihm.channel.ChannelDetailComponent;
import main.java.com.ubo.tp.message.ihm.channel.IChannelDetailObserver;
import main.java.com.ubo.tp.message.ihm.channel.creator.ChannelCreatorComponent;
import main.java.com.ubo.tp.message.ihm.channel.creator.IChannelCreatorObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.ihm.message.MessageComponent;
import main.java.com.ubo.tp.message.datamodel.IMessageRecipient;
import main.java.com.ubo.tp.message.ihm.message.IMessageObserver;
import main.java.com.ubo.tp.message.ihm.register.IRegisterObserver;
import main.java.com.ubo.tp.message.ihm.register.RegisterComponent;
import main.java.com.ubo.tp.message.ihm.user.IUserObserver;
import main.java.com.ubo.tp.message.ihm.user.UserComponent;

/**
 * Vue principale de l'application.
 */
public class MessageAppMainontroller extends JPanel {

    private DataManager mDataManager;
    private IDatabase mDatabase;
    private Session mSession;
    private User mCurrentUser;
    private LoginComponent mLoginComponent;
    private RegisterComponent mRegisterComponent;
    private ProfileComponent mProfileComponent;
    private ProfileEditorComponent mProfileEditorComponent;
    private UserComponent mUserComponent;
    private ChannelComponent mChannelComponent;
    private ChannelCreatorComponent mChannelCreatorComponent;
    private ChannelDetailComponent mChannelDetailComponent;
    private MessageComponent mMessageComponent;
    private HomeView mHomeView;

    /**
     * Constructeur.
     */
    public MessageAppMainontroller(DataManager dataManager, IDatabase database, Session session) {
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
        mUserComponent.addObserver(new IUserObserver() {
            @Override
            public void notifyUserSelected(User selectedUser) {
                showConversation(selectedUser);
            }
        });

        // Composant Channel
        mChannelComponent = new ChannelComponent(mDataManager, mDatabase);
        mChannelComponent.addObserver(new IChannelObserver() {
            @Override
            public void notifyChannelSelected(Channel selectedChannel) {
                showConversation(selectedChannel);
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

        // Composant ChannelDetail
        mChannelDetailComponent = new ChannelDetailComponent(mDataManager, mDatabase);
        mChannelDetailComponent.addObserver(new IChannelDetailObserver() {
            @Override
            public void notifyChannelDeleted() {
                mHomeView.resetCenterContent();
            }

            @Override
            public void notifyChannelLeft() {
                mHomeView.resetCenterContent();
            }
        });
        mChannelDetailComponent.addCloseListener(e -> {
            IMessageRecipient r = mMessageComponent.getRecipient();
            if (r != null) {
                showConversation(r);
            } else {
                mHomeView.resetCenterContent();
            }
        });

        // Composant Messages
        mMessageComponent = new MessageComponent(mDataManager, mDatabase);
        mMessageComponent.addObserver(new IMessageObserver() {
            @Override
            public void notifyConversationClosed() {
                mHomeView.resetCenterContent();
            }

            @Override
            public void notifyNewNotification(String title, String text,
                    java.util.UUID senderUuid, java.util.UUID channelUuid) {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mHomeView.addNotification(title, text, senderUuid, channelUuid);
                });
            }

            @Override
            public void notifyUnreadMessage(java.util.UUID senderUuid) {
                javax.swing.SwingUtilities.invokeLater(() -> mUserComponent.addUnread(senderUuid));
            }

            @Override
            public void notifyUnreadChannel(java.util.UUID channelUuid) {
                javax.swing.SwingUtilities.invokeLater(() -> mChannelComponent.addUnread(channelUuid));
            }
        });
        mMessageComponent.addSettingsListener(e -> {
            IMessageRecipient r = mMessageComponent.getRecipient();
            if (r instanceof Channel) {
                showChannelDetail((Channel) r);
            }
        });

        // Vue Home
        mHomeView = new HomeView(mProfileComponent, mUserComponent, mChannelComponent);

        // Navigation depuis les notifications
        mHomeView.addNotificationClickListener(e -> {
            int index = Integer.parseInt(e.getActionCommand());
            HomeView.NotificationEntry entry = mHomeView.getNotification(index);
            if (entry != null) {
                if (entry.channelUuid != null) {
                    for (Channel ch : mDataManager.getChannels()) {
                        if (ch.getUuid().equals(entry.channelUuid)) {
                            showConversation(ch);
                            break;
                        }
                    }
                } else if (entry.senderUuid != null) {
                    for (User u : mDataManager.getUsers()) {
                        if (u.getUuid().equals(entry.senderUuid)) {
                            showConversation(u);
                            break;
                        }
                    }
                }
            }
        });

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
                if (mCurrentUser != null) {
                    mDataManager.deletePresence(mCurrentUser);
                    mCurrentUser = null;
                }
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
        mCurrentUser = connectedUser;
        mDataManager.sendPresence(connectedUser);
        mProfileComponent.setConnectedUser(connectedUser);
        mProfileEditorComponent.setConnectedUser(connectedUser);
        mUserComponent.setConnectedUser(connectedUser);
        mChannelComponent.setConnectedUser(connectedUser);
        mChannelCreatorComponent.setConnectedUser(connectedUser);
        mMessageComponent.setConnectedUser(connectedUser);
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
     * Nettoyage de présence (utilisé par le ShutdownHook).
     */
    public void cleanupPresence() {
        if (mCurrentUser != null) {
            mDataManager.deletePresence(mCurrentUser);
            mCurrentUser = null;
        }
    }

    /**
     * Affiche le formulaire de creation de canal dans la zone centrale.
     */
    private void showChannelCreator() {
        mHomeView.setCenterContent(mChannelCreatorComponent.getView());
    }

    /**
     * Ouvre la conversation avec un destinataire (User ou Channel).
     */
    private void showConversation(IMessageRecipient recipient) {
        if (recipient instanceof User) {
            mUserComponent.clearUnread(recipient.getUuid());
        }
        if (recipient instanceof Channel) {
            mChannelComponent.clearUnread(recipient.getUuid());
        }
        mMessageComponent.setRecipient(recipient, mSession.getConnectedUser());
        mHomeView.setCenterContent(mMessageComponent.getView());
    }

    /**
     * Affiche le detail d'un canal dans la zone centrale.
     */
    private void showChannelDetail(Channel channel) {
        mChannelDetailComponent.setChannel(channel, mSession.getConnectedUser());
        mHomeView.setCenterContent(mChannelDetailComponent.getView());
    }
}