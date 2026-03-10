package main.java.com.ubo.tp.message.ihm.channel.creator;

import main.java.com.ubo.tp.message.datamodel.Channel;

/**
 * Interface pour observer les evenements du composant de creation de canal.
 * SRS-MAP-CHN-003, SRS-MAP-CHN-004
 */
public interface IChannelCreatorObserver {

    /** Canal cree avec succes. */
    void notifyChannelCreated(Channel channel);

    /** L'utilisateur a annule la creation. */
    void notifyCreationCancelled();
}
