package main.java.com.ubo.tp.message.ihm.message;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import main.java.com.ubo.tp.message.datamodel.Message;
import main.java.com.ubo.tp.message.ihm.Theme;

/**
 * Vue du mode recherche WhatsApp-style.
 * Affiche un champ de recherche et les résultats (date + texte avec highlight).
 */
public class MessageSearchView extends JPanel {

    private JTextField mSearchField;
    private JPanel mSearchResultsPanel;
    private String mRecipientName = "";
    private Consumer<Message> mOnResultClick;

    public MessageSearchView(Runnable onClose) {
        initComponents(onClose);
    }

    private void initComponents(Runnable onClose) {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // --- Header recherche : [✕]  Rechercher des messages ---
        JPanel searchHeader = new JPanel(new BorderLayout());
        searchHeader.setBackground(Theme.BACKGROUND);
        searchHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(10, 10, 10, 15)));

        JButton closeSearchBtn = new JButton("\u2715");
        closeSearchBtn.setFont(Theme.FONT_TITLE);
        closeSearchBtn.setForeground(Theme.TEXT_SECONDARY);
        closeSearchBtn.setBackground(Theme.BACKGROUND);
        closeSearchBtn.setBorderPainted(false);
        closeSearchBtn.setFocusPainted(false);
        closeSearchBtn.setContentAreaFilled(false);
        closeSearchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeSearchBtn.addActionListener(e -> onClose.run());

        JLabel searchTitle = new JLabel("Rechercher des messages");
        searchTitle.setFont(Theme.FONT_TITLE);
        searchTitle.setForeground(Theme.TEXT_PRIMARY);
        searchTitle.setBorder(new EmptyBorder(0, 6, 0, 0));

        JPanel searchHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchHeaderLeft.setBackground(Theme.BACKGROUND);
        searchHeaderLeft.add(closeSearchBtn);
        searchHeaderLeft.add(searchTitle);
        searchHeader.add(searchHeaderLeft, BorderLayout.CENTER);

        // --- Barre de recherche stylisée ---
        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(Theme.BACKGROUND);
        searchBarPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                new EmptyBorder(8, 15, 8, 15)));

        JPanel searchFieldWrapper = new JPanel(new BorderLayout());
        searchFieldWrapper.setBackground(Theme.SURFACE);
        searchFieldWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 2, true),
                new EmptyBorder(4, 8, 4, 8)));

        JLabel searchIcon = new JLabel("\uD83D\uDD0D ");
        searchIcon.setFont(Theme.FONT_BODY);
        searchIcon.setForeground(Theme.TEXT_SECONDARY);
        searchFieldWrapper.add(searchIcon, BorderLayout.WEST);

        mSearchField = new JTextField();
        mSearchField.setFont(Theme.FONT_BODY);
        mSearchField.setBorder(null);
        searchFieldWrapper.add(mSearchField, BorderLayout.CENTER);

        // Bouton ✕ pour vider le champ
        JButton clearFieldBtn = new JButton("\u2715");
        clearFieldBtn.setFont(Theme.FONT_SMALL);
        clearFieldBtn.setForeground(Theme.TEXT_LIGHT);
        clearFieldBtn.setBackground(Theme.SURFACE);
        clearFieldBtn.setBorderPainted(false);
        clearFieldBtn.setFocusPainted(false);
        clearFieldBtn.setContentAreaFilled(false);
        clearFieldBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearFieldBtn.setVisible(false);
        clearFieldBtn.addActionListener(e -> {
            mSearchField.setText("");
            mSearchField.requestFocusInWindow();
        });
        searchFieldWrapper.add(clearFieldBtn, BorderLayout.EAST);

        mSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { clearFieldBtn.setVisible(mSearchField.getText().length() > 0); }
            @Override public void removeUpdate(DocumentEvent e) { clearFieldBtn.setVisible(mSearchField.getText().length() > 0); }
            @Override public void changedUpdate(DocumentEvent e) { clearFieldBtn.setVisible(mSearchField.getText().length() > 0); }
        });

        searchBarPanel.add(searchFieldWrapper, BorderLayout.CENTER);

        JPanel northSearch = new JPanel();
        northSearch.setLayout(new BoxLayout(northSearch, BoxLayout.Y_AXIS));
        northSearch.add(searchHeader);
        northSearch.add(searchBarPanel);
        add(northSearch, BorderLayout.NORTH);

        // --- Zone résultats ---
        mSearchResultsPanel = new JPanel();
        mSearchResultsPanel.setLayout(new BoxLayout(mSearchResultsPanel, BoxLayout.Y_AXIS));
        mSearchResultsPanel.setBackground(Theme.BACKGROUND);
        mSearchResultsPanel.setBorder(new EmptyBorder(8, 0, 8, 0));

        JScrollPane searchScroll = new JScrollPane(mSearchResultsPanel);
        searchScroll.setBorder(null);
        searchScroll.getViewport().setBackground(Theme.BACKGROUND);
        searchScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(searchScroll, BorderLayout.CENTER);
    }

    // === API publique ===

    public JTextField getSearchField() {
        return mSearchField;
    }

    public void setRecipientName(String name) {
        this.mRecipientName = name;
    }

    public void setOnResultClick(Consumer<Message> listener) {
        this.mOnResultClick = listener;
    }

    /**
     * Affiche le placeholder "Recherchez des messages avec [Nom]."
     */
    public void showPlaceholder() {
        mSearchResultsPanel.removeAll();
        mSearchResultsPanel.add(Box.createVerticalGlue());
        JLabel placeholder = new JLabel("Recherchez des messages avec " + mRecipientName + ".");
        placeholder.setFont(Theme.FONT_BODY);
        placeholder.setForeground(Theme.TEXT_LIGHT);
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        placeholder.setAlignmentX(CENTER_ALIGNMENT);
        mSearchResultsPanel.add(placeholder);
        mSearchResultsPanel.add(Box.createVerticalGlue());
        mSearchResultsPanel.revalidate();
        mSearchResultsPanel.repaint();
    }

    /**
     * Affiche les résultats de recherche (liste d'items date + preview).
     */
    public void updateSearchResults(List<Message> results, String query) {
        mSearchResultsPanel.removeAll();
        if (results.isEmpty()) {
            mSearchResultsPanel.add(Box.createVerticalGlue());
            JLabel noResult = new JLabel("Aucun message trouvé");
            noResult.setFont(Theme.FONT_BODY);
            noResult.setForeground(Theme.TEXT_LIGHT);
            noResult.setHorizontalAlignment(SwingConstants.CENTER);
            noResult.setAlignmentX(CENTER_ALIGNMENT);
            mSearchResultsPanel.add(noResult);
            mSearchResultsPanel.add(Box.createVerticalGlue());
        } else {
            for (Message msg : results) {
                mSearchResultsPanel.add(buildSearchResultItem(msg, query));
            }
        }
        mSearchResultsPanel.revalidate();
        mSearchResultsPanel.repaint();
    }

    // === Construction des items de résultat ===

    private JPanel buildSearchResultItem(Message msg, String query) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(Theme.BACKGROUND);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 240)),
                new EmptyBorder(10, 20, 10, 20)));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        String dateStr = formatSearchDate(msg.getEmissionDate());
        JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setFont(Theme.FONT_SMALL);
        dateLabel.setForeground(Theme.TEXT_SECONDARY);

        String preview = truncateAndHighlight(msg.getText(), query, 80);
        JLabel textLabel = new JLabel("<html>" + preview + "</html>");
        textLabel.setFont(Theme.FONT_BODY);
        textLabel.setForeground(Theme.TEXT_PRIMARY);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BACKGROUND);
        dateLabel.setAlignmentX(LEFT_ALIGNMENT);
        textLabel.setAlignmentX(LEFT_ALIGNMENT);
        content.add(dateLabel);
        content.add(Box.createRigidArea(new Dimension(0, 3)));
        content.add(textLabel);

        item.add(content, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(Theme.SELECTION);
                content.setBackground(Theme.SELECTION);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(Theme.BACKGROUND);
                content.setBackground(Theme.BACKGROUND);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mOnResultClick != null) {
                    mOnResultClick.accept(msg);
                }
            }
        });

        return item;
    }

    private String formatSearchDate(long timestamp) {
        Date date = new Date(timestamp);
        Calendar msgCal = Calendar.getInstance();
        msgCal.setTime(date);
        Calendar today = Calendar.getInstance();
        if (msgCal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && msgCal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return new SimpleDateFormat("HH:mm").format(date);
        }
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    private String truncateAndHighlight(String text, String query, int maxLen) {
        String escaped = escapeHtml(text).replace("\n", " ");
        if (escaped.length() > maxLen) {
            escaped = escaped.substring(0, maxLen) + "...";
        }
        if (query == null || query.isEmpty()) {
            return escaped;
        }
        String escapedQuery = escapeHtml(query);
        Pattern pattern = Pattern.compile("(" + Pattern.quote(escapedQuery) + ")", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(escaped);
        return matcher.replaceAll("<b style='color:" + colorToHex(Theme.ACCENT) + "'>$1</b>");
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }

    private String colorToHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}