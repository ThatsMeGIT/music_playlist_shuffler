package musicplaylistshuffler.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainFrame extends JFrame {

    private MainActionHandler actionHandler = new MainActionHandler();

    private JTextField searchField;
    private JTable playlistTable;
    private JCheckBox shuffle;
    private JProgressBar progressBar;
    private JLabel playedDuration;
    private JLabel maxDuration;

    public MainFrame() {
        setTitle("Best music Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        URL iconUrl = MainFrame.class.getResource("/MusicPlaylistShufflerLogo.png");
        setIconImage(new ImageIcon(iconUrl).getImage());

        // implement worker fields
        //searchField = new JTextField();
        //playlistTable = new JTable();
        //shuffle = new JCheckBox();
        //progressBar = new JProgressBar();

        // North (Search bar & File Input)
        add(createTopBar(), BorderLayout.NORTH);

        // Center (Playlists)

        // South (Control Panel)
        add(createControlBar(), BorderLayout.SOUTH);


        pack();
        setVisible(true);
    }


    private JPanel createControlBar() {
        JPanel controlBar = new JPanel();
        controlBar.setLayout(new BorderLayout());

        JPanel controls = createControls();
        JPanel playBar = createPlayBar();

        controlBar.add(controls, BorderLayout.CENTER);
        controlBar.add(playBar, BorderLayout.SOUTH);

        return controlBar;
    }

    private JPanel createControls() {
        JPanel controls = new JPanel();

        JButton previous = new JButton("Previous");
        JButton pause = new JButton("Pause/Play");
        JButton next = new JButton("Next");


        previous.setActionCommand("previous");
        previous.addActionListener(actionHandler);
        pause.setActionCommand("pause/play");
        pause.addActionListener(actionHandler);
        next.setActionCommand("next");
        next.addActionListener(actionHandler);

        controls.add(previous, BorderLayout.WEST);
        controls.add(pause, BorderLayout.CENTER);
        controls.add(next, BorderLayout.EAST);

        return controls;
    }

    private JPanel createPlayBar() {
        JPanel playBar = new JPanel();
        playBar.setLayout(new BorderLayout());

        playedDuration = new JLabel("0:00");
        progressBar = new JProgressBar();
        maxDuration = new JLabel("3:12");

        playBar.add(playedDuration, BorderLayout.WEST);
        playBar.add(progressBar, BorderLayout.CENTER);
        playBar.add(maxDuration, BorderLayout.EAST);

        return playBar;
    }

    private JPanel createTopBar() {
        JPanel searchBar = new JPanel();
        JButton addPlaylist = new JButton("Add Playlist");
        searchBar.setLayout(new BorderLayout());

        addPlaylist.setActionCommand("new_playlist");
        addPlaylist.addActionListener(actionHandler);

        searchField = new JTextField();

        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(addPlaylist, BorderLayout.EAST);

        return searchBar;
    }

}
