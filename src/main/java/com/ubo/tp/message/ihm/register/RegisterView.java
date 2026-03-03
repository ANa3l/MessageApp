package main.java.com.ubo.tp.message.ihm.register;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Vue du composant de création de compte.
 */
public class RegisterView extends JPanel {
    
    private JTextField mTagField;
    private JTextField mNameField;
    private JPasswordField mPasswordField;
    private JButton mCreateButton;
    private JButton mBackButton;
    
    /**
     * Constructeur.
     */
    public RegisterView() {
        initComponents();
    }
    
    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        
        // Panel conteneur avec bordure
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Création de compte"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Label Tag
        JLabel tagLabel = new JLabel("Tag :");
        registerPanel.add(tagLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 10), 0, 0));
        
        // Champ Tag
        mTagField = new JTextField(20);
        registerPanel.add(mTagField, new GridBagConstraints(1, 0, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5), 0, 0));
        
        // Label Nom
        JLabel nameLabel = new JLabel("Nom :");
        registerPanel.add(nameLabel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 10), 0, 0));
        
        // Champ Nom
        mNameField = new JTextField(20);
        registerPanel.add(mNameField, new GridBagConstraints(1, 1, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5), 0, 0));
        
        // Label Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe :");
        registerPanel.add(passwordLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets(5, 5, 5, 10), 0, 0));
        
        // Champ Mot de passe
        mPasswordField = new JPasswordField(20);
        registerPanel.add(mPasswordField, new GridBagConstraints(1, 2, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets(5, 5, 5, 5), 0, 0));
        
        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        mCreateButton = new JButton("Créer");
        mBackButton = new JButton("Retour");
        
        buttonPanel.add(mCreateButton);
        buttonPanel.add(mBackButton);
        
        registerPanel.add(buttonPanel, new GridBagConstraints(0, 3, 2, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(15, 5, 5, 5), 0, 0));
        
        // Ajout du panel centré
        add(registerPanel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    }
    
    // === Getters ===
    
    public String getTagValue() {
        return mTagField.getText().trim();
    }
    
    public String getNameValue() {
        return mNameField.getText().trim();
    }
    
    public String getPasswordValue() {
        return new String(mPasswordField.getPassword());
    }
    
    public void clearFields() {
        mTagField.setText("");
        mNameField.setText("");
        mPasswordField.setText("");
    }
    
    // === Listeners ===
    
    public void addCreateListener(ActionListener listener) {
        mCreateButton.addActionListener(listener);
    }
    
    public void addBackListener(ActionListener listener) {
        mBackButton.addActionListener(listener);
    }
}