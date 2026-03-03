package main.java.com.ubo.tp.message.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Barre de menu de l'application.
 */
public class MessageAppMenuBar extends JMenuBar {
    
    private static final String ICONS_PATH = "C:\\Users\\abasy\\Documents\\Cursus informatique\\Master\\Master2_TIIL-A\\IHM_UI\\MessageApp\\src\\main\\resources\\images\\";
    
    private MessageApp mMessageApp;
    
    /**
     * Constructeur.
     * 
     * @param messageApp référence vers l'application principale
     */
    public MessageAppMenuBar(MessageApp messageApp) {
        this.mMessageApp = messageApp;
        initMenus();
    }
    
    /**
     * Initialisation des menus.
     */
    private void initMenus() {
        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        
        JMenuItem itemFolder = new JMenuItem("Choisir répertoire");
        itemFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mMessageApp.initDirectory();
            }
        });
        
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        ImageIcon exitIcon = new ImageIcon(ICONS_PATH + "exitIcon_20.png");
        itemQuitter.setIcon(exitIcon);
        itemQuitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mMessageApp.exitApplication();
            }
        });
        
        menuFichier.add(itemFolder);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);
        
        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mMessageApp.showAboutDialog();
            }
        });
        
        menuAide.add(itemAPropos);
        
        // Ajout des menus
        add(menuFichier);
        add(menuAide);
    }
}