package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Palette de couleurs, polices et methodes utilitaires partagees.
 * Theme metallique epure.
 */
public final class Theme {

    // === Couleurs principales ===
    public static final Color DARK = new Color(43, 45, 49);
    public static final Color DARK_SECONDARY = new Color(55, 57, 63);
    public static final Color SIDEBAR = new Color(240, 240, 243);
    public static final Color BACKGROUND = new Color(250, 250, 252);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(215, 215, 220);

    // === Texte ===
    public static final Color TEXT_PRIMARY = new Color(40, 40, 45);
    public static final Color TEXT_SECONDARY = new Color(120, 120, 130);
    public static final Color TEXT_LIGHT = new Color(170, 170, 180);

    // === Accents ===
    public static final Color ACCENT = new Color(88, 101, 242);
    public static final Color ACCENT_HOVER = new Color(71, 82, 196);
    public static final Color SELECTION = new Color(232, 235, 248);

    // === Navigation ===
    public static final Color NAV_BAR = new Color(43, 45, 49);
    public static final Color NAV_ICON = new Color(180, 182, 190);
    public static final Color NAV_ICON_ACTIVE = Color.WHITE;

    // === Polices ===
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 12);

    private Theme() {
    }

    // =============================================
    // === Methodes utilitaires (factory methods) ===
    // =============================================

    /**
     * Cree un header bar avec un label a gauche et un panneau a droite.
     */
    public static JPanel createHeaderBar(JLabel leftLabel, JPanel rightContent) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            new EmptyBorder(10, 15, 10, 15)
        ));
        header.setBackground(BACKGROUND);
        leftLabel.setFont(FONT_TITLE);
        header.add(leftLabel, BorderLayout.CENTER);

        rightContent.setOpaque(false);
        header.add(rightContent, BorderLayout.EAST);
        return header;
    }

    /**
     * Cree un panneau placeholder avec un message centre.
     */
    public static JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setForeground(TEXT_LIGHT);
        label.setFont(new Font("SansSerif", Font.ITALIC, 13));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Applique le style standard a un bouton.
     */
    public static void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Cree un panneau avec FlowLayout a droite, transparent.
     */
    public static JPanel createRightAlignedPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setOpaque(false);
        return panel;
    }
}
