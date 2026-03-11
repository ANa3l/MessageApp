package main.java.com.ubo.tp.message.ihm.channel;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant de detail d'un canal.
 * Assemble la vue et le controleur.
 * SRS-MAP-CHN-005, SRS-MAP-CHN-006, SRS-MAP-CHN-007, SRS-MAP-CHN-008
 */
public class ChannelDetailComponent {

    private final ChannelDetailView mView;
    private final ChannelDetailController mController;

    public ChannelDetailComponent(DataManager dataManager, IDatabase database) {
        this.mView = new ChannelDetailView();
        this.mController = new ChannelDetailController(mView, dataManager);
        database.addObserver(mController);

        mView.addDeleteListener(e -> mController.handleDelete());
        mView.addLeaveListener(e -> mController.handleLeave());
        mView.addAddMemberListener(e -> mController.handleAddMember());
        mView.addRemoveMemberListener(e -> mController.handleRemoveMember());
    }

    public void addCloseListener(java.awt.event.ActionListener listener) {
        mView.addCloseListener(listener);
    }

    public void setChannel(Channel channel, User connectedUser) {
        mController.setChannel(channel, connectedUser);
    }

    public void addObserver(IChannelDetailObserver observer) {
        mController.addObserver(observer);
    }

    public JPanel getView() {
        return mView;
    }
}
