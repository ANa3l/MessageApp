package main.java.com.ubo.tp.message.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.ihm.channel.ChannelComponent;
import main.java.com.ubo.tp.message.ihm.profile.ProfileComponent;
import main.java.com.ubo.tp.message.ihm.user.UserComponent;

/**
 * Vue principale apres connexion.
 * Assemble les composants.
 */
public class HomeView extends JPanel {

    private JLabel mHeaderLabel;
    private JPanel mCenterContent;
    private JButton mBellButton;
    private JLabel mBellBadge;
    private final List<NotificationEntry> mNotifications = new ArrayList<>();
    private final List<ActionListener> mNotificationClickListeners = new ArrayList<>();

    /**
     * Structure pour une entree de notification.
     */
    public static class NotificationEntry {
        public final String title;
        public final String text;
        public final long timestamp;
        public final UUID senderUuid;
        public final UUID channelUuid;

        public NotificationEntry(String title, String text, UUID senderUuid, UUID channelUuid) {
            this.title = title;
            this.text = text;
            this.timestamp = System.currentTimeMillis();
            this.senderUuid = senderUuid;
            this.channelUuid = channelUuid;
        }
    }

    /**
     * Constructeur.
     */
    public HomeView(ProfileComponent profileComponent, UserComponent userComponent, ChannelComponent channelComponent) {
        initComponents(profileComponent, userComponent, channelComponent);
    }

    /**
     * Initialisation des composants.
     */
    private void initComponents(ProfileComponent profileComponent, UserComponent userComponent,
            ChannelComponent channelComponent) {
        setLayout(new BorderLayout());

        // === Header ===
        mHeaderLabel = new JLabel("Accueil");

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.setOpaque(false);

        // Bouton cloche notifications
        JPanel bellWrapper = new JPanel(new BorderLayout());
        bellWrapper.setOpaque(false);

        mBellButton = new JButton("\uD83D\uDD14");
        mBellButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        mBellButton.setPreferredSize(new Dimension(36, 32));
        mBellButton.setBorderPainted(false);
        mBellButton.setContentAreaFilled(false);
        mBellButton.setFocusPainted(false);
        mBellButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mBellButton.setToolTipText("Notifications");
        mBellButton.addActionListener(e -> showNotificationPopup());

        mBellBadge = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getText() != null && !getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(237, 66, 69));
                    int w = getWidth();
                    int h = 16;
                    int y = (getHeight() - h) / 2;
                    g2.fillRoundRect(0, y, w, h, h, h);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        mBellBadge.setFont(new Font("SansSerif", Font.BOLD, 9));
        mBellBadge.setForeground(Color.WHITE);
        mBellBadge.setHorizontalAlignment(JLabel.CENTER);
        mBellBadge.setPreferredSize(new Dimension(0, 0));
        mBellBadge.setVisible(false);

        bellWrapper.add(mBellButton, BorderLayout.CENTER);
        bellWrapper.add(mBellBadge, BorderLayout.EAST);

        rightPanel.add(bellWrapper);
        rightPanel.add(profileComponent.getView());
        add(Theme.createHeaderBar(mHeaderLabel, rightPanel), BorderLayout.NORTH);

        // === Sidebar gauche (onglets Utilisateurs + Canaux) ===
        JTabbedPane sidebarTabs = new JTabbedPane();
        sidebarTabs.setFont(Theme.FONT_SUBTITLE);
        sidebarTabs.setBackground(Theme.SIDEBAR);
        sidebarTabs.addTab("Utilisateurs", userComponent.getView());
        sidebarTabs.addTab("Canaux", channelComponent.getView());
        JPanel sidebarWrapper = new JPanel(new BorderLayout());
        sidebarWrapper.setPreferredSize(new Dimension(260, 0));
        sidebarWrapper.add(sidebarTabs, BorderLayout.CENTER);

        // === Zone centrale (contenu futur : messages, channels...) ===
        mCenterContent = Theme.createPlaceholderPanel("Selectionnez un utilisateur ou un canal");

        // === SplitPane : sidebar | contenu ===
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarWrapper, mCenterContent);
        splitPane.setDividerLocation(260);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Retourne le panneau central pour y ajouter du contenu.
     */
    public JPanel getCenterContent() {
        return mCenterContent;
    }

    /**
     * Remplace le contenu de la zone centrale.
     */
    public void setCenterContent(JPanel content) {
        mCenterContent.removeAll();
        mCenterContent.add(content, BorderLayout.CENTER);
        mCenterContent.revalidate();
        mCenterContent.repaint();
    }

    /**
     * Remet le contenu par defaut dans la zone centrale.
     */
    public void resetCenterContent() {
        mCenterContent.removeAll();
        mCenterContent.setLayout(new BorderLayout());
        JPanel placeholder = Theme.createPlaceholderPanel("Selectionnez un utilisateur ou un canal");
        mCenterContent.add(placeholder, BorderLayout.CENTER);
        mCenterContent.revalidate();
        mCenterContent.repaint();
    }

    // =============================================
    // API Notification Bell
    // =============================================

    /**
     * Ajoute une notification au centre de notifications.
     */
    public void addNotification(String title, String text, UUID senderUuid, UUID channelUuid) {
        mNotifications.add(0, new NotificationEntry(title, text, senderUuid, channelUuid));
        updateBellBadge();
    }

    /**
     * Listener appele quand l'utilisateur clique sur une notification.
     */
    public void addNotificationClickListener(ActionListener listener) {
        mNotificationClickListeners.add(listener);
    }

    /**
     * Retourne la notification a l'index donne.
     */
    public NotificationEntry getNotification(int index) {
        if (index >= 0 && index < mNotifications.size()) {
            return mNotifications.get(index);
        }
        return null;
    }

    private void updateBellBadge() {
        int count = mNotifications.size();
        if (count > 0) {
            String label = count > 99 ? "99+" : String.valueOf(count);
            mBellBadge.setText(label);
            mBellBadge.setPreferredSize(new Dimension(count > 99 ? 24 : 18, 16));
            mBellBadge.setVisible(true);
        } else {
            mBellBadge.setText("");
            mBellBadge.setVisible(false);
        }
        mBellButton.getParent().revalidate();
        mBellButton.getParent().repaint();
    }

    /**
     * Affiche le popup notifications.
     */
    private void showNotificationPopup() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(Color.WHITE);
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                new EmptyBorder(4, 0, 4, 0)));

        if (mNotifications.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
            JLabel emptyLabel = new JLabel("Aucune notification", JLabel.CENTER);
            emptyLabel.setFont(Theme.FONT_BODY);
            emptyLabel.setForeground(Theme.TEXT_LIGHT);
            emptyPanel.add(emptyLabel);
            popup.add(emptyPanel);
        } else {
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(Color.WHITE);
            headerPanel.setBorder(new EmptyBorder(8, 12, 8, 12));
            JLabel headerLabel = new JLabel("Notifications");
            headerLabel.setFont(Theme.FONT_TITLE);
            headerPanel.add(headerLabel, BorderLayout.WEST);

            JButton clearBtn = new JButton("Tout effacer");
            clearBtn.setFont(Theme.FONT_SMALL);
            clearBtn.setForeground(Theme.ACCENT);
            clearBtn.setBorderPainted(false);
            clearBtn.setContentAreaFilled(false);
            clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            clearBtn.addActionListener(ev -> {
                mNotifications.clear();
                updateBellBadge();
                popup.setVisible(false);
            });
            headerPanel.add(clearBtn, BorderLayout.EAST);
            popup.add(headerPanel);
            popup.addSeparator();

            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setBackground(Color.WHITE);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            int maxDisplay = Math.min(mNotifications.size(), 50);

            for (int i = 0; i < maxDisplay; i++) {
                final int index = i;
                NotificationEntry entry = mNotifications.get(i);

                JPanel item = new JPanel(new BorderLayout(8, 0));
                item.setBackground(Color.WHITE);
                item.setBorder(new EmptyBorder(8, 12, 8, 12));
                item.setMaximumSize(new Dimension(350, 60));
                item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                String icon = entry.channelUuid != null ? "#" : "\u2709";
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                iconLabel.setForeground(Theme.ACCENT);
                iconLabel.setPreferredSize(new Dimension(24, 24));
                item.add(iconLabel, BorderLayout.WEST);

                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setOpaque(false);

                JLabel titleLabel = new JLabel(entry.title);
                titleLabel.setFont(Theme.FONT_SUBTITLE);
                titleLabel.setForeground(Theme.TEXT_PRIMARY);
                textPanel.add(titleLabel);

                String preview = entry.text.length() > 60
                        ? entry.text.substring(0, 60) + "..."
                        : entry.text;
                JLabel previewLabel = new JLabel(preview);
                previewLabel.setFont(Theme.FONT_SMALL);
                previewLabel.setForeground(Theme.TEXT_SECONDARY);
                textPanel.add(previewLabel);

                item.add(textPanel, BorderLayout.CENTER);

                JLabel timeLabel = new JLabel(sdf.format(new Date(entry.timestamp)));
                timeLabel.setFont(Theme.FONT_SMALL);
                timeLabel.setForeground(Theme.TEXT_LIGHT);
                item.add(timeLabel, BorderLayout.EAST);

                item.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        item.setBackground(Theme.SELECTION);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        item.setBackground(Color.WHITE);
                    }

                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        popup.setVisible(false);
                        // 1. Navigation (lit getNotification(index) avant suppression)
                        for (ActionListener l : mNotificationClickListeners) {
                            l.actionPerformed(new java.awt.event.ActionEvent(
                                    HomeView.this, 0, String.valueOf(index)));
                        }
                        // 2. Supprimer la notification lue et mettre à jour le badge
                        if (index < mNotifications.size()) {
                            mNotifications.remove(index);
                            updateBellBadge();
                        }
                    }
                });

                listPanel.add(item);

                if (i < maxDisplay - 1) {
                    JPanel sep = new JPanel();
                    sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                    sep.setBackground(Theme.BORDER);
                    listPanel.add(sep);
                }
            }

            JScrollPane scroll = new JScrollPane(listPanel);
            scroll.setBorder(null);
            scroll.setPreferredSize(new Dimension(350, Math.min(maxDisplay * 55, 350)));
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            popup.add(scroll);
        }

        popup.show(mBellButton, -popup.getPreferredSize().width + mBellButton.getWidth(),
                mBellButton.getHeight());
    }
}