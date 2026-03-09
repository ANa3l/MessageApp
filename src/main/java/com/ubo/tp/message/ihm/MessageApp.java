package main.java.com.ubo.tp.message.ihm;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import main.java.com.ubo.tp.message.core.DataManager;
import main.java.com.ubo.tp.message.core.database.IDatabase;
import main.java.com.ubo.tp.message.core.session.Session;

/**
 * Classe principale l'application.
 *
 * @author S.Lucas
 */
public class MessageApp extends JFrame {
    /**
     * Base de données.
     */
    protected DataManager mDataManager;

    /**
     * Base de données.
     */
    protected IDatabase mDatabase;

    /**
     * Vue principale de l'application.
     */
    protected MessageAppMainView mMainView;

    /**
     * Session de l'application.
     */
    protected Session mSession;
    
    /**
     * Constructeur.
     *
     * @param dataManager
     * @param database
     */
    public MessageApp(DataManager dataManager, IDatabase database) {
        super("MessageApp - Application de Messagerie");
        this.mDataManager = dataManager;
        this.mDatabase = database;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Lance l'affichage de l'IHM.
     */
    public void showGUI() {
        // Initialisation de l'application
        this.init();
        
        // Affichage dans l'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MessageApp.this.setVisible(true);
            }
        });
    }

    /**
     * Initialisation de l'application.
     */
    protected void init() {
        // Init du look and feel de l'application
        this.initLookAndFeel();

        // Initialisation de la session
        mSession = new Session();

        // Initialisation de l'IHM
        this.initGui();

        // Initialisation du répertoire d'échange
        this.initDirectory();
    }

    /**
     * Initialisation du look and feel de l'application.
     */
    protected void initLookAndFeel() {
        try {
            // Utilisation du look and feel Nimbus (moderne et agréable)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Fallback sur le look and feel du système si Nimbus n'est pas disponible
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialisation de l'interface graphique.
     */
    protected void initGui() {
    	
    	  // Icône de la fenêtre (dans la barre de titre)
        ImageIcon logo = new ImageIcon("C:\\Users\\abasy\\Documents\\Cursus informatique\\Master\\Master2_TIIL-A\\IHM_UI\\MessageApp\\src\\main\\resources\\images\\logo_20.png");
        setIconImage(logo.getImage());
        
        
     // Barre de menu (utilisation de la classe dédiée)
        setJMenuBar(new MessageAppMenuBar(this));

        // Création de la vue principale
        mMainView = new MessageAppMainView(mDataManager, mDatabase, mSession);
        setContentPane(this.mMainView);

        // Configuration de la fenêtre
        setSize(800, 600);
        setLocationRelativeTo(null); // Centrer la fenêtre
    }
   
    
    /**
     * Affiche la boîte de dialogue "À propos".
     */
    protected void showAboutDialog() {
    	 ImageIcon logo = new ImageIcon("C:\\Users\\abasy\\Documents\\Cursus informatique\\Master\\Master2_TIIL-A\\IHM_UI\\MessageApp\\src\\main\\resources\\images\\logo_50.png");
    	
        JOptionPane.showMessageDialog(
            this,
            "MessageApp\nVersion 1.0\nApplication de Messagerie Instantanée",
            "À propos",
            JOptionPane.INFORMATION_MESSAGE,
            logo
        );
    }

    /**
     * Initialisation du répertoire d'échange (depuis la conf ou depuis un file
     * chooser).
     * Le chemin doit obligatoirement avoir été saisi et être valide avant de
     * pouvoir utiliser l'application
     */
    protected void initDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionner un répertoir ");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            
            if (isValidExchangeDirectory(selectedDirectory)) {
                initDirectory(selectedDirectory.getAbsolutePath());
                JOptionPane.showMessageDialog(
                    this,
                    "Répertoire d'échange configuré : " + selectedDirectory.getAbsolutePath(),
                    "Configuration",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Le répertoire sélectionné n'est pas valide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                // Redemander la sélection
                initDirectory();
            }
        } else {
            // L'utilisateur a annulé → fermeture de l'application
            JOptionPane.showMessageDialog(
                    this,
                    "Vous devez sélectionner un répertoire d'échange pour utiliser l'application.",
                    "Configuration requise",
                    JOptionPane.WARNING_MESSAGE
            );
            System.exit(0);
        }
        
        
    }

    /**
     * Indique si le fichier donné est valide pour servir de répertoire d'échange
     *
     * @param directory , Répertoire à tester.
     */
    protected boolean isValidExchangeDirectory(File directory) {
        // Valide si répertoire disponible en lecture et écriture
        return directory != null && directory.exists() && directory.isDirectory() && directory.canRead()
                && directory.canWrite();
    }

    /**
     * Initialisation du répertoire d'échange.
     *
     * @param directoryPath
     */
    protected void initDirectory(String directoryPath) {
        mDataManager.setExchangeDirectory(directoryPath);
    }
    
    /**
     * Retourne la session.
     */
    public Session getSession() {
        return mSession;
    }

    /**
     * Quitte l'application.
     */
    public void exitApplication() {
        System.exit(0);
    }
}