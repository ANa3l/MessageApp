package main.java.com.ubo.tp.message.ihm.message;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.IMessageRecipient;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant messagerie.
 * Assemble la vue et le contrôleur.
 * Ctrl+Entrée pour envoyer un message.
 */
public class MessageComponent {

    private final MessageView mView;
    private final MessageController mController;

    public MessageComponent(DataManager dataManager, IDatabase database) {
        this.mView = new MessageView();
        this.mController = new MessageController(mView, dataManager);

        // Enregistrer le contrôleur comme observateur de la DB (nouveaux messages en temps réel)
        database.addObserver(mController);

        // Câblage bouton Envoyer
        mView.addSendListener(e -> mController.handleSend());

        // Ctrl+Entrée pour envoyer depuis le champ texte
        mView.getInputField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    mController.handleSend();
                }
            }
        });
    }

    /**
     * Ajoute un observateur sur les evenements de la conversation.
     */
    public void addObserver(IMessageObserver observer) {
        mController.addObserver(observer);
    }

    /**
     * Définit la conversation active.
     */
    public void setRecipient(IMessageRecipient recipient, User connectedUser) {
        mController.setRecipient(recipient, connectedUser);
    }

    /**
     * Ajoute un listener sur le bouton paramètres (⚙) du header.
     */
    public void addSettingsListener(java.awt.event.ActionListener listener) {
        mView.addSettingsListener(listener);
    }

    /**
     * Retourne le destinataire courant.
     */
    public IMessageRecipient getRecipient() {
        return mController.getRecipient();
    }

    /**
     * Retourne le panel graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}
