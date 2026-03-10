package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import main.java.com.ubo.tp.message.ihm.channel.ChannelComponent;
import main.java.com.ubo.tp.message.ihm.profile.ProfileComponent;
import main.java.com.ubo.tp.message.ihm.user.UserComponent;

/**
 * Vue principale apres connexion.
 * Assemble les composants.
 */
public class HomeView extends JPanel {

    private JLabel mHeaderLabel;
    private JPanel mCenterContent;

    /**
     * Constructeur.
     */
    public HomeView(ProfileComponent profileComponent, UserComponent userComponent, ChannelComponent channelComponent) {
        initComponents(profileComponent, userComponent, channelComponent);
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents(ProfileComponent profileComponent, UserComponent userComponent, ChannelComponent channelComponent) {
        setLayout(new BorderLayout());

        // === Header ===
        mHeaderLabel = new JLabel("Accueil");
        JPanel profilePanel = Theme.createRightAlignedPanel();
        profilePanel.add(profileComponent.getView());
        add(Theme.createHeaderBar(mHeaderLabel, profilePanel), BorderLayout.NORTH);

        // === Sidebar gauche (onglets Utilisateurs + Canaux) ===
        JTabbedPane sidebarTabs = new JTabbedPane();
        sidebarTabs.setFont(Theme.FONT_SUBTITLE);
        sidebarTabs.setBackground(Theme.SIDEBAR);
        sidebarTabs.addTab("Utilisateurs", userComponent.getView());
        sidebarTabs.addTab("Canaux", channelComponent.getView());
        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setPreferredSize(new Dimension(260, 0));
        sidebarWrapper.add(sidebarTabs, BorderLayout.CENTER);

        // === Zone centrale (contenu futur : messages, channels...) ===
        mCenterContent = Theme.createPlaceholderPanel("Selectionnez un utilisateur ou un canal");

        // === SplitPane : sidebar | contenu ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarWrapper, mCenterContent);
        splitPane.setDividerLocation(260);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Retourne le panneau central pour y ajouter du contenu.
     */
    public JPanel getCenterContent() {
        return mCenterContent;
    }

    /**
     * Remplace le contenu de la zone centrale.
     */
    public void setCenterContent(JPanel content) {
        mCenterContent.removeAll();
        mCenterContent.add(content, BorderLayout.CENTER);
        mCenterContent.revalidate();
        mCenterContent.repaint();
    }

    /**
     * Remet le contenu par defaut dans la zone centrale.
     */
    public void resetCenterContent() {
        mCenterContent.removeAll();
        mCenterContent.setLayout(new BorderLayout());
        JPanel placeholder = Theme.createPlaceholderPanel("Selectionnez un utilisateur ou un canal");
        mCenterContent.add(placeholder, BorderLayout.CENTER);
        mCenterContent.revalidate();
        mCenterContent.repaint();
    }
}