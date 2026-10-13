package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerRestorationTest {
    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 0, 5, Integer.MAX_VALUE})
    void clearsPlaybackWhenSelectedSongIsMissing(int songIndex) {
        Playlist playlist = new Playlist("Favorites", List.of(song("Present")));
        Player player = new Player(42, null, List.of(playlist), 0, songIndex, Status.PLAYING);

        assertClearedAndSafeToNavigate(player);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 0, 5, Integer.MAX_VALUE})
    void clearsPlaybackWhenSelectedSongIsNotInPlaylist(int songIndex) {
        Playlist playlist = new Playlist("Favorites", List.of(song("Present")));
        Player player = new Player(42, song("Absent"), List.of(playlist), 0, songIndex, Status.PLAYING);

        assertClearedAndSafeToNavigate(player);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, 1, Integer.MAX_VALUE})
    void clearsPlaybackWhenPlaylistIndexIsInvalid(int playlistIndex) {
        Song selected = song("Present");
        Playlist playlist = new Playlist("Favorites", List.of(selected));
        Player player = new Player(42, selected, List.of(playlist), playlistIndex, 0, Status.PLAYING);

        assertEquals(0, player.getCurrentPlaylistIndex());
        assertClearedAndSafeToNavigate(player);
    }

    @Test
    void clearsPlaybackWhenPlaylistIsEmpty() {
        Playlist playlist = new Playlist("Empty", List.of());
        Player player = new Player(42, song("Absent"), List.of(playlist), 0, -2, Status.PLAYING);

        assertClearedAndSafeToNavigate(player);
    }

    @Test
    void clearsPlaybackWhenPlaylistsAreMissing() {
        assertClearedAndSafeToNavigate(new Player(42, song("Absent"), null, 0, -2, Status.PLAYING));
        assertClearedAndSafeToNavigate(new Player(42, song("Absent"), List.of(), 0, -2, Status.PLAYING));
    }

    @Test
    void preservesRepeatWhenClearingInvalidSongSelection() {
        Playlist playlist = new Playlist("Favorites", List.of(song("Present")));
        Player player = new Player(42, song("Absent"), List.of(playlist), 0, -2,
                Status.PLAYING, Loop.REPEAT_ON);

        assertTrue(playlist.isOnRepeat());
        assertEquals(Status.PAUSED, player.getStatus());
        assertEquals(0, player.getPlayedSeconds());
        assertNull(player.getCurrentSong());
        player.playPlaylist(playlist);
        player.skip();
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void recoversValidSongDespiteInvalidSongIndex() {
        Song selected = song("Present");
        Playlist playlist = new Playlist("Favorites", List.of(selected));
        Player player = new Player(42, song("Present"), List.of(playlist), 0, -2, Status.PLAYING);

        assertSame(selected, player.getCurrentSong());
        assertEquals(0, player.getCurrentSongIndex());
        assertEquals(42, player.getPlayedSeconds());
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void rejectsNullCollectionEntries() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player(0, null, Arrays.asList((Playlist) null), 0, 0, Status.PAUSED));
        assertThrows(IllegalArgumentException.class,
                () -> new Playlist("Invalid", Arrays.asList((Song) null)));
    }

    private static void assertClearedAndSafeToNavigate(Player player) {
        assertNull(player.getCurrentSong());
        assertEquals(0, player.getCurrentSongIndex());
        assertEquals(0, player.getPlayedSeconds());
        assertEquals(Status.PAUSED, player.getStatus());
        assertDoesNotThrow(player::tick);
        assertDoesNotThrow(player::skip);
        assertDoesNotThrow(player::previous);
        assertDoesNotThrow(player::toggleShuffle);
    }

    private static Song song(String title) {
        return new Song(title, "Artist", 60, "Pop");
    }
}
