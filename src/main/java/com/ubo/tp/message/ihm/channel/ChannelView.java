package main.java.com.ubo.tp.message.ihm.channel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue du composant canal.
 * Affiche la liste des canaux accessibles avec recherche.
 * SRS-MAP-CHN-001, SRS-MAP-CHN-002
 */
public class ChannelView extends JPanel {

    private JList<Channel> mChannelList;
    private DefaultListModel<Channel> mListModel;
    private JTextField mSearchField;
    private JButton mCreateButton;

    public ChannelView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.SIDEBAR);

        // === Header (titre + recherche) ===
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Theme.SIDEBAR);

        // Titre + bouton "+ Créer"
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(Theme.SIDEBAR);
        titleRow.setBorder(new EmptyBorder(10, 14, 4, 10));
        titleRow.setAlignmentX(LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Canaux");
        titleLabel.setFont(Theme.FONT_SUBTITLE);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);
        titleRow.add(titleLabel, BorderLayout.CENTER);

        mCreateButton = new JButton("+");
        Theme.styleButton(mCreateButton, Theme.ACCENT, java.awt.Color.WHITE);
        mCreateButton.setPreferredSize(new Dimension(26, 22));
        mCreateButton.setToolTipText("Creer un canal");
        titleRow.add(mCreateButton, BorderLayout.EAST);

        headerPanel.add(titleRow);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Theme.SIDEBAR);
        searchPanel.setBorder(new EmptyBorder(0, 14, 10, 14));
        searchPanel.setAlignmentX(LEFT_ALIGNMENT);
        mSearchField = new JTextField();
        mSearchField.setFont(Theme.FONT_BODY);
        mSearchField.setToolTipText("Rechercher par nom de canal");
        searchPanel.add(mSearchField, BorderLayout.CENTER);
        headerPanel.add(searchPanel);

        add(headerPanel, BorderLayout.NORTH);

        // === Liste ===
        mListModel = new DefaultListModel<>();
        mChannelList = new JList<>(mListModel);
        mChannelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mChannelList.setCellRenderer(new ChannelCellRenderer());
        mChannelList.setFixedCellHeight(48);
        mChannelList.setBackground(Theme.SIDEBAR);
        mChannelList.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(mChannelList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.SIDEBAR);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Met à jour la liste des canaux affichés.
     */
    public void updateChannelList(List<Channel> channels) {
        mListModel.clear();
        for (Channel channel : channels) {
            mListModel.addElement(channel);
        }
    }

    public JTextField getSearchField() {
        return mSearchField;
    }

    public Channel getSelectedChannel() {
        return mChannelList.getSelectedValue();
    }

    public JList<Channel> getChannelList() {
        return mChannelList;
    }

    public void addCreateChannelListener(ActionListener l) {
        mCreateButton.addActionListener(l);
    }

    // ===================================================================
    // Renderer des cellules
    // ===================================================================

    private static class ChannelCellRenderer extends JPanel implements ListCellRenderer<Channel> {

        private ChannelIconPanel mIcon;
        private JLabel mNameLabel;
        private JLabel mSubLabel;

        public ChannelCellRenderer() {
            setLayout(new BorderLayout(10, 0));
            setBorder(new EmptyBorder(4, 10, 4, 10));
            setOpaque(true);

            mIcon = new ChannelIconPanel();
            add(mIcon, BorderLayout.WEST);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            infoPanel.add(Box.createVerticalGlue());

            mNameLabel = new JLabel();
            mNameLabel.setFont(Theme.FONT_BODY);
            infoPanel.add(mNameLabel);

            mSubLabel = new JLabel();
            mSubLabel.setFont(Theme.FONT_SMALL);
            mSubLabel.setForeground(Theme.TEXT_SECONDARY);
            infoPanel.add(mSubLabel);

            infoPanel.add(Box.createVerticalGlue());
            add(infoPanel, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Channel> list, Channel channel,
                int index, boolean isSelected, boolean cellHasFocus) {

            mIcon.setPrivate(channel.isPrivate());
            mIcon.setIconColor(getChannelColor(channel.getName()));

            mNameLabel.setText(channel.getName());
            String creatorInfo = "@" + channel.getCreator().getUserTag();
            if (channel.isPrivate()) {
                creatorInfo += "  \uD83D\uDD12";
            }
            mSubLabel.setText(creatorInfo);

            if (isSelected) {
                setBackground(Theme.SELECTION);
                mNameLabel.setForeground(Theme.ACCENT);
            } else {
                setBackground(Theme.SIDEBAR);
                mNameLabel.setForeground(Theme.TEXT_PRIMARY);
            }

            return this;
        }

        private Color getChannelColor(String name) {
            Color[] colors = {
                new Color(88, 101, 242),
                new Color(87, 169, 131),
                new Color(237, 135, 82),
                new Color(155, 89, 182),
                new Color(52, 152, 219),
                new Color(149, 165, 166)
            };
            return colors[Math.abs(name.hashCode()) % colors.length];
        }
    }

    /**
     * Icône ronde avec # (public) ou un symbole cadenas pour les canaux privés.
     */
    private static class ChannelIconPanel extends JPanel {

        private boolean mIsPrivate = false;
        private Color mColor = Theme.ACCENT;

        public ChannelIconPanel() {
            setPreferredSize(new Dimension(34, 34));
            setOpaque(false);
        }

        public void setPrivate(boolean isPrivate) {
            this.mIsPrivate = isPrivate;
        }

        public void setIconColor(Color color) {
            this.mColor = color;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(mColor);
            g2.fillOval(0, 0, 34, 34);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            String symbol = mIsPrivate ? "\uD83D\uDD12" : "#";
            // Fallback pour les polices sans support emoji
            if (mIsPrivate) {
                symbol = "P";
            }
            int textWidth = g2.getFontMetrics().stringWidth(symbol);
            int textHeight = g2.getFontMetrics().getAscent();
            g2.drawString(symbol, (34 - textWidth) / 2, (34 + textHeight) / 2 - 2);

            g2.dispose();
        }
    }
}
