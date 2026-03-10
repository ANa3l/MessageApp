package main.java.com.ubo.tp.message.ihm.channel;

import main.java.com.ubo.tp.message.datamodel.Channel;

/**
 * Interface pour observer les événements du composant Channel.
 * SRS-MAP-CHN-001, SRS-MAP-CHN-002
 */
public interface IChannelObserver {

    /**
     * Notification de sélection d'un canal.
     */
    void notifyChannelSelected(Channel selectedChannel);
}
