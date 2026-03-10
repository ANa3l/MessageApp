package main.java.com.ubo.tp.message.ihm.channel.creator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue du formulaire de creation de canal.
 * Champs : nom du canal, option prive/public.
 * SRS-MAP-CHN-003, SRS-MAP-CHN-004
 */
public class ChannelCreatorView extends JPanel {

    private static final Color ERROR_COLOR = new Color(200, 50, 50);

    private JTextField mNameField;
    private JCheckBox mPrivateCheckBox;
    private JLabel mErrorLabel;
    private JButton mCreateButton;
    private JButton mCancelButton;

    public ChannelCreatorView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // === Titre ===
        JLabel titleLabel = new JLabel("Creer un canal");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(24, 30, 16, 30));
        add(titleLabel, BorderLayout.NORTH);

        // === Formulaire ===
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Theme.BACKGROUND);
        formPanel.setBorder(new EmptyBorder(0, 30, 0, 30));

        JLabel nameLabel = new JLabel("Nom du canal");
        nameLabel.setFont(Theme.FONT_BODY);
        nameLabel.setForeground(Theme.TEXT_SECONDARY);
        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(4));

        mNameField = new JTextField();
        mNameField.setFont(Theme.FONT_BODY);
        mNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        formPanel.add(mNameField);
        formPanel.add(Box.createVerticalStrut(12));

        mPrivateCheckBox = new JCheckBox("Canal prive (accessible uniquement sur invitation)");
        mPrivateCheckBox.setFont(Theme.FONT_BODY);
        mPrivateCheckBox.setForeground(Theme.TEXT_PRIMARY);
        mPrivateCheckBox.setBackground(Theme.BACKGROUND);
        formPanel.add(mPrivateCheckBox);
        formPanel.add(Box.createVerticalStrut(8));

        mErrorLabel = new JLabel(" ");
        mErrorLabel.setFont(Theme.FONT_SMALL);
        mErrorLabel.setForeground(ERROR_COLOR);
        formPanel.add(mErrorLabel);

        add(formPanel, BorderLayout.CENTER);

        // === Boutons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 16));
        buttonPanel.setBackground(Theme.BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(0, 30, 10, 30));

        mCancelButton = new JButton("Annuler");
        Theme.styleButton(mCancelButton, Theme.SIDEBAR, Theme.TEXT_PRIMARY);
        buttonPanel.add(mCancelButton);

        mCreateButton = new JButton("Creer");
        Theme.styleButton(mCreateButton, Theme.ACCENT, Color.WHITE);
        buttonPanel.add(mCreateButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getChannelName() {
        return mNameField.getText();
    }

    public boolean isPrivate() {
        return mPrivateCheckBox.isSelected();
    }

    public void showError(String message) {
        mErrorLabel.setText(message);
    }

    /** Remet le formulaire a zero. */
    public void reset() {
        mNameField.setText("");
        mPrivateCheckBox.setSelected(false);
        mErrorLabel.setText(" ");
    }

    public void addCreateListener(ActionListener l) {
        mCreateButton.addActionListener(l);
    }

    public void addCancelListener(ActionListener l) {
        mCancelButton.addActionListener(l);
    }
}
