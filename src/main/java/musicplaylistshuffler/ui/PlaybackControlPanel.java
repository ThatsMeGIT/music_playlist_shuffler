package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PlaybackControlPanel extends JPanel {

    private final Player player;
    private final MainActionHandler actionHandler;

    private JLabel songTitleLabel;
    private JLabel artistLabel;
    private JProgressBar progressBar;
    private JToggleButton shuffleButton;

    public PlaybackControlPanel(Player player, MainActionHandler actionHandler) {
        this.player = player;
        this.actionHandler = actionHandler;

        //setLayout(new BorderLayout(20, 0));
        setLayout(new GridLayout(1, 3, 20, 0));

        add(createSongInfoPanel());
        add(createPlaybackControlPanel());
        add(createShuffleControlPanel());

        startPlaybackTimer();
    }

    private JPanel createSongInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));

        JLabel coverLabel = new JLabel();

        URL coverUrl = getClass().getResource("/cover.png");
        if (coverUrl == null) {
            coverUrl = getClass().getResource("/icons/SongCover.png");
        }

        ImageIcon icon = new ImageIcon(coverUrl);
        Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        coverLabel.setIcon(new ImageIcon(scaledImage));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));

        songTitleLabel = new JLabel();
        songTitleLabel.setFont(songTitleLabel.getFont().deriveFont(Font.BOLD, 16f));

        artistLabel = new JLabel();
        artistLabel.setFont(artistLabel.getFont().deriveFont(14f));

        textPanel.add(songTitleLabel);
        textPanel.add(artistLabel);

        panel.add(coverLabel, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPlaybackControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 10));

        JButton previousButton = new JButton("<");
        previousButton.setActionCommand("previous");
        previousButton.addActionListener(actionHandler);

        JButton pausePlayButton = new JButton("⏸/▶");
        pausePlayButton.setActionCommand("pause/play");
        pausePlayButton.addActionListener(actionHandler);


        JButton nextButton = new JButton(">");
        nextButton.setActionCommand("skip");
        nextButton.addActionListener(actionHandler);

        progressBar = new JProgressBar(0, 100);
        progressBar.setEnabled(false);
        progressBar.setStringPainted(true);

        panel.add(previousButton, BorderLayout.WEST);
        panel.add(pausePlayButton, BorderLayout.CENTER);
        panel.add(nextButton, BorderLayout.EAST);
        panel.add(progressBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createShuffleControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));

        shuffleButton = new JToggleButton("\uD83D\uDD00");
        shuffleButton.setActionCommand("shuffle");
        shuffleButton.addActionListener(actionHandler);

        JSlider volumeSlider = new JSlider(0, 100, 50);

        panel.add(shuffleButton, BorderLayout.WEST);
        panel.add(volumeSlider, BorderLayout.CENTER);

        return panel;
    }

    public void refreshPlayerView() {
        songTitleLabel.setText(player.getCurrentSong().getTitle());
        artistLabel.setText(player.getCurrentSong().getArtist());
        shuffleButton.setSelected(player.getCurrentPlaylist().isShuffled());

        progressBar.setMaximum(player.getCurrentSong().getDuration());
        progressBar.setValue(player.getPlayedSeconds());

        progressBar.setString(TimeUtils.formatDuration(player.getPlayedSeconds()) + " / " + TimeUtils.formatDuration(player.getCurrentSong().getDuration())
        );
    }

    private void startPlaybackTimer() {
        Timer timer = new Timer(1000, event -> {
            player.tick();
            refreshPlayerView();
        });

        timer.start();
    }
}
