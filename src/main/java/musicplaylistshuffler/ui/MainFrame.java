package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainFrame extends JFrame {

    private final Player player;
    private final MainActionHandler actionHandler;

    private JLabel songTitleLabel;
    private JLabel artistLabel;
    private JLabel statusLabel;

    public MainFrame(Player player) {
        this.player = player;
        this.actionHandler = new MainActionHandler(player, this);


        setTitle("Best music Player");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        URL iconUrl = MainFrame.class.getResource("/MusicPlaylistShufflerLogo.png");
        setIconImage(new ImageIcon(iconUrl).getImage());


        add(createSongInfoPanel(), BorderLayout.SOUTH);
        refreshPlayerView();

    }

    private  JPanel createSongInfoPanel(){
        JPanel panel = new JPanel(new GridLayout(1,3));

        songTitleLabel = new JLabel();
        artistLabel = new JLabel();
        statusLabel = new JLabel();

        panel.add(songTitleLabel);
        panel.add(artistLabel);
        panel.add(statusLabel);

        return panel;
    }

    public void refreshPlayerView(){
        songTitleLabel.setText(player.getCurrentSong().getTitle());
        songTitleLabel.setText(player.getCurrentSong().getArtist());
        songTitleLabel.setText(player.getStatus().toString());
    }

}
