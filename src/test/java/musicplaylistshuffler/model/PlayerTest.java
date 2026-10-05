package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PlayerTest {

    @Test
    void shouldCreatePlayerWithValidValues() {
        Song song = song("First Song");
        Playlist playlist = new Playlist("Favorites", List.of(song));

        Player player = new Player(
                30,
                song,
                List.of(playlist),
                0,
                0,
                Status.PLAYING
        );

        assertEquals(30, player.getPlayedSeconds());
        assertEquals(song, player.getCurrentSong());
        assertEquals(1, player.getPlaylists().size());
        assertEquals(0, player.getCurrentPlaylistIndex());
        assertEquals(0, player.getCurrentSongIndex());
        assertEquals(Status.PLAYING, player.getStatus());
    }

    @Test
    void shouldDefaultStatusAndModeWhenMissing() {
        Player player = playerAtSongIndex(0, null);

        assertEquals(Status.PAUSED, player.getStatus());
    }

    @Test
    void shouldPlayAndPause() {
        Player player = playerAtSongIndex(0, Status.PAUSED);

        player.play();
        assertEquals(Status.PLAYING, player.getStatus());

        player.pause();
        assertEquals(Status.PAUSED, player.getStatus());
    }

    @Test
    void shouldSkipToNextSongAndResetPlayedSeconds() {
        Player player = playerAtSongIndex(0, Status.PAUSED);

        player.skip();

        assertEquals(1, player.getCurrentSongIndex());
        assertEquals("Second Song", player.getCurrentSong().getTitle());
        assertEquals(0, player.getPlayedSeconds());
    }

    @Test
    void shouldSkipFromLastSongToFirstSong() {
        Player player = playerAtSongIndex(2, Status.PAUSED);

        player.skip();

        assertEquals(0, player.getCurrentSongIndex());
        assertEquals("First Song", player.getCurrentSong().getTitle());
    }

    @Test
    void shouldGoToPreviousSongAndResetPlayedSeconds() {
        Player player = playerAtSongIndex(2, Status.PAUSED);

        player.previous();

        assertEquals(1, player.getCurrentSongIndex());
        assertEquals("Second Song", player.getCurrentSong().getTitle());
        assertEquals(0, player.getPlayedSeconds());
    }

    @Test
    void shouldGoFromSecondSongToFirstSong() {
        Player player = playerAtSongIndex(1, Status.PAUSED);

        player.previous();

        assertEquals(0, player.getCurrentSongIndex());
        assertEquals("First Song", player.getCurrentSong().getTitle());
    }

    @Test
    void shouldGoFromFirstSongToLastSong() {
        Player player = playerAtSongIndex(0, Status.PAUSED);

        player.previous();

        assertEquals(2, player.getCurrentSongIndex());
        assertEquals("Third Song", player.getCurrentSong().getTitle());
    }

    @Test
    void shouldClearCurrentSongWhenSkippingEmptyPlaylist() {
        Player player = playerWithPlaylist(new Playlist("Empty", List.of()));

        player.skip();

        assertNull(player.getCurrentSong());
        assertEquals(0, player.getPlayedSeconds());
    }

    @Test
    void shouldClearCurrentSongWhenGoingPreviousInEmptyPlaylist() {
        Player player = playerWithPlaylist(new Playlist("Empty", List.of()));

        player.previous();

        assertNull(player.getCurrentSong());
        assertEquals(0, player.getPlayedSeconds());
    }

    @Test
    void shouldTogglePlaylistRepeat() {
        Playlist playlist = new Playlist("Favorites", List.of(song("First Song")));
        Player player = playerWithPlaylist(playlist);

        player.repeatAll();
        assertTrue(playlist.isOnRepeat());
        player.repeatAll();
        assertFalse(playlist.isOnRepeat());
    }

    @Test
    void shouldIgnoreRepeatWhenNoPlaylistExists() {
        Player player = new Player(0, null, List.of(), 0, 0, Status.PAUSED);
        assertDoesNotThrow(player::repeatAll);
    }

    @Test
    void shouldRemoveOnlySelectedPlaylist() {
        Playlist first = new Playlist("First", List.of(song("Song")));
        Playlist second = new Playlist("Second", List.of(song("Song")));
        Player player = new Player(0, null, new ArrayList<>(List.of(first, second)),
                0, 0, Status.PAUSED);

        player.removePlaylist(first);
        assertEquals(List.of(second), player.getPlaylists());
    }

    private static Player playerAtSongIndex(int currentSongIndex, Status status) {
        List<Song> songs = List.of(
                song("First Song"),
                song("Second Song"),
                song("Third Song")
        );
        Playlist playlist = new Playlist("Favorites", songs);

        return new Player(
                42,
                songs.get(currentSongIndex),
                List.of(playlist),
                0,
                currentSongIndex,
                status
        );
    }

    private static Player playerWithPlaylist(Playlist playlist) {
        return new Player(
                42,
                null,
                List.of(playlist),
                0,
                0,
                Status.PAUSED
        );
    }

    private static Song song(String title) {
        return new Song(title, "Artist", 180, "Pop");
    }
}