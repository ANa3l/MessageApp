package main.java.com.ubo.tp.message.ihm.profile.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue de l'editeur de profil.
 * Affiche les infos du profil, permet de modifier le nom et supprimer le compte.
 */
public class ProfileEditorView extends JPanel {

    private JLabel mTagLabel;
    private JTextField mNameField;
    private JButton mSaveButton;
    private JButton mDeleteButton;
    private JButton mBackButton;
    private AvatarPanel mAvatarPanel;

    /**
     * Constructeur.
     */
    public ProfileEditorView() {
        initComponents();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // === Panneau central avec formulaire ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.SURFACE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Avatar
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mAvatarPanel = new AvatarPanel();
        JPanel avatarWrapper = new JPanel();
        avatarWrapper.setOpaque(false);
        avatarWrapper.add(mAvatarPanel);
        formPanel.add(avatarWrapper, gbc);

        // Tag (lecture seule)
        gbc.gridy = 1;
        mTagLabel = new JLabel("@tag");
        mTagLabel.setFont(Theme.FONT_BODY);
        mTagLabel.setForeground(Theme.TEXT_SECONDARY);
        mTagLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(mTagLabel, gbc);

        // Separateur
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 5, 0);
        JLabel nameLabel = new JLabel("Nom d'utilisateur");
        nameLabel.setFont(Theme.FONT_SUBTITLE);
        nameLabel.setForeground(Theme.TEXT_PRIMARY);
        formPanel.add(nameLabel, gbc);

        // Champ nom
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        mNameField = new JTextField();
        mNameField.setFont(Theme.FONT_BODY);
        mNameField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(mNameField, gbc);

        // Bouton enregistrer
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 20, 0);
        mSaveButton = new JButton("Enregistrer");
        Theme.styleButton(mSaveButton, Theme.ACCENT, Color.WHITE);
        formPanel.add(mSaveButton, gbc);

        // Bouton supprimer
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        mDeleteButton = new JButton("Supprimer mon compte");
        Theme.styleButton(mDeleteButton, new Color(231, 76, 60), Color.WHITE);
        formPanel.add(mDeleteButton, gbc);

        // Bouton retour en haut
        mBackButton = new JButton("← Retour");
        Theme.styleButton(mBackButton, Theme.DARK_SECONDARY, Color.WHITE);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(mBackButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Centrer le formulaire
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(formPanel);
        add(centerWrapper, BorderLayout.CENTER);
    }

    /**
     * Met a jour la vue avec les infos du user.
     */
    public void setUser(String name, String tag) {
        mNameField.setText(name);
        mTagLabel.setText("@" + tag);
        String initial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
        mAvatarPanel.setInitial(initial);
        mAvatarPanel.setCircleColor(getColorFromTag(tag));
    }

    public JTextField getNameField() {
        return mNameField;
    }

    public JButton getSaveButton() {
        return mSaveButton;
    }

    public JButton getDeleteButton() {
        return mDeleteButton;
    }

    public JButton getBackButton() {
        return mBackButton;
    }

    public String getNameValue() {
        return mNameField.getText().trim();
    }

    /**
     * Couleur deterministe basee sur le tag.
     */
    private Color getColorFromTag(String tag) {
        Color[] colors = {
            new Color(88, 101, 242),
            new Color(87, 169, 131),
            new Color(237, 135, 82),
            new Color(155, 89, 182),
            new Color(231, 76, 60),
            new Color(52, 152, 219),
            new Color(149, 165, 166),
            new Color(241, 196, 15)
        };
        return colors[Math.abs(tag.hashCode()) % colors.length];
    }

    /**
     * Avatar rond grand format.
     */
    private static class AvatarPanel extends JPanel {

        private static final int SIZE = 64;
        private String mInitial = "?";
        private Color mColor = Theme.ACCENT;

        public AvatarPanel() {
            setPreferredSize(new Dimension(SIZE, SIZE));
            setOpaque(false);
        }

        public void setInitial(String initial) {
            this.mInitial = initial;
            repaint();
        }

        public void setCircleColor(Color color) {
            this.mColor = color;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(mColor);
            g2.fillOval(0, 0, SIZE, SIZE);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 24));
            int textWidth = g2.getFontMetrics().stringWidth(mInitial);
            int textHeight = g2.getFontMetrics().getAscent();
            g2.drawString(mInitial, (SIZE - textWidth) / 2, (SIZE + textHeight) / 2 - 2);

            g2.dispose();
        }
    }
}
