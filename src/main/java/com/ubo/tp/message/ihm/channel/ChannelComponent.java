package main.java.com.ubo.tp.message.ihm.channel;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant canal.
 * Assemble la vue et le contrôleur.
 * SRS-MAP-CHN-001, SRS-MAP-CHN-002
 */
public class ChannelComponent {

    private final ChannelView mView;
    private final ChannelController mController;

    public ChannelComponent(DataManager dataManager, IDatabase database) {
        this.mView = new ChannelView();
        this.mController = new ChannelController(mView, dataManager);

        // Enregistrer le contrôleur comme observateur de la DB
        database.addObserver(mController);

        // Câblage recherche : champ texte -> contrôleur
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

        // Câblage bouton "+ Créer" -> contrôleur
        mView.addCreateChannelListener(e -> mController.handleCreateChannelRequest());

        // Câblage sélection : clic sur un canal -> contrôleur
        mView.getChannelList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Channel selected = mView.getSelectedChannel();
                if (selected != null) {
                    mController.handleChannelSelected(selected);
                }
            }
        });
    }

    /**
     * Définit l'utilisateur connecté (pour filtrer les canaux privés).
     */
    public void setConnectedUser(User user) {
        mController.setConnectedUser(user);
    }

    /**
     * Ajoute un observateur sur les événements canal.
     */
    public void addObserver(IChannelObserver observer) {
        mController.addObserver(observer);
    }

    /**
     * Retourne le panel graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}
