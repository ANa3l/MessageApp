package main.java.com.ubo.tp.message.ihm.message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.IMessageRecipient;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Composant messagerie.
 * Assemble la vue et le contrôleur.
 */
public class MessageComponent {

    private final MessageView mView;
    private final MessageController mController;
    private JPopupMenu mMentionPopup;
    private JList<String> mMentionList;
    private DefaultListModel<String> mMentionModel;

    public MessageComponent(DataManager dataManager, IDatabase database) {
        this.mView = new MessageView();
        this.mController = new MessageController(mView, dataManager);

        // Enregistrer le contrôleur comme observateur de la DB
        database.addObserver(mController);

        // Câblage bouton Envoyer
        mView.addSendListener(e -> mController.handleSend());

        // Câblage suppression
        mView.setDeleteListener(msg -> mController.handleDeleteMessage(msg));

        // Câblage recherche WhatsApp-style
        mView.addSearchListener(e -> mView.enterSearchMode());

        JTextField searchField = mView.getSearchField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { onSearchChanged(); }
            @Override
            public void removeUpdate(DocumentEvent e) { onSearchChanged(); }
            @Override
            public void changedUpdate(DocumentEvent e) { onSearchChanged(); }
            private void onSearchChanged() {
                String query = searchField.getText().trim();
                if (query.isEmpty()) {
                    mView.updateSearchResults(java.util.Collections.emptyList(), "");
                } else {
                    java.util.List<main.java.com.ubo.tp.message.datamodel.Message> results =
                            mController.searchMessages(query);
                    mView.updateSearchResults(results, query);
                }
            }
        });

        // Ctrl+Entrée pour envoyer depuis le champ texte
        JTextArea inputField = mView.getInputField();
        initMentionPopup();

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    mController.handleSend();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleMentionDetection(inputField);
            }
        });
    }

    /**
     * Initialise le popup d'autocomplétion des @mentions.
     */
    private void initMentionPopup() {
        mMentionModel = new DefaultListModel<>();
        mMentionList = new JList<>(mMentionModel);
        mMentionList.setFont(Theme.FONT_BODY);
        mMentionList.setFixedCellHeight(24);
        mMentionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                insertMention(mView.getInputField());
            }
        });
        mMentionPopup = new JPopupMenu();
        mMentionPopup.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        JScrollPane scroll = new JScrollPane(mMentionList);
        scroll.setPreferredSize(new Dimension(200, 120));
        scroll.setBorder(null);
        mMentionPopup.add(scroll);
    }

    /**
     * Détecte si l'utilisateur est en train de taper un @tag et affiche l'autocomplétion.
     */
    private void handleMentionDetection(JTextArea inputField) {
        String text = inputField.getText();
        int caretPos = inputField.getCaretPosition();
        // Chercher le @ le plus proche avant le curseur
        int atIndex = -1;
        for (int i = caretPos - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (c == '@') { atIndex = i; break; }
            if (c == ' ' || c == '\n') break;
        }
        if (atIndex < 0) {
            mMentionPopup.setVisible(false);
            return;
        }
        String prefix = text.substring(atIndex + 1, caretPos).toLowerCase();
        List<User> users = mController.getMentionableUsers();
        mMentionModel.clear();
        for (User u : users) {
            if (u.getUserTag().toLowerCase().startsWith(prefix)) {
                mMentionModel.addElement("@" + u.getUserTag() + "  (" + u.getName() + ")");
            }
        }
        if (mMentionModel.isEmpty()) {
            mMentionPopup.setVisible(false);
            return;
        }
        mMentionList.setSelectedIndex(0);
        try {
            // Position du popup au-dessus du champ de saisie
            Point p = inputField.getLocationOnScreen();
            mMentionPopup.show(inputField, 0, -Math.min(mMentionModel.size(), 5) * 24 - 10);
        } catch (Exception ex) {
            // ignore si le component n'est pas encore visible
        }
    }

    /**
     * Insère le @tag sélectionné dans le champ de saisie.
     */
    private void insertMention(JTextArea inputField) {
        String selected = mMentionList.getSelectedValue();
        if (selected == null) return;
        // Extraire le @tag (avant les espaces)
        String tag = selected.split("\\s")[0];
        String text = inputField.getText();
        int caretPos = inputField.getCaretPosition();
        int atIndex = text.lastIndexOf('@', caretPos - 1);
        if (atIndex >= 0) {
            String newText = text.substring(0, atIndex) + tag + " " + text.substring(caretPos);
            inputField.setText(newText);
            inputField.setCaretPosition(atIndex + tag.length() + 1);
        }
        mMentionPopup.setVisible(false);
    }

    public void addObserver(IMessageObserver observer) {
        mController.addObserver(observer);
    }

    public void setConnectedUser(User connectedUser) {
        mController.setConnectedUser(connectedUser);
    }

    public void setRecipient(IMessageRecipient recipient, User connectedUser) {
        mController.setRecipient(recipient, connectedUser);
    }

    public void addSettingsListener(java.awt.event.ActionListener listener) {
        mView.addSettingsListener(listener);
    }

    public IMessageRecipient getRecipient() {
        return mController.getRecipient();
    }

    public JPanel getView() {
        return mView;
    }
}
