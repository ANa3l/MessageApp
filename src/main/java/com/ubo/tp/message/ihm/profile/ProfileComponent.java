package main.java.com.ubo.tp.message.ihm.profile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant profil utilisateur connecte.
 * Assemble la vue et le controleur.
 * Affiche un avatar cliquable avec menu contextuel (Profil, Deconnexion).
 */
public class ProfileComponent {

    private ProfileView mView;
    private ProfileController mController;

    /**
     * Constructeur.
     */
    public ProfileComponent() {
        this.mView = new ProfileView();
        this.mController = new ProfileController();

        // Cablage : clic sur avatar -> popup
        mView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mView.showPopup();
            }
        });

        // Cablage : items du menu -> controller
        mView.getLogoutItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleLogout();
            }
        });

        mView.getProfileItem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleProfile();
            }
        });
    }

    /**
     * Met a jour le profil avec l'utilisateur connecte.
     */
    public void setConnectedUser(User user) {
        mView.setUser(user.getName(), user.getUserTag());
    }

    /**
     * Ajoute un observateur des actions profil.
     */
    public void addObserver(IProfileObserver observer) {
        mController.addObserver(observer);
    }

    /**
     * Retourne la vue graphique du composant.
     */
    public ProfileView getView() {
        return mView;
    }
}
