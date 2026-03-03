package main.java.com.ubo.tp.message.ihm.login;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Vue du composant de login.
 */
public class LoginView extends JPanel {
    
    private JTextField mTagField;
    private JPasswordField mPasswordField; // ← Changé de JTextField à JPasswordField
    private JButton mLoginButton;
    private JButton mCreateAccountButton;
    
    /**
     * Constructeur.
     */
    public LoginView() {
        initComponents();
    }
    
    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        
        // Panel conteneur avec bordure
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Authentification"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Label Tag
        JLabel tagLabel = new JLabel("Tag :");
        loginPanel.add(tagLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 10), 0, 0));
        
        // Champ Tag
        mTagField = new JTextField(40);
        loginPanel.add(mTagField, new GridBagConstraints(1, 0, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5), 0, 0));
        
        // Label Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe :");
        loginPanel.add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 10), 0, 0));
        
        // Champ Mot de passe
        mPasswordField = new JPasswordField(40); // ← JPasswordField
        loginPanel.add(mPasswordField, new GridBagConstraints(1, 1, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5), 0, 0));
        
        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        mLoginButton = new JButton("Se connecter");
        mCreateAccountButton = new JButton("Créer un compte");
        
        buttonPanel.add(mLoginButton);
        buttonPanel.add(mCreateAccountButton);
        
        loginPanel.add(buttonPanel, new GridBagConstraints(0, 2, 2, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(15, 5, 5, 5), 0, 0));
        
        // Ajout du panel centré dans la vue
        add(loginPanel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    }
    
    // === Getters ===
    
    public String getTagValue() {
        return mTagField.getText().trim();
    }

    public String getPasswordValue() { // ← Renommé
        return new String(mPasswordField.getPassword());
    }

    public void clearFields() {
        mTagField.setText("");
        mPasswordField.setText("");
    }
    
    // === Listeners ===
    
    public void addLoginListener(ActionListener listener) {
        mLoginButton.addActionListener(listener);
    }

    public void addCreateAccountListener(ActionListener listener) {
        mCreateAccountButton.addActionListener(listener);
    }
}