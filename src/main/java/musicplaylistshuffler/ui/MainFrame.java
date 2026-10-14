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

        setTitle("Music Playlist Shuffler");
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
        add(createLibraryPanel(), BorderLayout.CENTER);

        setVisible(true);
        refreshPlayerView();
    }


    private JPanel createLibraryPanel() {
        JPanel library = UiStyle.panel(new BorderLayout(0, UiStyle.PADDING), UiStyle.BACKGROUND);
        library.setBorder(BorderFactory.createEmptyBorder(
                UiStyle.PADDING, UiStyle.PADDING, UiStyle.PADDING, UiStyle.PADDING));
        JPanel header = new ForestHeader();
        JPanel title = UiStyle.panel(new GridLayout(2, 1, 0, 6), UiStyle.BACKGROUND);
        title.setOpaque(false);
        title.add(new PixelHeading("Your playlists", 3));
        title.add(UiStyle.secondary("A little music for your evening."));
        JButton importButton = new JButton("Import playlist");
        importButton.setActionCommand("addPlaylist");
        importButton.addActionListener(actionHandler);
        UiStyle.button(importButton, "Import a playlist from a CSV file", false);
        importButton.setIcon(new PixelIcon(PixelIcon.Kind.IMPORT, 16));
        JPanel actions = UiStyle.panel(new FlowLayout(FlowLayout.RIGHT, 0, 0), UiStyle.BACKGROUND);
        actions.setOpaque(false);
        actions.add(importButton);
        header.add(title, BorderLayout.CENTER);
        header.add(actions, BorderLayout.EAST);
        playlistTable.getColumnModel().getColumn(0).setPreferredWidth(360);
        playlistTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        playlistTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        playlistTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        library.add(header, BorderLayout.NORTH);
        library.add(UiStyle.table(playlistTable, 1, 2), BorderLayout.CENTER);
        playlistTable.setRowHeight(44);
        library.add(UiStyle.secondary("Double-click a playlist to explore its songs."), BorderLayout.SOUTH);
        return library;
    }

    public void refreshPlayerView() {
        playbackControlPanel.refreshPlayerView();
        playlistTableModel.fireTableDataChanged();
    }
}
