package main.java.com.ubo.tp.message.ihm.register;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;

/**
 * Composant de création de compte.
 * Assemble la vue et le contrôleur.
 */
public class RegisterComponent {
	
	private RegisterView mView;
	private RegisterController mController;
	private List<IRegisterObserver> mObservers;
	
	/**
	 * Constructeur.
	 */
	public RegisterComponent(DataManager dataManager) {
		this.mObservers = new ArrayList<>();
		this.mView = new RegisterView();
		this.mController = new RegisterController(mView, dataManager, this);
	}
	
	/**
	 * Retourne le panel graphique du composant.
	 */
	public JPanel getView() {
		return mView;
	}
	
	// === Gestion des observateurs ===
	
	public void addObserver(IRegisterObserver observer) {
		this.mObservers.add(observer);
	}
	
	public void notifyAccountCreated() {
		for (IRegisterObserver observer : mObservers) {
			observer.notifyAccountCreated();
		}
	}
	
	public void notifyBackToLogin() {
		for (IRegisterObserver observer : mObservers) {
			observer.notifyBackToLogin();
		}
	}
}
