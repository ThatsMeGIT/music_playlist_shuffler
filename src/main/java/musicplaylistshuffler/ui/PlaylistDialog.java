package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlaylistDialog extends JDialog {

    private final Playlist playlist;
    private final Player player;
    private final MainFrame mainFrame;
    private final MainActionHandler actionHandler;

    public PlaylistDialog(MainFrame owner, Player player, Playlist playlist, MainActionHandler actionHandler) {
        super(owner, playlist.getName(), true);

        this.actionHandler = actionHandler;
        this.mainFrame = owner;
        this.playlist = playlist;
        this.player = player;

        setSize(720, 460);
        setMinimumSize(new Dimension(600, 420));
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        JPanel playlistPanel = UiStyle.panel(new BorderLayout(0, UiStyle.GAP), UiStyle.BACKGROUND);
        playlistPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));


        playlistPanel.add(createPlaylistOverviewPanel(), BorderLayout.NORTH);
        playlistPanel.add(createSongTablePanel(), BorderLayout.CENTER);

        JPanel statPanel = UiStyle.panel(new BorderLayout(), UiStyle.BACKGROUND);
        statPanel.add(createStatPanel(), BorderLayout.NORTH);

        tabs.addTab("Overview", playlistPanel);
        tabs.addTab("Details", statPanel);

        tabs.setUI(new PixelTabsUi());
        tabs.setFont(UiStyle.BODY_FONT);
        tabs.setForeground(UiStyle.TEXT);
        tabs.setBackground(UiStyle.BACKGROUND);
        JPanel content = UiStyle.panel(new BorderLayout(), UiStyle.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(tabs, BorderLayout.CENTER);
        add(content);

        setVisible(true);
    }


    private JPanel createPlaylistControlPanel(){
        JPanel panel = UiStyle.panel(new FlowLayout(FlowLayout.LEFT, 8, 0), UiStyle.BACKGROUND);

        JButton playPlaylistButton = new JButton("Play playlist");
        playPlaylistButton.setActionCommand("playPlaylist");
        playPlaylistButton.addActionListener(e -> {
            actionHandler.actionPerformed(e, playlist);
            dispose();
        });

        JButton exportToCSVButton = new JButton("Export to CSV");
        exportToCSVButton.setActionCommand("exportPlaylist");

        exportToCSVButton.addActionListener(e -> {
            actionHandler.actionPerformed(e, playlist);
            dispose();
        });

        JButton deletePlaylistButton = new JButton("Delete Playlist");
        deletePlaylistButton.setActionCommand("deletePlaylist");
        deletePlaylistButton.addActionListener(e -> {
            actionHandler.actionPerformed(e, playlist);
            dispose();
        });

        UiStyle.button(playPlaylistButton, "Start playing this playlist", true);
        UiStyle.button(exportToCSVButton, "Export this playlist to CSV", false);
        UiStyle.button(deletePlaylistButton, "Delete this playlist", false);
        deletePlaylistButton.setForeground(UiStyle.DANGER);

        panel.add(playPlaylistButton);
        panel.add(exportToCSVButton);
        panel.add(deletePlaylistButton);

        return panel;
    }

    private JPanel createPlaylistOverviewPanel() {
        JPanel panel = UiStyle.panel(new BorderLayout(0, UiStyle.GAP), UiStyle.BACKGROUND);
        JPanel metadata = UiStyle.panel(new BorderLayout(0, 4), UiStyle.BACKGROUND);
        JLabel name = UiStyle.heading(playlist.getName(), 22f);
        name.setToolTipText(playlist.getName());
        JLabel description = UiStyle.secondary(playlist.getDescription());
        description.setToolTipText(playlist.getDescription());
        metadata.add(name, BorderLayout.NORTH);
        if (playlist.getDescription() != null && !playlist.getDescription().isBlank()) {
            metadata.add(description, BorderLayout.SOUTH);
        }
        panel.add(metadata, BorderLayout.NORTH);
        panel.add(createPlaylistControlPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSongTablePanel() {
        JPanel panel = UiStyle.panel(new BorderLayout(0, 8), UiStyle.BACKGROUND);

        String[] columnNames = {"Title", "Artist", "Genre", "Duration"};

        Object[][] data = new Object[playlist.getCurrentList().size()][4];

        for (int i = 0; i < playlist.getCurrentList().size(); i++) {
            var song = playlist.getCurrentList().get(i);

            data[i][0] = song.getTitle();
            data[i][1] = song.getArtist();
            data[i][2] = song.getGenre();
            data[i][3] = TimeUtils.formatDuration(song.getDuration());
        }

        JTable songTable = new JTable(data, columnNames);

        songTable.setUpdateSelectionOnSort(true);

        songTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int row = songTable.getSelectedRow();
                    if (row >= 0) {
                        player.removeSong(playlist,
                                playlist.getCurrentList().get(songTable.convertRowIndexToModel(row)));
                        mainFrame.refreshPlayerView();
                        dispose();
                    }
                }
            }
        });

        songTable.getColumnModel().getColumn(0).setPreferredWidth(240);
        songTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        songTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        songTable.getColumnModel().getColumn(3).setPreferredWidth(112);
        songTable.getColumnModel().getColumn(3).setMinWidth(112);
        JScrollPane scrollPane = UiStyle.table(songTable, 3);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(UiStyle.secondary("Double-click a song to remove it from this playlist."), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatPanel() {
        JPanel panel = UiStyle.panel(new GridLayout(4, 2, 16, 16), UiStyle.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiStyle.BORDER),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        panel.add(UiStyle.secondary("Number of Songs:"));
        panel.add(new JLabel(String.valueOf(playlist.getSongs().size())));

        panel.add(UiStyle.secondary("Total Length:"));
        panel.add(new JLabel(TimeUtils.formatDuration(playlist.playlistTimeLength())));

        panel.add(UiStyle.secondary("Average Song Length:"));
        panel.add(new JLabel(TimeUtils.formatDuration((int) playlist.averageSongLength())));

        panel.add(UiStyle.secondary("Top Genre:"));
        panel.add(new JLabel(playlist.topGenre() != null ? playlist.topGenre() : "-"));

        return panel;
    }


}
