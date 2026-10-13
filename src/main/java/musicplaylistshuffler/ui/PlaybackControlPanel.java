package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
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
    private JToggleButton repeatButton;
    private final Timer playbackTimer;

    public PlaybackControlPanel(Player player, MainActionHandler actionHandler) {
        this.player = player;
        this.actionHandler = actionHandler;

        setLayout(new GridLayout(1, 4, 20, 0));

        add(createSongInfoPanel());
        add(createRepeatControlPanel());
        add(createPlaybackControlPanel());
        add(createShuffleControlPanel());

        playbackTimer = new Timer(1000, event -> {
            player.tick();
            refreshPlayerView();
        });
    }

    private JPanel createSongInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));

        JLabel coverLabel = new JLabel();

        URL coverUrl = getClass().getResource("/cover.png");
        if (coverUrl == null) {
            coverUrl = getClass().getResource("/icons/SongCover.png");
        }

        if (coverUrl != null) {
            ImageIcon icon = new ImageIcon(coverUrl);
            Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            coverLabel.setIcon(new ImageIcon(scaledImage));
        }

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

    private JPanel createRepeatControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));

        repeatButton = new JToggleButton("><");
        repeatButton.setActionCommand("repeat");
        repeatButton.addActionListener(actionHandler);

        panel.add(repeatButton, BorderLayout.EAST);

        return panel;
    }

    public void refreshPlayerView() {
        Song song = player.getCurrentSong();
        Playlist playlist = player.getCurrentPlaylist();
        songTitleLabel.setText(song != null ? song.getTitle() : "No song selected");
        artistLabel.setText(song != null ? song.getArtist() : "");
        shuffleButton.setSelected(playlist != null && playlist.isShuffled());
        repeatButton.setSelected(playlist != null && playlist.isOnRepeat());

        int duration = song != null ? song.getDuration() : 0;
        int playedSeconds = song != null ? player.getPlayedSeconds() : 0;
        progressBar.setMaximum(duration);
        progressBar.setValue(playedSeconds);
        progressBar.setString(TimeUtils.formatDuration(playedSeconds)
                + " / " + TimeUtils.formatDuration(duration));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        playbackTimer.start();
    }

    @Override
    public void removeNotify() {
        playbackTimer.stop();
        super.removeNotify();
    }
}
