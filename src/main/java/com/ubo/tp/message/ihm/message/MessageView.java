package main.java.com.ubo.tp.message.ihm.message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue d'une conversation (messages + champ de saisie).
 * Affiche les messages de la conversation active et permet d'en envoyer.
 */
public class MessageView extends JPanel {

    private JLabel mHeaderLabel;
    private JPanel mHeaderPanel;
    private JButton mSettingsButton;
    private JPanel mMessagesPanel;
    private JScrollPane mScrollPane;
    private JTextArea mInputField;
    private JButton mSendButton;

    public MessageView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // === Header (cliquable pour les canaux) ===
        mHeaderPanel = new JPanel(new BorderLayout());
        mHeaderPanel.setBackground(Theme.BACKGROUND);
        mHeaderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(10, 15, 10, 15)));
        mHeaderLabel = new JLabel("Sélectionnez un contact");
        mHeaderLabel.setFont(Theme.FONT_TITLE);
        mHeaderLabel.setForeground(Theme.TEXT_PRIMARY);
        mHeaderPanel.add(mHeaderLabel, BorderLayout.CENTER);

        // Bouton paramètres (visible uniquement pour les canaux)
        mSettingsButton = new JButton("\u2699");
        mSettingsButton.setFont(Theme.FONT_TITLE);
        mSettingsButton.setForeground(Theme.TEXT_SECONDARY);
        mSettingsButton.setBackground(Theme.BACKGROUND);
        mSettingsButton.setBorderPainted(false);
        mSettingsButton.setFocusPainted(false);
        mSettingsButton.setContentAreaFilled(false);
        mSettingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mSettingsButton.setToolTipText("Paramètres du canal");
        mSettingsButton.setVisible(false);
        mHeaderPanel.add(mSettingsButton, BorderLayout.EAST);

        add(mHeaderPanel, BorderLayout.NORTH);

        // === Zone messages ===
        mMessagesPanel = new JPanel();
        mMessagesPanel.setLayout(new BoxLayout(mMessagesPanel, BoxLayout.Y_AXIS));
        mMessagesPanel.setBackground(Theme.BACKGROUND);
        mMessagesPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        mScrollPane = new JScrollPane(mMessagesPanel);
        mScrollPane.setBorder(null);
        mScrollPane.getViewport().setBackground(Theme.BACKGROUND);
        mScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(mScrollPane, BorderLayout.CENTER);

        // === Zone saisie ===
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Theme.BACKGROUND);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                new EmptyBorder(10, 15, 10, 15)));

        mInputField = new JTextArea(3, 0);
        mInputField.setFont(Theme.FONT_BODY);
        mInputField.setLineWrap(true);
        mInputField.setWrapStyleWord(true);
        mInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(6, 8, 6, 8)));
        JScrollPane inputScroll = new JScrollPane(mInputField);
        inputScroll.setBorder(null);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        mSendButton = new JButton("Envoyer");
        mSendButton.setFont(Theme.FONT_BUTTON);
        mSendButton.setBackground(Theme.ACCENT);
        mSendButton.setForeground(Color.WHITE);
        mSendButton.setFocusPainted(false);
        mSendButton.setOpaque(true);
        mSendButton.setBorderPainted(false);
        mSendButton.setBorder(new EmptyBorder(8, 18, 8, 18));
        mSendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel sendWrapper = new JPanel(new BorderLayout());
        sendWrapper.setBackground(Theme.BACKGROUND);
        sendWrapper.setBorder(new EmptyBorder(0, 10, 0, 0));
        sendWrapper.add(mSendButton, BorderLayout.SOUTH);
        inputPanel.add(sendWrapper, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Met à jour le titre de la conversation dans le header.
     */
    public void setHeader(String title) {
        mHeaderLabel.setText(title);
    }

    /**
     * Affiche la liste des messages de la conversation.
     */
    public void updateMessages(List<Message> messages, UUID connectedUserUuid) {
        mMessagesPanel.removeAll();
        if (messages.isEmpty()) {
            JLabel empty = new JLabel("Aucun message. Commencez la conversation !");
            empty.setFont(Theme.FONT_BODY);
            empty.setForeground(Theme.TEXT_LIGHT);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            mMessagesPanel.add(Box.createVerticalGlue());
            mMessagesPanel.add(empty);
            mMessagesPanel.add(Box.createVerticalGlue());
        } else {
            for (Message msg : messages) {
                boolean isMine = msg.getSender().getUuid().equals(connectedUserUuid);
                mMessagesPanel.add(buildMessageBubble(msg, isMine));
                mMessagesPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            }
        }
        mMessagesPanel.revalidate();
        mMessagesPanel.repaint();
        // Scroll automatique vers le bas après rendu
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = mScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    private JPanel buildMessageBubble(Message msg, boolean isMine) {
        final Color bgColor = isMine ? Theme.ACCENT : new Color(240, 240, 245);
        final int arc = 14;

        // Bulle avec coins arrondis
        JPanel bubble = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
                g2.dispose();
            }
        };
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setOpaque(false);
        bubble.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Nom (seulement si pas moi)
        if (!isMine) {
            JLabel senderLabel = new JLabel(msg.getSender().getName());
            senderLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
            senderLabel.setForeground(new Color(70, 80, 200));
            senderLabel.setAlignmentX(LEFT_ALIGNMENT);
            bubble.add(senderLabel);
        }

        // Texte + heure sur une ligne
        String time = new SimpleDateFormat("HH:mm").format(new Date(msg.getEmissionDate()));
        String textHtml = "<html><body style='width:220px'>"
                + escapeHtml(msg.getText())
                + " <span style='color:" + (isMine ? "#c8cdff" : "#aaa") + ";font-size:9px'>"
                + time + "</span></body></html>";
        JLabel textLabel = new JLabel(textHtml);
        textLabel.setFont(Theme.FONT_BODY);
        textLabel.setForeground(isMine ? Color.WHITE : Theme.TEXT_PRIMARY);
        textLabel.setAlignmentX(LEFT_ALIGNMENT);
        bubble.add(textLabel);

        // Contraindre la largeur max de la bulle
        bubble.setMaximumSize(new Dimension(320, Short.MAX_VALUE));

        // Ligne : aligne la bulle à droite ou à gauche
        JPanel row = new JPanel();
        row.setLayout(new FlowLayout(isMine ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        row.setBackground(Theme.BACKGROUND);
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));
        row.add(bubble);
        return row;
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\n", "<br>");
    }

    public String getMessageText() {
        return mInputField.getText().trim();
    }

    public void clearInput() {
        mInputField.setText("");
    }

    public void addSendListener(ActionListener listener) {
        mSendButton.addActionListener(listener);
    }

    public void addSettingsListener(ActionListener listener) {
        mSettingsButton.addActionListener(listener);
    }

    public void setSettingsVisible(boolean visible) {
        mSettingsButton.setVisible(visible);
    }

    public JTextArea getInputField() {
        return mInputField;
    }
}
