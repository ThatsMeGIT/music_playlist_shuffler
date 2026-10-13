package musicplaylistshuffler.ui;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import org.junit.jupiter.api.Test;

import javax.swing.SwingUtilities;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}
