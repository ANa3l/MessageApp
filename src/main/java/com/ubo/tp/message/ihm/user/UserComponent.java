package main.java.com.ubo.tp.message.ihm.user;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;

/**
 * Composant utilisateur.
 * Assemble la vue et le controleur.
 * SRS-MAP-USR-007, SRS-MAP-USR-008
 */
public class UserComponent {

    private UserView mView;
    private UserController mController;

    /**
     * Constructeur.
     */
    public UserComponent(DataManager dataManager, IDatabase database) {
        this.mView = new UserView();
        this.mController = new UserController(mView, dataManager);

        // Enregistrer le controller comme observateur de la DB
        database.addObserver(mController);

        // Cablage recherche : champ texte -> controller
        mView.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mController.setSearchFilter(mView.getSearchField().getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mController.setSearchFilter(mView.getSearchField().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mController.setSearchFilter(mView.getSearchField().getText());
            }
        });
    }

    /**
     * Retourne le panel graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}