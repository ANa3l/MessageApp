package main.java.com.ubo.tp.message.ihm.user;

import java.awt.BorderLayout;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.java.com.ubo.tp.message.datamodel.User;

/**
 * Vue du composant utilisateur.
 * Affiche la liste des utilisateurs enregistrés.
 */
public class UserView extends JPanel {

    private JList<String> mUserList;
    private DefaultListModel<String> mListModel;

    /**
     * Constructeur.
     */
    public UserView() {
        initComponents();
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Utilisateurs enregistrés"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        mListModel = new DefaultListModel<>();
        mUserList = new JList<>(mListModel);
        mUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(mUserList);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Met à jour la liste des utilisateurs.
     */
    public void updateUserList(Set<User> users) {
        mListModel.clear();
        for (User user : users) {
            mListModel.addElement(user.getName() + " (@" + user.getUserTag() + ")");
        }
    }

    /**
     * Retourne l'index sélectionné.
     */
    public int getSelectedIndex() {
        return mUserList.getSelectedIndex();
    }
}