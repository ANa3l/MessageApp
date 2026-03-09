package main.java.com.ubo.tp.message.ihm.user;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Contrôleur du composant utilisateur.
 * Écoute la base de données pour rafraîchir la liste.
 */
public class UserController implements IDatabaseObserver {

    private UserView mView;
    private DataManager mDataManager;

    /**
     * Constructeur.
     */
    public UserController(UserView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
        refreshUserList();
    }

    /**
     * Rafraîchit la liste des utilisateurs.
     */
    private void refreshUserList() {
        mView.updateUserList(mDataManager.getUsers());
    }

    // === IDatabaseObserver ===

    @Override
    public void notifyUserAdded(User addedUser) {
        refreshUserList();
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {
        refreshUserList();
    }

    @Override
    public void notifyUserModified(User modifiedUser) {
        refreshUserList();
    }

    @Override
    public void notifyMessageAdded(Message addedMessage) {
        // Pas utilisé
    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {
        // Pas utilisé
    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {
        // Pas utilisé
    }

    @Override
    public void notifyChannelAdded(Channel addedChannel) {
        // Pas utilisé
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {
        // Pas utilisé
    }

    @Override
    public void notifyChannelModified(Channel modifiedChannel) {
        // Pas utilisé
    }
}