package main.java.com.ubo.tp.message.ihm.profile;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue du composant profil.
 * Affiche le nom de l'utilisateur + un avatar rond avec pastille verte.
 * Au clic, affiche un menu contextuel.
 */
public class ProfileView extends JPanel {

    private JLabel mNameLabel;
    private AvatarPanel mAvatarPanel;

    private JPopupMenu mPopupMenu;
    private JMenuItem mProfileItem;
    private JMenuItem mLogoutItem;

    public ProfileView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Mon profil");

        // Label nom
        mNameLabel = new JLabel();
        mNameLabel.setFont(Theme.FONT_BODY);
        mNameLabel.setForeground(Theme.TEXT_LIGHT);
        add(mNameLabel);

        // Avatar
        mAvatarPanel = new AvatarPanel();
        add(mAvatarPanel);

        // Menu contextuel
        mPopupMenu = new JPopupMenu();

        mProfileItem = new JMenuItem("Profil");
        mProfileItem.setFont(Theme.FONT_BODY);
        mPopupMenu.add(mProfileItem);

        mPopupMenu.addSeparator();

        mLogoutItem = new JMenuItem("Se deconnecter");
        mLogoutItem.setFont(Theme.FONT_BODY);
        mLogoutItem.setForeground(new Color(231, 76, 60));
        mPopupMenu.add(mLogoutItem);
    }

    public void setUser(String name, String tag) {
        mNameLabel.setText(name);
        mAvatarPanel.setInitial(name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase());
        mAvatarPanel.setCircleColor(getColorFromTag(tag));
        setToolTipText(name + " (@" + tag + ")");
    }

    public void showPopup() {
        mPopupMenu.show(mAvatarPanel, 0, mAvatarPanel.getHeight());
    }

    public JMenuItem getProfileItem() {
        return mProfileItem;
    }

    public JMenuItem getLogoutItem() {
        return mLogoutItem;
    }

    private Color getColorFromTag(String tag) {
        Color[] colors = {
            new Color(88, 101, 242),  new Color(87, 169, 131),
            new Color(237, 135, 82),  new Color(155, 89, 182),
            new Color(231, 76, 60),   new Color(52, 152, 219),
            new Color(149, 165, 166), new Color(241, 196, 15)
        };
        return colors[Math.abs(tag.hashCode()) % colors.length];
    }

    /**
     * Panneau avatar rond avec pastille verte.
     */
    private static class AvatarPanel extends JPanel {

        private static final int AVATAR_SIZE = 34;
        private static final int DOT_SIZE = 10;
        private String mInitial = "?";
        private Color mColor = Theme.ACCENT;

        public AvatarPanel() {
            setPreferredSize(new Dimension(AVATAR_SIZE, AVATAR_SIZE));
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
            g2.fillOval(0, 0, AVATAR_SIZE, AVATAR_SIZE);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            int tw = g2.getFontMetrics().stringWidth(mInitial);
            int th = g2.getFontMetrics().getAscent();
            g2.drawString(mInitial, (AVATAR_SIZE - tw) / 2, (AVATAR_SIZE + th) / 2 - 2);

            int dotX = AVATAR_SIZE - DOT_SIZE;
            int dotY = AVATAR_SIZE - DOT_SIZE;
            g2.setColor(Theme.BACKGROUND);
            g2.fillOval(dotX - 2, dotY - 2, DOT_SIZE + 4, DOT_SIZE + 4);
            g2.setColor(new Color(67, 181, 129));
            g2.fillOval(dotX, dotY, DOT_SIZE, DOT_SIZE);

            g2.dispose();
        }
    }
}