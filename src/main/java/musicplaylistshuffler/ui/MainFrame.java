package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.storage.StorageService;

import javax.swing.*;
import javax.xml.xpath.XPath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MainFrame extends JFrame {

    private final Player player;
    private final String path;

    private final MainActionHandler actionHandler;
    private final PlaybackControlPanel playbackControlPanel;
    private final MenuBar menuBar;
    private final PlaylistTableModel playlistTableModel;
    private final JTable playlistTable;

    public MainFrame(Player player, String path) {
        this.player = player;
        this.path = path;
        this.actionHandler = new MainActionHandler(player, this);
        this.playbackControlPanel = new PlaybackControlPanel(player, actionHandler);
        this.menuBar = new MenuBar(actionHandler);
        this.playlistTableModel = new PlaylistTableModel(player);
        this.playlistTable = new JTable(playlistTableModel);

        playlistTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int row = playlistTable.getSelectedRow();

                    if (row >= 0) {
                        Playlist playlist = player.getPlaylists().get(row);
                        new PlaylistDialog(MainFrame.this, player, playlist, actionHandler);
                    }
                }
            }
        });

        setTitle("Best Music Player");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                StorageService.savePlayerToJson(player, path);
                dispose();
                System.exit(0);
            }
        });

        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        URL iconUrl = MainFrame.class.getResource("/icons/MusicPlaylistShufflerLogo.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }

        setJMenuBar(menuBar);

        add(playbackControlPanel, BorderLayout.SOUTH);
        add(new JScrollPane(playlistTable), BorderLayout.CENTER);

        setVisible(true);
        refreshPlayerView();
    }


    public void refreshPlayerView() {
        playbackControlPanel.refreshPlayerView();
        playlistTableModel.fireTableDataChanged();
    }
}
