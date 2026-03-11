package main.java.com.ubo.tp.message.ihm.channel;

/**
 * Interface pour observer les evenements du detail d'un canal.
 * SRS-MAP-CHN-005, SRS-MAP-CHN-006
 */
public interface IChannelDetailObserver {

    /** Le canal a ete supprime (proprietaire). */
    void notifyChannelDeleted();

    /** L'utilisateur a quitte le canal. */
    void notifyChannelLeft();
}
