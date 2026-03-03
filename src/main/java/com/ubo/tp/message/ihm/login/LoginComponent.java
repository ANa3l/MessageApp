package main.java.com.ubo.tp.message.ihm.login;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Composant de connexion.
 * Assemble la vue et le contrôleur.
 */
public class LoginComponent {
	
	private LoginView mView;
	private LoginController mController;
	private List<ILoginObserver> mObservers;
	
	/**
	 * Constructeur.
	 */
	public LoginComponent(DataManager dataManager) {
		this.mObservers = new ArrayList<>();
		this.mView = new LoginView();
		this.mController = new LoginController(mView, dataManager, this);
	}
	
	/**
	 * Retourne le panel graphique du composant.
	 */
	public JPanel getView() {
		return mView;
	}
	
	// === Gestion des observateurs ===
	
	public void addObserver(ILoginObserver observer) {
		this.mObservers.add(observer);
	}
	
	public void notifyLogin(User user) {
		for (ILoginObserver observer : mObservers) {
			observer.notifyLogin(user);
		}
	}
	
	public void notifyRegisterRequest() {
		for (ILoginObserver observer : mObservers) {
			observer.notifyRegisterRequest();
		}
	}
}
