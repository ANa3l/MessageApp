package main.java.com.ubo.tp.message.ihm.profile.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant editeur de profil.
 * Assemble la vue et le controleur.
 * SRS-MAP-USR-009, SRS-MAP-USR-010
 */
public class ProfileEditorComponent {

    private ProfileEditorView mView;
    private ProfileEditorController mController;

    /**
     * Constructeur.
     */
    public ProfileEditorComponent(DataManager dataManager) {
        this.mView = new ProfileEditorView();
        this.mController = new ProfileEditorController(mView, dataManager);

        // Cablage : boutons -> controller
        mView.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleSave();
            }
        });

        mView.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleDelete();
            }
        });

        mView.getBackButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleBack();
            }
        });
    }

    /**
     * Met a jour le profil avec l'utilisateur connecte.
     */
    public void setConnectedUser(User user) {
        mController.setConnectedUser(user);
    }

    /**
     * Ajoute un observateur des actions de l'editeur.
     */
    public void addObserver(IProfileEditorObserver observer) {
        mController.addObserver(observer);
    }

    /**
     * Retourne la vue graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}
