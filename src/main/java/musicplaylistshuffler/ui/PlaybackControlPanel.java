package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.utils.TimeUtils;

import javax.swing.*;
import java.awt.*;

public class PlaybackControlPanel extends JPanel {
    private final Player player;
    private final MainActionHandler actionHandler;

    private JLabel songTitleLabel;
    private JLabel artistLabel;
    private JProgressBar progressBar;
    private JButton pausePlayButton;
    private JToggleButton shuffleButton;
    private JToggleButton repeatButton;
    private final Timer playbackTimer;

    public PlaybackControlPanel(Player player, MainActionHandler actionHandler) {
        this.player = player;
        this.actionHandler = actionHandler;

        setLayout(new BorderLayout(UiStyle.PADDING, UiStyle.GAP));
        setBackground(UiStyle.SURFACE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UiStyle.BORDER),
                BorderFactory.createEmptyBorder(16, UiStyle.PADDING, 16, UiStyle.PADDING)));

        JPanel songInfo = createSongInfoPanel();
        songInfo.setPreferredSize(new Dimension(250, 64));
        add(songInfo, BorderLayout.WEST);
        add(createPlaybackControlPanel(), BorderLayout.CENTER);
        add(createModeControlPanel(), BorderLayout.EAST);

        playbackTimer = new Timer(1000, event -> {
            player.tick();
            refreshPlayerView();
        });
        refreshPlayerView();
    }

    private JPanel createSongInfoPanel() {
        JPanel panel = UiStyle.panel(new BorderLayout(UiStyle.GAP, 0), UiStyle.SURFACE);
        JLabel coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(64, 64));
        coverLabel.setIcon(new PixelIcon(PixelIcon.Kind.COVER, 64));
        JPanel textPanel = UiStyle.panel(new GridLayout(2, 1, 0, 4), UiStyle.SURFACE);
        songTitleLabel = UiStyle.heading("", 16f);
        artistLabel = UiStyle.secondary("");
        textPanel.add(songTitleLabel);
        textPanel.add(artistLabel);
        // Long metadata truncates in the available space; tooltips retain the full text.
        textPanel.setMinimumSize(new Dimension(0, 64));
        panel.add(coverLabel, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPlaybackControlPanel() {
        JPanel panel = UiStyle.panel(new BorderLayout(UiStyle.GAP, UiStyle.GAP), UiStyle.SURFACE);
        JPanel buttons = UiStyle.panel(new FlowLayout(FlowLayout.CENTER, 8, 0), UiStyle.SURFACE);
        JButton previousButton = new JButton("");
        previousButton.setActionCommand("previous");
        previousButton.addActionListener(actionHandler);
        previousButton.setIcon(new PixelIcon(PixelIcon.Kind.PREVIOUS, 16));
        UiStyle.button(previousButton, "Go to the previous song", false);

        pausePlayButton = new JButton("Play");
        pausePlayButton.setActionCommand("pause/play");
        pausePlayButton.addActionListener(actionHandler);
        UiStyle.button(pausePlayButton, "Play or pause the current song", true);
        previousButton.setPreferredSize(new Dimension(40, 40));
        pausePlayButton.setPreferredSize(new Dimension(100, 40));

        JButton nextButton = new JButton("");
        nextButton.setActionCommand("skip");
        nextButton.addActionListener(actionHandler);
        nextButton.setIcon(new PixelIcon(PixelIcon.Kind.NEXT, 16));
        UiStyle.button(nextButton, "Go to the next song", false);
        nextButton.setPreferredSize(previousButton.getPreferredSize());
        buttons.add(previousButton);
        buttons.add(pausePlayButton);
        buttons.add(nextButton);

        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new PixelProgressUi());
        progressBar.setOpaque(false);
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setEnabled(false);
        progressBar.setStringPainted(true);
        progressBar.setFont(UiStyle.BODY_FONT);
        progressBar.setPreferredSize(new Dimension(0, 24));
        progressBar.getAccessibleContext().setAccessibleName("Playback progress");
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createModeControlPanel() {
        JPanel panel = UiStyle.panel(new BorderLayout(0, 8), UiStyle.SURFACE);
        panel.setPreferredSize(new Dimension(220, 76));
        panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, UiStyle.BORDER));
        JPanel modes = UiStyle.panel(new FlowLayout(FlowLayout.RIGHT, 8, 0), UiStyle.SURFACE);
        shuffleButton = new JToggleButton("Shuffle");
        shuffleButton.setActionCommand("shuffle");
        shuffleButton.addActionListener(actionHandler);
        shuffleButton.setIcon(new PixelIcon(PixelIcon.Kind.SHUFFLE, 16));
        UiStyle.button(shuffleButton, "Toggle shuffle for the current playlist", false);

        repeatButton = new JToggleButton("Repeat");
        repeatButton.setActionCommand("repeat");
        repeatButton.addActionListener(actionHandler);
        repeatButton.setIcon(new PixelIcon(PixelIcon.Kind.REPEAT, 16));
        UiStyle.button(repeatButton, "Toggle repeat for the current playlist", false);
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBackground(UiStyle.SURFACE);
        volumeSlider.setPreferredSize(new Dimension(100, 32));
        volumeSlider.setToolTipText("Volume preview (audio playback is not yet available)");
        volumeSlider.getAccessibleContext().setAccessibleName("Volume preview");
        shuffleButton.setFont(UiStyle.BODY_FONT.deriveFont(14f));
        repeatButton.setFont(UiStyle.BODY_FONT.deriveFont(14f));
        shuffleButton.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        repeatButton.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        modes.add(shuffleButton);
        modes.add(repeatButton);
        panel.add(modes, BorderLayout.NORTH);
        JPanel volume = UiStyle.panel(new FlowLayout(FlowLayout.RIGHT, 8, 0), UiStyle.SURFACE);
        volume.add(volumeSlider);
        panel.add(volume, BorderLayout.SOUTH);
        return panel;
    }

    public void refreshPlayerView() {
        Song song = player.getCurrentSong();
        Playlist playlist = player.getCurrentPlaylist();
        songTitleLabel.setText(song != null ? song.getTitle() : "No song selected");
        songTitleLabel.setToolTipText(songTitleLabel.getText());
        artistLabel.setText(song != null ? song.getArtist() : "Choose a playlist to get started");
        artistLabel.setToolTipText(artistLabel.getText());
        shuffleButton.setSelected(playlist != null && playlist.isShuffled());
        repeatButton.setSelected(playlist != null && playlist.isOnRepeat());
        boolean playing = player.getStatus() == Status.PLAYING;
        pausePlayButton.setText(playing ? "Pause" : "Play");
        pausePlayButton.setIcon(new PixelIcon(playing ? PixelIcon.Kind.PAUSE : PixelIcon.Kind.PLAY, 16));
        pausePlayButton.setToolTipText(playing ? "Pause playback" : "Start playback");
        pausePlayButton.getAccessibleContext().setAccessibleName(pausePlayButton.getText());
        pausePlayButton.getAccessibleContext().setAccessibleDescription(pausePlayButton.getToolTipText());

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
