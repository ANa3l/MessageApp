package main.java.com.ubo.tp.message.ihm.user;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;

/**
 * Composant utilisateur.
 * Assemble la vue et le contrôleur.
 * SRS-MAP-USR-007
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
    }

    /**
     * Retourne le panel graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}