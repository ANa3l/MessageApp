package main.java.com.ubo.tp.message.ihm.logout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

/**
 * Composant de déconnexion.
 */
public class LogoutComponent {

    private JButton mLogoutButton;
    private List<ILogoutObserver> mObservers;

    /**
     * Constructeur.
     */
    public LogoutComponent() {
        this.mObservers = new ArrayList<>();
        this.mLogoutButton = new JButton("Se déconnecter");
        initListeners();
    }

    /**
     * Initialisation des listeners.
     */
    private void initListeners() {
        mLogoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyLogout();
            }
        });
    }

    /**
     * Retourne le bouton graphique du composant.
     */
    public JButton getView() {
        return mLogoutButton;
    }

    // === Gestion des observateurs ===

    public void addObserver(ILogoutObserver observer) {
        this.mObservers.add(observer);
    }

    private void notifyLogout() {
        for (ILogoutObserver observer : mObservers) {
            observer.notifyLogout();
        }
    }
}
