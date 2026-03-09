package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

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
    public HomeView(ProfileComponent profileComponent, UserComponent userComponent) {
        initComponents(profileComponent, userComponent);
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents(ProfileComponent profileComponent, UserComponent userComponent) {
        setLayout(new BorderLayout());

        // === Header ===
        mHeaderLabel = new JLabel("Accueil");
        JPanel profilePanel = Theme.createRightAlignedPanel();
        profilePanel.add(profileComponent.getView());
        add(Theme.createHeaderBar(mHeaderLabel, profilePanel), BorderLayout.NORTH);

        // === Sidebar gauche (utilisateurs) ===
        JPanel sidebarPanel = userComponent.getView();
        sidebarPanel.setPreferredSize(new Dimension(250, 0));

        // === Zone centrale (contenu futur : messages, channels...) ===
        mCenterContent = Theme.createPlaceholderPanel("Selectionnez un utilisateur ou un canal");

        // === SplitPane : sidebar | contenu ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, mCenterContent);
        splitPane.setDividerLocation(250);
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
}