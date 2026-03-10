package main.java.com.ubo.tp.message.ihm.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Contrôleur du composant canal.
 * Écoute la base de données pour rafraîchir la liste des canaux.
 * Gère le filtrage par recherche (SRS-MAP-CHN-002) et la visibilité :
 *   - canaux publics : visibles par tous
 *   - canaux privés  : visibles uniquement par le créateur et les membres
 * SRS-MAP-CHN-001, SRS-MAP-CHN-002
 */
public class ChannelController implements IDatabaseObserver {

    private final ChannelView mView;
    private final DataManager mDataManager;
    private final List<IChannelObserver> mObservers = new ArrayList<>();

    private String mSearchFilter = "";
    private User mConnectedUser;

    public ChannelController(ChannelView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
        refreshChannelList();
    }

    // ===================================================================
    // API publique
    // ===================================================================

    public void setSearchFilter(String filter) {
        this.mSearchFilter = filter.trim().toLowerCase();
        refreshChannelList();
    }

    public void setConnectedUser(User user) {
        this.mConnectedUser = user;
        refreshChannelList();
    }

    public void addObserver(IChannelObserver observer) {
        mObservers.add(observer);
    }

    /**
     * Notifie les observateurs qu'un canal a été sélectionné.
     */
    public void handleChannelSelected(Channel channel) {
        for (IChannelObserver observer : mObservers) {
            observer.notifyChannelSelected(channel);
        }
    }

    /**
     * Notifie les observateurs que l'utilisateur veut créer un canal.
     */
    public void handleCreateChannelRequest() {
        for (IChannelObserver observer : mObservers) {
            observer.notifyCreateChannelRequest();
        }
    }

    // ===================================================================
    // Logique interne
    // ===================================================================

    private void refreshChannelList() {
        Set<Channel> allChannels = mDataManager.getChannels();
        List<Channel> filtered = new ArrayList<>();

        for (Channel channel : allChannels) {
            if (isVisible(channel) && matchesFilter(channel)) {
                filtered.add(channel);
            }
        }

        // Trier par nom
        filtered.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        mView.updateChannelList(filtered);
    }

    /**
     * Détermine si le canal est visible pour l'utilisateur connecté.
     * - Canal public : toujours visible.
     * - Canal privé  : visible uniquement si l'utilisateur est créateur ou membre.
     */
    private boolean isVisible(Channel channel) {
        if (!channel.isPrivate()) {
            return true;
        }
        if (mConnectedUser == null) {
            return false;
        }
        boolean isCreator = channel.getCreator().getUuid().equals(mConnectedUser.getUuid());
        boolean isMember = channel.getUsers().contains(mConnectedUser);
        return isCreator || isMember;
    }

    private boolean matchesFilter(Channel channel) {
        if (mSearchFilter.isEmpty()) {
            return true;
        }
        return channel.getName().toLowerCase().contains(mSearchFilter);
    }

    // ===================================================================
    // IDatabaseObserver
    // ===================================================================

    @Override
    public void notifyChannelAdded(Channel addedChannel) {
        refreshChannelList();
    }

    @Override
    public void notifyChannelDeleted(Channel deletedChannel) {
        refreshChannelList();
    }

    @Override
    public void notifyChannelModified(Channel modifiedChannel) {
        refreshChannelList();
    }

    @Override
    public void notifyUserAdded(User addedUser) {
    }

    @Override
    public void notifyUserDeleted(User deletedUser) {
    }

    @Override
    public void notifyUserModified(User modifiedUser) {
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
}
