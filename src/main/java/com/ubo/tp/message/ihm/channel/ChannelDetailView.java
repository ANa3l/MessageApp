package main.java.com.ubo.tp.message.ihm.channel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.datamodel.Channel;
import main.java.com.ubo.tp.message.datamodel.User;
import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue de detail d'un canal.
 * SRS-MAP-CHN-005, SRS-MAP-CHN-006, SRS-MAP-CHN-007, SRS-MAP-CHN-008
 */
public class ChannelDetailView extends JPanel {

    private UUID mConnectedUserUuid;

    private JLabel mNameLabel;
    private JLabel mPrivateLabel;
    private JLabel mCreatorLabel;
    private DefaultListModel<User> mMembersModel;
    private JList<User> mMembersList;
    private JButton mDeleteButton;
    private JButton mLeaveButton;
    private JButton mAddMemberButton;
    private JButton mRemoveMemberButton;
    private JComboBox<User> mAddMemberCombo;
    private JPanel mAddRow;
    private JButton mCloseButton;

    public ChannelDetailView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BACKGROUND);
        content.setBorder(new EmptyBorder(20, 24, 16, 24));

        // === Bouton fermer ===
        mCloseButton = new JButton("\u2715");
        mCloseButton.setFont(Theme.FONT_TITLE);
        mCloseButton.setForeground(Theme.TEXT_SECONDARY);
        mCloseButton.setBackground(Theme.BACKGROUND);
        mCloseButton.setBorderPainted(false);
        mCloseButton.setFocusPainted(false);
        mCloseButton.setContentAreaFilled(false);
        mCloseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mCloseButton.setToolTipText("Fermer");
        JPanel closeRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        closeRow.setBackground(Theme.BACKGROUND);
        closeRow.setAlignmentX(LEFT_ALIGNMENT);
        closeRow.add(mCloseButton);
        content.add(closeRow);
        content.add(Box.createVerticalStrut(4));

        // === Header ===
        mNameLabel = new JLabel(" ");
        mNameLabel.setFont(Theme.FONT_TITLE);
        mNameLabel.setForeground(Theme.TEXT_PRIMARY);
        mNameLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(mNameLabel);
        content.add(Box.createVerticalStrut(4));

        mPrivateLabel = new JLabel(" ");
        mPrivateLabel.setFont(Theme.FONT_SMALL);
        mPrivateLabel.setForeground(Theme.TEXT_SECONDARY);
        mPrivateLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(mPrivateLabel);

        mCreatorLabel = new JLabel(" ");
        mCreatorLabel.setFont(Theme.FONT_SMALL);
        mCreatorLabel.setForeground(Theme.TEXT_SECONDARY);
        mCreatorLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(mCreatorLabel);
        content.add(Box.createVerticalStrut(16));

        // === Membres ===
        JLabel membersTitle = new JLabel("Membres");
        membersTitle.setFont(Theme.FONT_SUBTITLE);
        membersTitle.setForeground(Theme.TEXT_SECONDARY);
        membersTitle.setAlignmentX(LEFT_ALIGNMENT);
        content.add(membersTitle);
        content.add(Box.createVerticalStrut(6));

        mMembersModel = new DefaultListModel<>();
        mMembersList = new JList<>(mMembersModel);
        mMembersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mMembersList.setBackground(Theme.SIDEBAR);
        mMembersList.setFont(Theme.FONT_BODY);
        mMembersList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User u = (User) value;
                    if (mConnectedUserUuid != null && u.getUuid().equals(mConnectedUserUuid)) {
                        setText("Moi  (@" + u.getUserTag() + ")");
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        setText(u.getName() + "  @" + u.getUserTag());
                    }
                }
                return this;
            }
        });

        JScrollPane membersScroll = new JScrollPane(mMembersList);
        membersScroll.setBorder(null);
        membersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        membersScroll.setAlignmentX(LEFT_ALIGNMENT);
        membersScroll.setPreferredSize(new Dimension(0, 140));
        membersScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        content.add(membersScroll);
        content.add(Box.createVerticalStrut(16));

        // === Actions proprietaire ===
        mAddMemberCombo = new JComboBox<>();
        mAddMemberCombo.setFont(Theme.FONT_BODY);
        mAddMemberCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User u = (User) value;
                    setText(u.getName() + "  @" + u.getUserTag());
                }
                return this;
            }
        });

        mAddMemberButton = new JButton("+ Ajouter");
        Theme.styleButton(mAddMemberButton, Theme.ACCENT, Color.WHITE);

        mAddRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        mAddRow.setBackground(Theme.BACKGROUND);
        mAddRow.setAlignmentX(LEFT_ALIGNMENT);
        mAddRow.add(mAddMemberCombo);
        mAddRow.add(mAddMemberButton);
        content.add(mAddRow);
        content.add(Box.createVerticalStrut(8));

        mRemoveMemberButton = new JButton("Retirer le membre selectionne");
        Theme.styleButton(mRemoveMemberButton, Theme.SIDEBAR, Theme.TEXT_PRIMARY);
        mRemoveMemberButton.setAlignmentX(LEFT_ALIGNMENT);
        content.add(mRemoveMemberButton);
        content.add(Box.createVerticalStrut(16));

        // === Actions danger ===
        mDeleteButton = new JButton("Supprimer le canal");
        Theme.styleButton(mDeleteButton, new Color(200, 50, 50), Color.WHITE);
        mDeleteButton.setAlignmentX(LEFT_ALIGNMENT);
        content.add(mDeleteButton);
        content.add(Box.createVerticalStrut(8));

        mLeaveButton = new JButton("Quitter le canal");
        Theme.styleButton(mLeaveButton, Theme.SIDEBAR, Theme.TEXT_PRIMARY);
        mLeaveButton.setAlignmentX(LEFT_ALIGNMENT);
        content.add(mLeaveButton);

        JScrollPane mainScroll = new JScrollPane(content);
        mainScroll.setBorder(null);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(mainScroll, BorderLayout.CENTER);
    }

    public void setChannel(Channel channel, boolean isCreator, UUID connectedUserUuid) {
        mConnectedUserUuid = connectedUserUuid;
        mNameLabel.setText(channel.getName());
        mPrivateLabel.setText(channel.isPrivate() ? "Canal prive \uD83D\uDD12" : "Canal public");
        mCreatorLabel.setText("Cree par @" + channel.getCreator().getUserTag());

        mMembersModel.clear();
        for (User u : channel.getUsers()) {
            mMembersModel.addElement(u);
        }

        boolean showOwnerActions = isCreator && channel.isPrivate();
        mAddRow.setVisible(showOwnerActions);
        mRemoveMemberButton.setVisible(showOwnerActions);
        mDeleteButton.setVisible(isCreator && channel.isPrivate());
        mLeaveButton.setVisible(!isCreator && channel.isPrivate());
    }

    public void setAddableMemberUsers(List<User> users) {
        mAddMemberCombo.removeAllItems();
        for (User u : users) {
            mAddMemberCombo.addItem(u);
        }
    }

    public User getSelectedMember() { return mMembersList.getSelectedValue(); }
    public User getSelectedAddableUser() { return (User) mAddMemberCombo.getSelectedItem(); }

    public void addDeleteListener(ActionListener l) { mDeleteButton.addActionListener(l); }
    public void addLeaveListener(ActionListener l) { mLeaveButton.addActionListener(l); }
    public void addAddMemberListener(ActionListener l) { mAddMemberButton.addActionListener(l); }
    public void addRemoveMemberListener(ActionListener l) { mRemoveMemberButton.addActionListener(l); }
    public void addCloseListener(ActionListener l) { mCloseButton.addActionListener(l); }
}