package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainFrame extends JFrame {

    private final Player player;
    private final MainActionHandler actionHandler;
    private final PlaybackControlPanel playbackControlPanel;

    public MainFrame(Player player) {
        this.player = player;
        this.actionHandler = new MainActionHandler(player, this);
        this.playbackControlPanel = new PlaybackControlPanel(player, actionHandler);


        setTitle("Best music Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        System.out.println(System.getProperty("user.dir"));
        URL iconUrl = MainFrame.class.getResource("/icons/MusicPlaylistShufflerLogo.png");
        if (iconUrl != null) {
            setIconImage(new ImageIcon(iconUrl).getImage());
        }

        add(playbackControlPanel, BorderLayout.SOUTH);
        setJMenuBar(createMenuBar());

        setVisible(true);
        refreshPlayerView();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem addPlaylistItem = new JMenuItem("Add Playlist");
        addPlaylistItem.setActionCommand("addPlaylist");
        addPlaylistItem.addActionListener(actionHandler);

        JMenuItem shuffleItem = new JMenuItem("Shuffle");
        shuffleItem.setActionCommand("shuffle");
        shuffleItem.addActionListener(actionHandler);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setActionCommand("exit");
        exitItem.addActionListener(actionHandler);

        fileMenu.add(addPlaylistItem);
        fileMenu.addSeparator();
        fileMenu.add(shuffleItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        return menuBar;
    }

    public void refreshPlayerView() {
        playbackControlPanel.refreshPlayerView();
    }


}
