package main.java.com.ubo.tp.message.ihm.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue du composant utilisateur.
 * Affiche la liste des utilisateurs enregistres.
 */
public class UserView extends JPanel {

    private JList<User> mUserList;
    private DefaultListModel<User> mListModel;
    private JTextField mSearchField;

    /**
     * Constructeur.
     */
    public UserView() {
        initComponents();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR);

        // === Header (titre + recherche) ===
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Theme.SIDEBAR);

        JLabel titleLabel = new JLabel("Utilisateurs");
        titleLabel.setFont(Theme.FONT_SUBTITLE);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);
        titleLabel.setBorder(new EmptyBorder(12, 14, 6, 14));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Theme.SIDEBAR);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Theme.SIDEBAR);
        searchPanel.setBorder(new EmptyBorder(0, 14, 10, 14));
        searchPanel.setAlignmentX(LEFT_ALIGNMENT);
        mSearchField = new JTextField();
        mSearchField.setFont(Theme.FONT_BODY);
        mSearchField.setToolTipText("Rechercher par nom ou tag");
        searchPanel.add(mSearchField, BorderLayout.CENTER);
        headerPanel.add(searchPanel);

        add(headerPanel, BorderLayout.NORTH);

        // === Liste ===
        mListModel = new DefaultListModel<>();
        mUserList = new JList<>(mListModel);
        mUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mUserList.setCellRenderer(new UserCellRenderer());
        mUserList.setFixedCellHeight(48);
        mUserList.setBackground(Theme.SIDEBAR);
        mUserList.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(mUserList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.SIDEBAR);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Met a jour la liste des utilisateurs affiches.
     */
    public void updateUserList(List<User> users) {
        mListModel.clear();
        for (User user : users) {
            mListModel.addElement(user);
        }
    }

    /**
     * Retourne le champ de recherche.
     */
    public JTextField getSearchField() {
        return mSearchField;
    }

    /**
     * Retourne l'utilisateur selectionne.
     */
    public User getSelectedUser() {
        return mUserList.getSelectedValue();
    }

    /**
     * Retourne la JList pour ajouter des listeners externes.
     */
    public JList<User> getUserList() {
        return mUserList;
    }

    // === Classes internes (rendu graphique) ===

    /**
     * Renderer pour les cellules utilisateur.
     */
    private static class UserCellRenderer extends JPanel implements ListCellRenderer<User> {

        private AvatarLabel mAvatar;
        private JLabel mNameLabel;
        private JLabel mTagLabel;

        public UserCellRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(new EmptyBorder(4, 10, 4, 10));
            setOpaque(true);

            mAvatar = new AvatarLabel();
            add(mAvatar, BorderLayout.WEST);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            infoPanel.add(Box.createVerticalGlue());

            mNameLabel = new JLabel();
            mNameLabel.setFont(Theme.FONT_BODY);
            infoPanel.add(mNameLabel);

            mTagLabel = new JLabel();
            mTagLabel.setFont(Theme.FONT_SMALL);
            mTagLabel.setForeground(Theme.TEXT_SECONDARY);
            infoPanel.add(mTagLabel);

            infoPanel.add(Box.createVerticalGlue());
            add(infoPanel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User user,
                int index, boolean isSelected, boolean cellHasFocus) {

            String initial = user.getName().isEmpty() ? "?" : user.getName().substring(0, 1).toUpperCase();
            mAvatar.setInitial(initial);
            mAvatar.setCircleColor(getAvatarColor(user.getUserTag()));

            mNameLabel.setText(user.getName());
            mTagLabel.setText("@" + user.getUserTag());

            if (isSelected) {
                setBackground(Theme.SELECTION);
                mNameLabel.setForeground(Theme.ACCENT);
            } else {
                setBackground(Theme.SIDEBAR);
                mNameLabel.setForeground(Theme.TEXT_PRIMARY);
            }

            return this;
        }

        private Color getAvatarColor(String tag) {
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
    }

    /**
     * Avatar rond avec initiale.
     */
    private static class AvatarLabel extends JPanel {

        private String mInitial = "";
        private Color mColor = Theme.ACCENT;

        public AvatarLabel() {
            setPreferredSize(new Dimension(34, 34));
            setOpaque(false);
        }

        public void setInitial(String initial) {
            this.mInitial = initial;
        }

        public void setCircleColor(Color color) {
            this.mColor = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(mColor);
            g2.fillOval(0, 0, 34, 34);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            int textWidth = g2.getFontMetrics().stringWidth(mInitial);
            int textHeight = g2.getFontMetrics().getAscent();
            g2.drawString(mInitial, (34 - textWidth) / 2, (34 + textHeight) / 2 - 2);

            g2.dispose();
        }
    }
}