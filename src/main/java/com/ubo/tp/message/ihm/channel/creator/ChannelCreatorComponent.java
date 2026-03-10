package main.java.com.ubo.tp.message.ihm.channel.creator;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant de creation de canal.
 * Assemble la vue et le controleur.
 * SRS-MAP-CHN-003, SRS-MAP-CHN-004
 */
public class ChannelCreatorComponent {

    private final ChannelCreatorView mView;
    private final ChannelCreatorController mController;

    public ChannelCreatorComponent(DataManager dataManager) {
        this.mView = new ChannelCreatorView();
        this.mController = new ChannelCreatorController(mView, dataManager);

        mView.addCreateListener(e -> mController.handleCreate());
        mView.addCancelListener(e -> mController.handleCancel());
    }

    public void setConnectedUser(User user) {
        mController.setConnectedUser(user);
    }

    public void addObserver(IChannelCreatorObserver observer) {
        mController.addObserver(observer);
    }

    public JPanel getView() {
        return mView;
    }
}
