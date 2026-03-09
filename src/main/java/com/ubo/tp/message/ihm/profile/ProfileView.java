package main.java.com.ubo.tp.message.ihm.profile;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue du composant profil.
 * Affiche un avatar rond avec pastille verte (connecte).
 * Au clic, affiche un menu contextuel.
 */
public class ProfileView extends JPanel {

    private static final int AVATAR_SIZE = 34;
    private static final int DOT_SIZE = 10;

    private String mInitial = "?";
    private Color mAvatarColor = Theme.ACCENT;

    private JPopupMenu mPopupMenu;
    private JMenuItem mProfileItem;
    private JMenuItem mLogoutItem;

    /**
     * Constructeur.
     */
    public ProfileView() {
        initComponents();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setPreferredSize(new Dimension(AVATAR_SIZE, AVATAR_SIZE));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Mon profil");

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

    /**
     * Met a jour l'affichage avec les infos du user connecte.
     */
    public void setUser(String name, String tag) {
        this.mInitial = name.isEmpty() ? "?" : name.substring(0, 1).toUpperCase();
        this.mAvatarColor = getColorFromTag(tag);
        setToolTipText(name + " (@" + tag + ")");
        repaint();
    }

    /**
     * Affiche le menu popup sous l'avatar.
     */
    public void showPopup() {
        mPopupMenu.show(this, 0, getHeight());
    }

    /**
     * Retourne l'item "Profil".
     */
    public JMenuItem getProfileItem() {
        return mProfileItem;
    }

    /**
     * Retourne l'item "Se deconnecter".
     */
    public JMenuItem getLogoutItem() {
        return mLogoutItem;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cercle avatar
        g2.setColor(mAvatarColor);
        g2.fillOval(0, 0, AVATAR_SIZE, AVATAR_SIZE);

        // Initiale
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        int textWidth = g2.getFontMetrics().stringWidth(mInitial);
        int textHeight = g2.getFontMetrics().getAscent();
        g2.drawString(mInitial, (AVATAR_SIZE - textWidth) / 2, (AVATAR_SIZE + textHeight) / 2 - 2);

        // Pastille verte (en bas a droite)
        int dotX = AVATAR_SIZE - DOT_SIZE;
        int dotY = AVATAR_SIZE - DOT_SIZE;
        g2.setColor(Theme.BACKGROUND);
        g2.fillOval(dotX - 2, dotY - 2, DOT_SIZE + 4, DOT_SIZE + 4);
        g2.setColor(new Color(67, 181, 129));
        g2.fillOval(dotX, dotY, DOT_SIZE, DOT_SIZE);

        g2.dispose();
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
}
