package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;

public class PlaylistDialog extends JDialog {

    private final Playlist playlist;
    private final Player player;

    public PlaylistDialog(Frame owner, Player player, Playlist playlist) {
        super(owner, playlist.getName(), true);

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

    private JPanel createPlaylistOverviewPanel(){
        JPanel panel = new JPanel(new GridLayout(3,1));

        JButton playPlaylist = new JButton("▶");
        playPlaylist.setActionCommand("playPlaylist");
        playPlaylist.addActionListener(e -> {
            player.playPlaylist(playlist);
            dispose();
        });


        JLabel name = new JLabel(playlist.getName());
        JLabel description = new JLabel(playlist.getDescription());

        panel.add(playPlaylist);
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
        };
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
