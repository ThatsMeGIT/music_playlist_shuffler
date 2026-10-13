package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MainFrame extends JFrame {

    private final Player player;

    private final MainActionHandler actionHandler;
    private final PlaybackControlPanel playbackControlPanel;
    private final MenuBar menuBar;
    private final PlaylistTableModel playlistTableModel;
    private final JTable playlistTable;

    public MainFrame(Player player, String path) {
        this(player, path, true);
    }

    public MainFrame(Player player, String path, boolean saveOnClose) {
        this.player = player;
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

        CloseHandler closeHandler = new CloseHandler(player, path, saveOnClose, () -> {
            dispose();
            System.exit(0);
        }, () -> JOptionPane.showMessageDialog(MainFrame.this,
                "Could not save playlists. The window will stay open so you can try again.\n"
                        + "Check that the save folder is accessible and writable.",
                "Could not save playlists", JOptionPane.ERROR_MESSAGE),
                saving -> setEnabled(!saving));
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeHandler.requestClose();
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
