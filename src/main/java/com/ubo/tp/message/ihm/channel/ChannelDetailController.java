package main.java.com.ubo.tp.message.ihm.channel;

import java.util.ArrayList;
import java.util.List;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabaseObserver;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Controleur du detail d'un canal.
 * Gere suppression (CHN-006), depart (CHN-005),
 * ajout membre (CHN-007), retrait membre (CHN-008).
 */
public class ChannelDetailController  implements IDatabaseObserver{

    private final ChannelDetailView mView;
    private final DataManager mDataManager;
    private final List<IChannelDetailObserver> mObservers = new ArrayList<>();

    private Channel mChannel;
    private User mConnectedUser;

    public ChannelDetailController(ChannelDetailView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
    }

    public void setChannel(Channel channel, User connectedUser) {
        this.mChannel = channel;
        this.mConnectedUser = connectedUser;
        boolean isCreator = channel.getCreator().getUuid().equals(connectedUser.getUuid());
        mView.setChannel(channel, isCreator, mConnectedUser.getUuid());
        if (isCreator && channel.isPrivate()) {
            refreshAddableMemberList();
        }
    }

    public void addObserver(IChannelDetailObserver observer) {
        mObservers.add(observer);
    }

    /** CHN-006 : Supprimer le canal (createur seulement). */
    public void handleDelete() {
        mDataManager.deleteChannel(mChannel);
        for (IChannelDetailObserver o : mObservers) {
            o.notifyChannelDeleted();
        }
    }

    /** CHN-005 : Quitter le canal (membre non-createur). */
    public void handleLeave() {
        mChannel.removeUser(mConnectedUser);
        mDataManager.sendChannel(mChannel);
        for (IChannelDetailObserver o : mObservers) {
            o.notifyChannelLeft();
        }
    }

    /** CHN-007 : Ajouter un membre. */
    public void handleAddMember() {
        User toAdd = mView.getSelectedAddableUser();
        if (toAdd == null)
            return;
        mChannel.addUser(toAdd);
        mDataManager.sendChannel(mChannel);
        mView.setChannel(mChannel, true, mConnectedUser.getUuid());
        refreshAddableMemberList();
    }

    /** CHN-008 : Retirer un membre. */
    public void handleRemoveMember() {
        User toRemove = mView.getSelectedMember();
        if (toRemove == null)
            return;
        if (toRemove.getUuid().equals(mConnectedUser.getUuid()))
            return;
        mChannel.removeUser(toRemove);
        mDataManager.sendChannel(mChannel);
        mView.setChannel(mChannel, true, mConnectedUser.getUuid());
        refreshAddableMemberList();
    }

    private void refreshAddableMemberList() {
        List<User> currentMembers = mChannel.getUsers();
        List<User> addable = new ArrayList<>();
        for (User u : mDataManager.getUsers()) {
            if (!containsUuid(currentMembers, u)
                    && !u.getUuid().equals(main.java.com.ubo.tp.message.common.Constants.UNKNONWN_USER_UUID)) {
                addable.add(u);
            }
        }
        mView.setAddableMemberUsers(addable);
    }

    private boolean containsUuid(List<User> users, User user) {
        for (User u : users) {
            if (u.getUuid().equals(user.getUuid()))
                return true;
        }
        return false;
    }

        @Override
    public void notifyChannelModified(Channel modifiedChannel) {
        if (mChannel != null && mChannel.getUuid().equals(modifiedChannel.getUuid())) {
            mChannel = modifiedChannel;
            boolean isCreator = mChannel.getCreator().getUuid().equals(mConnectedUser.getUuid());
            mView.setChannel(mChannel, isCreator, mConnectedUser.getUuid());
            if (isCreator && mChannel.isPrivate()) {
                refreshAddableMemberList();
            }
        }
    }

    @Override public void notifyChannelAdded(Channel c) {}
    @Override public void notifyChannelDeleted(Channel c) {}
    @Override public void notifyUserAdded(main.java.com.ubo.tp.message.datamodel.User u) {}
    @Override public void notifyUserDeleted(main.java.com.ubo.tp.message.datamodel.User u) {}
    @Override public void notifyUserModified(main.java.com.ubo.tp.message.datamodel.User u) {}
    @Override public void notifyMessageAdded(main.java.com.ubo.tp.message.datamodel.Message m) {}
    @Override public void notifyMessageDeleted(main.java.com.ubo.tp.message.datamodel.Message m) {}
    @Override public void notifyMessageModified(main.java.com.ubo.tp.message.datamodel.Message m) {}
    @Override public void notifyUserOnline(java.util.UUID uuid) {}
    @Override public void notifyUserOffline(java.util.UUID uuid) {}
}
