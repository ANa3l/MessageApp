package main.java.com.ubo.tp.message.ihm.register;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;

/**
 * Composant de creation de compte.
 * Assemble la vue et le controleur.
 */
public class RegisterComponent {

    private RegisterView mView;
    private RegisterController mController;

    /**
     * Constructeur.
     */
    public RegisterComponent(DataManager dataManager) {
        this.mView = new RegisterView();
        this.mController = new RegisterController(mView, dataManager);

        // Cablage : boutons -> controller
        mView.addCreateListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleCreateAccount();
            }
        });

        mView.addBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mController.handleBackToLogin();
            }
        });
    }

    /**
     * Ajoute un observateur des actions d'inscription.
     */
    public void addObserver(IRegisterObserver observer) {
        mController.addObserver(observer);
    }

    /**
     * Retourne la vue graphique du composant.
     */
    public JPanel getView() {
        return mView;
    }
}
