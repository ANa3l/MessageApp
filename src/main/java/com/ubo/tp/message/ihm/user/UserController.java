package main.java.com.ubo.tp.message.ihm.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.com.ubo.tp.message.common.Constants;
import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Controleur du composant utilisateur.
 * Ecoute la base de donnees pour rafraichir la liste.
 * Gere le filtrage par recherche (SRS-MAP-USR-008).
 */
public class UserController implements IDatabaseObserver {

    private UserView mView;
    private DataManager mDataManager;
    private String mSearchFilter = "";
    private User mConnectedUser;

    /**
     * Constructeur.
     */
    public UserController(UserView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
        refreshUserList();
    }

    /**
     * Met a jour le filtre de recherche et rafraichit la liste.
     */
    public void setSearchFilter(String filter) {
        this.mSearchFilter = filter.trim().toLowerCase();
        refreshUserList();
    }

    /**
     * Definit l'utilisateur connecte (sera exclu de la liste).
     */
    public void setConnectedUser(User user) {
        this.mConnectedUser = user;
        refreshUserList();
    }

    /**
     * Rafraichit la liste des utilisateurs en appliquant le filtre.
     */
    private void refreshUserList() {
        Set<User> allUsers = new HashSet<>(mDataManager.getUsers());
        allUsers.removeIf(u -> u.getUuid().equals(Constants.UNKNONWN_USER_UUID));
        if (mConnectedUser != null) {
            allUsers.removeIf(u -> u.getUuid().equals(mConnectedUser.getUuid()));
        }

        List<User> filtered = new ArrayList<>();
        for (User user : allUsers) {
            if (matchesFilter(user)) {
                filtered.add(user);
            }
        }

        mView.updateUserList(filtered);
    }

    /**
     * Verifie si un utilisateur correspond au filtre de recherche.
     */
    private boolean matchesFilter(User user) {
        if (mSearchFilter.isEmpty()) {
            return true;
        }
        String name = user.getName().toLowerCase();
        String tag = user.getUserTag().toLowerCase();
        return name.contains(mSearchFilter) || tag.contains(mSearchFilter);
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
    }

    @Override
    public void notifyMessageDeleted(Message deletedMessage) {
    }

    @Override
    public void notifyMessageModified(Message modifiedMessage) {
    }

    @Override
    public void notifyChannelAdded(Channel addedChannel) {
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {
    }

    @Override
    public void notifyChannelModified(Channel modifiedChannel) {
    }
}