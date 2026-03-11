package main.java.com.ubo.tp.message.ihm.message;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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

    private static final String CARD_CONVERSATION = "conversation";
    private static final String CARD_SEARCH = "search";

    private JLabel mHeaderLabel;
    private JButton mSettingsButton;
    private JButton mSearchButton;
    private JPanel mMessagesPanel;
    private JScrollPane mScrollPane;
    private Consumer<Message> mDeleteListener;

    private CardLayout mCardLayout;
    private JPanel mCardPanel;
    private MessageSearchView mSearchView;
    private MessageInputView mInputView;
    private Map<Long, JPanel> mMessageRowMap = new HashMap<>();

    public MessageView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        mCardLayout = new CardLayout();
        mCardPanel = new JPanel(mCardLayout);

        // === CARD 1 : Vue conversation ===
        JPanel conversationPanel = new JPanel(new BorderLayout());
        conversationPanel.setBackground(Theme.BACKGROUND);

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Theme.BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(10, 15, 10, 15)));
        mHeaderLabel = new JLabel("Sélectionnez un contact");
        mHeaderLabel.setFont(Theme.FONT_TITLE);
        mHeaderLabel.setForeground(Theme.TEXT_PRIMARY);
        headerPanel.add(mHeaderLabel, BorderLayout.CENTER);

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

        mSearchButton = new JButton("\uD83D\uDD0D");
        mSearchButton.setFont(Theme.FONT_TITLE);
        mSearchButton.setForeground(Theme.TEXT_SECONDARY);
        mSearchButton.setBackground(Theme.BACKGROUND);
        mSearchButton.setBorderPainted(false);
        mSearchButton.setFocusPainted(false);
        mSearchButton.setContentAreaFilled(false);
        mSearchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mSearchButton.setToolTipText("Rechercher");

        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        headerButtons.setBackground(Theme.BACKGROUND);
        headerButtons.add(mSearchButton);
        headerButtons.add(mSettingsButton);
        headerPanel.add(headerButtons, BorderLayout.EAST);
        conversationPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Zone messages ---
        mMessagesPanel = new JPanel();
        mMessagesPanel.setLayout(new BoxLayout(mMessagesPanel, BoxLayout.Y_AXIS));
        mMessagesPanel.setBackground(Theme.BACKGROUND);
        mMessagesPanel.setBorder(new EmptyBorder(12, 15, 12, 15));

        mScrollPane = new JScrollPane(mMessagesPanel);
        mScrollPane.setBorder(null);
        mScrollPane.getViewport().setBackground(Theme.BACKGROUND);
        mScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        conversationPanel.add(mScrollPane, BorderLayout.CENTER);

        // --- Zone saisie (vue séparée) ---
        mInputView = new MessageInputView();
        conversationPanel.add(mInputView, BorderLayout.SOUTH);

        // === CARD 2 : Mode recherche (vue séparée) ===
        mSearchView = new MessageSearchView(this::exitSearchMode);
        mSearchView.setOnResultClick(this::scrollToMessage);

        mCardPanel.add(conversationPanel, CARD_CONVERSATION);
        mCardPanel.add(mSearchView, CARD_SEARCH);
        add(mCardPanel, BorderLayout.CENTER);
    }

    // === Gestion du mode recherche (délégation) ===

    public void enterSearchMode() {
        mSearchView.getSearchField().setText("");
        mSearchView.showPlaceholder();
        mCardLayout.show(mCardPanel, CARD_SEARCH);
        SwingUtilities.invokeLater(() -> mSearchView.getSearchField().requestFocusInWindow());
    }

    public void exitSearchMode() {
        mSearchView.getSearchField().setText("");
        mCardLayout.show(mCardPanel, CARD_CONVERSATION);
    }

    public void updateSearchResults(List<Message> results, String query) {
        mSearchView.updateSearchResults(results, query);
    }

    // === Méthodes publiques ===

    public void setHeader(String title) {
        mHeaderLabel.setText(title);
    }

    public void setRecipientName(String name) {
        mSearchView.setRecipientName(name);
    }

    public void setDeleteListener(Consumer<Message> listener) {
        mDeleteListener = listener;
    }

    public void updateMessages(List<Message> messages, UUID connectedUserUuid) {
        mMessagesPanel.removeAll();
        mMessageRowMap.clear();
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
                JPanel row = buildMessageBubble(msg, isMine);
                mMessageRowMap.put(msg.getEmissionDate(), row);
                mMessagesPanel.add(row);
                mMessagesPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            }
        }
        mMessagesPanel.revalidate();
        mMessagesPanel.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = mScrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    public void scrollToMessage(Message message) {
        mCardLayout.show(mCardPanel, CARD_CONVERSATION);
        JPanel targetRow = mMessageRowMap.get(message.getEmissionDate());
        if (targetRow == null) return;
        SwingUtilities.invokeLater(() -> SwingUtilities.invokeLater(() -> {
            targetRow.scrollRectToVisible(targetRow.getBounds());
            javax.swing.border.Border originalBorder = targetRow.getBorder();
            targetRow.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Theme.ACCENT, 2, true),
                    new EmptyBorder(1, 1, 1, 1)));
            Timer timer = new Timer(1500, e -> {
                targetRow.setBorder(originalBorder);
                targetRow.repaint();
            });
            timer.setRepeats(false);
            timer.start();
        }));
    }

    private JPanel buildMessageBubble(Message msg, boolean isMine) {
        final Color bgColor = isMine ? Theme.ACCENT : new Color(240, 240, 245);
        final int arc = 14;

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

        if (!isMine) {
            JLabel senderLabel = new JLabel(msg.getSender().getName());
            senderLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
            senderLabel.setForeground(new Color(70, 80, 200));
            senderLabel.setAlignmentX(LEFT_ALIGNMENT);
            bubble.add(senderLabel);
        }

        String time = new SimpleDateFormat("HH:mm").format(new Date(msg.getEmissionDate()));
        String textHtml = "<html><body style='width:220px'>"
                + formatMessageText(msg.getText())
                + " <span style='color:" + (isMine ? "#c8cdff" : "#aaa") + ";font-size:9px'>"
                + time + "</span></body></html>";
        JLabel textLabel = new JLabel(textHtml);
        textLabel.setFont(Theme.FONT_BODY);
        textLabel.setForeground(isMine ? Color.WHITE : Theme.TEXT_PRIMARY);
        textLabel.setAlignmentX(LEFT_ALIGNMENT);
        bubble.add(textLabel);

        bubble.setMaximumSize(new Dimension(320, Short.MAX_VALUE));

        JPanel row = new JPanel();
        row.setLayout(new FlowLayout(isMine ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        row.setBackground(Theme.BACKGROUND);
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Short.MAX_VALUE));
        row.add(bubble);

        if (isMine) {
            JLabel deleteBtn = new JLabel("\u2715");
            deleteBtn.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
            deleteBtn.setForeground(Theme.TEXT_LIGHT);
            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            deleteBtn.setToolTipText("Supprimer");
            deleteBtn.setBorder(new EmptyBorder(0, 4, 0, 0));
            deleteBtn.setVisible(false);
            deleteBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (mDeleteListener != null) {
                        mDeleteListener.accept(msg);
                    }
                }
            });
            row.add(deleteBtn);
            row.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { deleteBtn.setVisible(true); }
                @Override
                public void mouseExited(MouseEvent e) { deleteBtn.setVisible(false); }
            });
        }

        return row;
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\n", "<br>");
    }

    private String formatMessageText(String text) {
        String escaped = escapeHtml(text);
        return escaped.replaceAll("@(\\w+)", "<b style='color:#4650c8'>@$1</b>");
    }

    public String getMessageText() {
        return mInputView.getMessageText();
    }

    public void clearInput() {
        mInputView.clearInput();
    }

    public void addSendListener(ActionListener listener) {
        mInputView.addSendListener(listener);
    }

    public void addSettingsListener(ActionListener listener) {
        mSettingsButton.addActionListener(listener);
    }

    public void addSearchListener(ActionListener listener) {
        mSearchButton.addActionListener(listener);
    }

    public void setSettingsVisible(boolean visible) {
        mSettingsButton.setVisible(visible);
    }

    public JTextArea getInputField() {
        return mInputView.getInputField();
    }

    public JTextField getSearchField() {
        return mSearchView.getSearchField();
    }
}