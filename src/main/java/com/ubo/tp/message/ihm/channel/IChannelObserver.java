package main.java.com.ubo.tp.message.ihm.channel;

import main.java.com.ubo.tp.message.datamodel.Channel;

/**
 * Interface pour observer les événements du composant Channel.
 * SRS-MAP-CHN-001, SRS-MAP-CHN-002, SRS-MAP-CHN-003, SRS-MAP-CHN-004
 */
public interface IChannelObserver {

    /** L'utilisateur a sélectionné un canal dans la liste. */
    void notifyChannelSelected(Channel selectedChannel);

    /** L'utilisateur souhaite créer un nouveau canal. */
    void notifyCreateChannelRequest();
}
