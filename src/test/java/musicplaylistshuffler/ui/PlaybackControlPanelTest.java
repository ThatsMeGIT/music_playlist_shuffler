package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import javax.swing.AbstractButton;
import java.awt.Component;
import java.awt.Container;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaybackControlPanelTest {
    @Test
    void refreshesWithoutPlaylistOrSong() {
        Player player = new Player(0, null, List.of(), 0, 0, Status.PAUSED);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {
            PlaybackControlPanel panel = new PlaybackControlPanel(player, null);
            panel.refreshPlayerView();
        }));
    }

    @Test
    void refreshesAfterDeletingTheActivePlaylist() {
        Song song = new Song("Title", "Artist", 60, "Pop");
        Playlist playlist = new Playlist("Favorites", List.of(song));
        Player player = new Player(30, song, List.of(playlist), 0, 0, Status.PLAYING);
        assertDoesNotThrow(() -> SwingUtilities.invokeAndWait(() -> {
            PlaybackControlPanel panel = new PlaybackControlPanel(player, null);
            panel.refreshPlayerView();
            player.removePlaylist(playlist);
            panel.refreshPlayerView();
        }));
    }
    @Test
    void playButtonReflectsPlaybackStateWithoutChangingItsAction() throws Exception {
        Song song = new Song("Title", "Artist", 60, "Pop");
        Playlist playlist = new Playlist("Favorites", List.of(song));
        Player player = new Player(0, song, List.of(playlist), 0, 0, Status.PLAYING);
        SwingUtilities.invokeAndWait(() -> {
            PlaybackControlPanel panel = new PlaybackControlPanel(player, null);
            AbstractButton button = findButton(panel, "pause/play");
            assertEquals("Pause", button.getText());
            assertEquals("Pause", button.getAccessibleContext().getAccessibleName());
            player.pause();
            panel.refreshPlayerView();
            assertEquals("Play", button.getText());
            assertEquals("pause/play", button.getActionCommand());
            assertEquals("Play", button.getAccessibleContext().getAccessibleName());
        });
    }

    @Test
    void playbackControlsFitAtMinimumWindowWidthWithLongMetadata() throws Exception {
        Song song = new Song("A very long song title ".repeat(20), "A very long artist ".repeat(20), 60, "Pop");
        Playlist playlist = new Playlist("Favorites", List.of(song));
        Player player = new Player(0, song, List.of(playlist), 0, 0, Status.PAUSED);
        SwingUtilities.invokeAndWait(() -> {
            PlaybackControlPanel panel = new PlaybackControlPanel(player, null);
            panel.setSize(880, panel.getPreferredSize().height);
            layout(panel);
            for (String action : List.of("previous", "pause/play", "skip", "shuffle", "repeat")) {
                AbstractButton button = findButton(panel, action);
                Container parent = button.getParent();
                assertTrue(button.getWidth() > 0 && button.getHeight() > 0, action);
                assertTrue(button.getX() >= 0 && button.getY() >= 0, action);
                assertTrue(button.getX() + button.getWidth() <= parent.getWidth(), action);
                assertTrue(button.getY() + button.getHeight() <= parent.getHeight(), action);
                assertTrue(button.isFocusPainted(), action);
            }
            assertEquals(Status.PAUSED, player.getStatus());
            assertEquals(song, player.getCurrentSong());
        });
    }

    @Test
    void iconOnlyNavigationKeepsAccessibleNamesAndTooltips() throws Exception {
        Player player = new Player(0, null, List.of(), 0, 0, Status.PAUSED);
        SwingUtilities.invokeAndWait(() -> {
            PlaybackControlPanel panel = new PlaybackControlPanel(player, null);
            for (String action : List.of("previous", "skip")) {
                AbstractButton button = findButton(panel, action);
                assertTrue(button.getText().isEmpty());
                assertTrue(!button.getAccessibleContext().getAccessibleName().isBlank());
                assertTrue(!button.getToolTipText().isBlank());
            }
        });
    }

    private static void layout(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) {
            if (child instanceof Container nested) {
                layout(nested);
            }
        }
    }

    private static AbstractButton findButton(Container container, String action) {
        for (Component child : container.getComponents()) {
            if (child instanceof AbstractButton button && action.equals(button.getActionCommand())) {
                return button;
            }
            if (child instanceof Container nested) {
                AbstractButton button = findButton(nested, action);
                if (button != null) {
                    return button;
                }
            }
        }
        return null;
    }
}
