package musicplaylistshuffler.ui;

import com.sun.tools.javac.Main;
import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.utils.CsvUtils;
import musicplaylistshuffler.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDialog extends JDialog {

    private final Playlist playlist;
    private final Player player;
    private final MainFrame mainFrame;

    public PlaylistDialog(MainFrame owner, Player player, Playlist playlist) {
        super(owner, playlist.getName(), true);

        this.mainFrame = owner;
        this.playlist = playlist;
        this.player = player;

        setSize(600, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        JPanel playlistPanel = new JPanel(new BorderLayout());


        playlistPanel.add(createPlaylistOverviewPanel(), BorderLayout.NORTH);
        playlistPanel.add(createSongTablePanel(), BorderLayout.CENTER);

        JPanel statPanel = new JPanel();
        statPanel.add(createStatPanel());

        tabs.addTab("Overview", playlistPanel);
        tabs.addTab("Details", statPanel);

        add(tabs);

        setVisible(true);
    }

    private JPanel createPlaylistControlPanel(){
        JPanel panel = new JPanel(new GridLayout(1,3));

        JButton playPlaylistButton = new JButton("▶");
        playPlaylistButton.setActionCommand("playPlaylist");
        playPlaylistButton.addActionListener(e -> {
            player.playPlaylist(playlist);
            dispose();
        });

        JButton exportToCSVButton = new JButton("Export to CSV");
        exportToCSVButton.setActionCommand("export");
        exportToCSVButton.addActionListener(e -> {
            try {
                CsvUtils.exportPlaylistToCsv(playlist);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        JButton deletePlaylistButton = new JButton("Delete Playlist");
        deletePlaylistButton.setActionCommand("deletePlaylist");
        deletePlaylistButton.addActionListener(e -> {
            System.out.println("Test");
            //player.removePlaylist(playlist);
            player.addPlaylist(playlist);

            mainFrame.refreshPlayerView();
            dispose();
        });


        panel.add(playPlaylistButton);
        panel.add(exportToCSVButton);
        panel.add(deletePlaylistButton);

        return panel;
    }

    private JPanel createPlaylistOverviewPanel(){
        JPanel panel = new JPanel(new GridLayout(3,1));

        JLabel name = new JLabel(playlist.getName());
        JLabel description = new JLabel(playlist.getDescription());

        panel.add(createPlaylistControlPanel());
        panel.add(name);
        panel.add(description);

        return panel;

    }

    private JPanel createSongTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Title", "Artist", "Genre", "Duration"};

        Object[][] data = new Object[playlist.getCurrentList().size()][4];

        for (int i = 0; i < playlist.getCurrentList().size(); i++) {
            var song = playlist.getCurrentList().get(i);

            data[i][0] = song.getTitle();
            data[i][1] = song.getArtist();
            data[i][2] = song.getGenre();
            data[i][3] = TimeUtils.formatDuration(song.getDuration());
        }

        JTable songTable = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public boolean isColumnEditable(int column) {
                return true;
            }
        };

        songTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int row = songTable.getSelectedRow();
                    System.out.println(row);
                    playlist.removeSong(playlist.getCurrentList().get(row));
                    dispose();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(songTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Number of Songs:"));
        panel.add(new JLabel(String.valueOf(playlist.getSongs().size())));

        panel.add(new JLabel("Total Length:"));
        panel.add(new JLabel(TimeUtils.formatDuration(playlist.playlistTimeLength())));

        panel.add(new JLabel("Average Song Length:"));
        panel.add(new JLabel(TimeUtils.formatDuration((int) playlist.averageSongLength())));

        panel.add(new JLabel("Top Genre:"));
        panel.add(new JLabel(playlist.topGenre() != null ? playlist.topGenre() : "-"));

        return panel;
    }


}
