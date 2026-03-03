package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Vue principale après connexion.
 */
public class HomeView extends JPanel {

    private JLabel mWelcomeLabel;

    /**
     * Constructeur.
     */
    public HomeView() {
        initComponents();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Accueil"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        mWelcomeLabel = new JLabel("Bienvenue !");
        mWelcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        homePanel.add(mWelcomeLabel, BorderLayout.CENTER);

        add(homePanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(10, 10, 10, 10), 0, 0));
    }

    /**
     * Met à jour l'affichage avec l'utilisateur connecté.
     */
    public void setConnectedUser(User user) {
        mWelcomeLabel.setText("Bienvenue " + user.getName() + " (@" + user.getUserTag() + ") !");
    }
}
