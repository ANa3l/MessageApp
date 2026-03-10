package main.java.com.ubo.tp.message.ihm.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;

/**
 * Composant de connexion.
 * Assemble la vue et le controleur.
 */
public class LoginComponent {

    private LoginView mView;
    private LoginController mController;

    /**
     * Constructeur.
     */
    public LoginComponent(DataManager dataManager) {
        this.mView = new LoginView();
        this.mController = new LoginController(mView, dataManager);

        // Cablage : boutons -> controller
        mView.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleLogin();
            }
        });

        mView.addCreateAccountListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleRegisterRequest();
            }
        });
    }

    /**
     * Ajoute un observateur des actions de login.
     */
    public void addObserver(ILoginObserver observer) {
        mController.addObserver(observer);
    }

    /**
     * Retourne la vue graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}
