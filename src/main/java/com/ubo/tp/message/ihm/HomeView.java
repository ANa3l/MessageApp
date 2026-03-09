package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.logout.LogoutComponent;
import main.java.com.ubo.tp.message.ihm.user.UserComponent;

/**
 * Vue principale après connexion.
 * Assemble les composants.
 */
public class HomeView extends JPanel {

    private JLabel mWelcomeLabel;

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

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mWelcomeLabel = new JLabel("Bienvenue !");
        headerPanel.add(mWelcomeLabel, BorderLayout.CENTER);

        // Ajout du composant déconnexion
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.add(logoutComponent.getView());
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Liste des utilisateurs
        add(userComponent.getView(), BorderLayout.CENTER);
    }

    /**
     * Met à jour l'affichage avec l'utilisateur connecté.
     */
    public void setConnectedUser(User user) {
        mWelcomeLabel.setText("Bienvenue " + user.getName() + " (@" + user.getUserTag() + ") !");
    }
}