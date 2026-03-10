package main.java.com.ubo.tp.message.ihm.message;

/**
 * Interface pour observer les evenements du composant message.
 */
public interface IMessageObserver {

    /**
     * La conversation active a ete invalidee (canal supprime, etc.).
     */
    void notifyConversationClosed();
}
