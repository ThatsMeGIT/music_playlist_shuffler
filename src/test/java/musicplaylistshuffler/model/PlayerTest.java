package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
                Status.PLAYING,
                Mode.SHUFFLED
        );

        assertEquals(30, player.getPlayedSeconds());
        assertEquals(song, player.getCurrentSong());
        assertEquals(1, player.getPlaylists().size());
        assertEquals(0, player.getCurrentPlaylistIndex());
        assertEquals(0, player.getCurrentSongIndex());
        assertEquals(Status.PLAYING, player.getStatus());
        assertEquals(Mode.SHUFFLED, player.getMode());
    }

    @Test
    void shouldDefaultStatusAndModeWhenMissing() {
        Player player = playerAtSongIndex(0, null, null);

        assertEquals(Status.PAUSED, player.getStatus());
        assertEquals(Mode.NORMAL, player.getMode());
    }

    @Test
    void shouldPlayAndPause() {
        Player player = playerAtSongIndex(0, Status.PAUSED, Mode.NORMAL);

        player.play();
        assertEquals(Status.PLAYING, player.getStatus());

        player.pause();
        assertEquals(Status.PAUSED, player.getStatus());
    }

    @Test
    void shouldSkipToNextSongAndResetPlayedSeconds() {
        Player player = playerAtSongIndex(0, Status.PAUSED, Mode.NORMAL);

        player.skip();

        assertEquals(1, player.getCurrentSongIndex());
        assertEquals("Second Song", player.getCurrentSong().getTitle());
        assertEquals(0, player.getPlayedSeconds());
    }

    @Test
    void shouldSkipFromLastSongToFirstSong() {
        Player player = playerAtSongIndex(2, Status.PAUSED, Mode.NORMAL);

        player.skip();

        assertEquals(0, player.getCurrentSongIndex());
        assertEquals("First Song", player.getCurrentSong().getTitle());
    }

    @Test
    void shouldGoToPreviousSongAndResetPlayedSeconds() {
        Player player = playerAtSongIndex(2, Status.PAUSED, Mode.NORMAL);

        player.previous();

        assertEquals(1, player.getCurrentSongIndex());
        assertEquals("Second Song", player.getCurrentSong().getTitle());
        assertEquals(0, player.getPlayedSeconds());
    }

    @Test
    void shouldGoFromSecondSongToFirstSong() {
        Player player = playerAtSongIndex(1, Status.PAUSED, Mode.NORMAL);

        player.previous();

        assertEquals(0, player.getCurrentSongIndex());
        assertEquals("First Song", player.getCurrentSong().getTitle());
    }

    @Test
    void shouldGoFromFirstSongToLastSong() {
        Player player = playerAtSongIndex(0, Status.PAUSED, Mode.NORMAL);

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

    private static Player playerAtSongIndex(int currentSongIndex, Status status, Mode mode) {
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
                status,
                mode
        );
    }

    private static Player playerWithPlaylist(Playlist playlist) {
        return new Player(
                42,
                null,
                List.of(playlist),
                0,
                0,
                Status.PAUSED,
                Mode.NORMAL
        );
    }

    private static Song song(String title) {
        return new Song(title, "Artist", 180, "Pop");
    }
}