package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.logout.LogoutComponent;

/**
 * Vue principale après connexion.
 * Assemble les composants.
 */
public class HomeView extends JPanel {

    private JLabel mWelcomeLabel;

    /**
     * Constructeur.
     */
    public HomeView(LogoutComponent logoutComponent) {
        initComponents(logoutComponent);
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents(LogoutComponent logoutComponent) {
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

        // Contenu central
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Accueil"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel placeholderLabel = new JLabel("Contenu à venir...");
        contentPanel.add(placeholderLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Met à jour l'affichage avec l'utilisateur connecté.
     */
    public void setConnectedUser(User user) {
        mWelcomeLabel.setText("Bienvenue " + user.getName() + " (@" + user.getUserTag() + ") !");
    }
}