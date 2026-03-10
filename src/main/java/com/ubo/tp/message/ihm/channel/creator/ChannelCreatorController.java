package main.java.com.ubo.tp.message.ihm.channel.creator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Controleur du formulaire de creation de canal.
 * Valide les donnees, cree le canal et notifie les observateurs.
 * SRS-MAP-CHN-003 (canal public), SRS-MAP-CHN-004 (canal prive)
 */
public class ChannelCreatorController {

    private final ChannelCreatorView mView;
    private final DataManager mDataManager;
    private final List<IChannelCreatorObserver> mObservers = new ArrayList<>();
    private User mConnectedUser;

    public ChannelCreatorController(ChannelCreatorView view, DataManager dataManager) {
        this.mView = view;
        this.mDataManager = dataManager;
    }

    public void setConnectedUser(User user) {
        this.mConnectedUser = user;
    }

    public void addObserver(IChannelCreatorObserver observer) {
        mObservers.add(observer);
    }

    /** Traite la soumission du formulaire. */
    public void handleCreate() {
        String name = mView.getChannelName().trim();

        if (name.isEmpty()) {
            mView.showError("Le nom du canal est obligatoire.");
            return;
        }

        // Unicite du nom (insensible a la casse)
        for (Channel existing : mDataManager.getChannels()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                mView.showError("Un canal avec ce nom existe deja.");
                return;
            }
        }

        // Creation du canal
        Channel newChannel;
        if (mView.isPrivate()) {
            // Canal prive : le createur est automatiquement membre (SRS-MAP-CHN-004)
            newChannel = new Channel(mConnectedUser, name, Arrays.asList(mConnectedUser));
        } else {
            // Canal public (SRS-MAP-CHN-003)
            newChannel = new Channel(mConnectedUser, name);
        }

        mDataManager.sendChannel(newChannel);
        mView.reset();

        for (IChannelCreatorObserver observer : mObservers) {
            observer.notifyChannelCreated(newChannel);
        }
    }

    /** Traite l'annulation. */
    public void handleCancel() {
        mView.reset();
        for (IChannelCreatorObserver observer : mObservers) {
            observer.notifyCreationCancelled();
        }
    }
}
