package main.java.com.ubo.tp.message.ihm.message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue de la zone de saisie d'un message.
 * Champ de texte avec limite de caractères, compteur et bouton d'envoi.
 */
public class MessageInputView extends JPanel {

    private static final int MAX_CHARS = 200;

    private JTextArea mInputField;
    private JButton mSendButton;
    private JLabel mCharCountLabel;

    public MessageInputView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                new EmptyBorder(10, 15, 10, 15)));

        // --- Champ de saisie ---
        mInputField = new JTextArea(3, 0);
        mInputField.setFont(Theme.FONT_BODY);
        mInputField.setLineWrap(true);
        mInputField.setWrapStyleWord(true);
        mInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(6, 8, 6, 8)));

        mInputField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= MAX_CHARS) {
                    super.insertString(offs, str, a);
                }
            }
        });

        // --- Compteur de caractères ---
        mCharCountLabel = new JLabel("0/" + MAX_CHARS);
        mCharCountLabel.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 11));
        mCharCountLabel.setForeground(Theme.TEXT_LIGHT);
        mInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateCount(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateCount(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateCount(); }
            private void updateCount() {
                int len = mInputField.getText().length();
                mCharCountLabel.setText(len + "/" + MAX_CHARS);
                mCharCountLabel.setForeground(len >= MAX_CHARS ? new Color(200, 60, 60) : Theme.TEXT_LIGHT);
            }
        });

        JPanel inputFieldWrapper = new JPanel(new BorderLayout());
        inputFieldWrapper.setBackground(Theme.BACKGROUND);
        JScrollPane inputScroll = new JScrollPane(mInputField);
        inputScroll.setBorder(null);
        inputFieldWrapper.add(inputScroll, BorderLayout.CENTER);
        inputFieldWrapper.add(mCharCountLabel, BorderLayout.SOUTH);
        add(inputFieldWrapper, BorderLayout.CENTER);

        // --- Bouton Envoyer ---
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
        add(sendWrapper, BorderLayout.EAST);
    }

    // === API publique ===

    public String getMessageText() {
        return mInputField.getText().trim();
    }

    public void clearInput() {
        mInputField.setText("");
    }

    public void addSendListener(ActionListener listener) {
        mSendButton.addActionListener(listener);
    }

    public JTextArea getInputField() {
        return mInputField;
    }
}