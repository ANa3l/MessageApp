package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.logout.LogoutComponent;
import main.java.com.ubo.tp.message.ihm.user.UserComponent;

/**
 * Vue principale apres connexion.
 * Assemble les composants.
 */
public class HomeView extends JPanel {

    private JLabel mWelcomeLabel;
    private JPanel mCenterContent;

    /**
     * Constructeur.
     */
    public HomeView(LogoutComponent logoutComponent, UserComponent userComponent) {
        initComponents(logoutComponent, userComponent);
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents(LogoutComponent logoutComponent, UserComponent userComponent) {
        setLayout(new BorderLayout());

        // === Header ===
        mWelcomeLabel = new JLabel("Bienvenue !");
        JPanel logoutPanel = Theme.createRightAlignedPanel();
        logoutPanel.add(logoutComponent.getView());
        add(Theme.createHeaderBar(mWelcomeLabel, logoutPanel), BorderLayout.NORTH);

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

    /**
     * Met a jour l'affichage avec l'utilisateur connecte.
     */
    public void setConnectedUser(User user) {
        mWelcomeLabel.setText("Bienvenue " + user.getName() + " (@" + user.getUserTag() + ") !");
    }
}