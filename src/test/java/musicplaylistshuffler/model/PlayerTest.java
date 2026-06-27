package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldCreatePlayerWithValidValues() {
        Song song = new Song("Title", "Artist", 120, "Pop");
        Playlist playlist = new Playlist("Name", List.of(song));
        Player player = new Player(120, song, List.of(playlist));

        assertEquals(120, player.getPlayedSeconds());
        assertEquals("Title", player.getCurrentSong().getTitle());
        assertEquals("Name", player.getPlaylists().get(0).getName());
        // Mode and Status Changes here
    }

    @Test
    void shouldThrowWhenPlayedSecondsIsNotPositive() {
        Song song = new Song("Title", "Artist", 120, "Pop");
        Playlist playlist = new Playlist("Name", List.of(song));

        assertThrows(IllegalArgumentException.class, () ->
                new Player(0, song, List.of(playlist)));
    }

    @Test
    void shouldThrowWhenCurrentSongIsNull() {
        Song song = new Song("Title", "Artist", 120, "Pop");
        Playlist playlist = new Playlist("Name", List.of(song));

        assertThrows(IllegalArgumentException.class, () ->
                new Player(120, null, List.of(playlist)));
    }

    @Test
    void shouldThrowWhenPlaylistsAreNull() {
        Song song = new Song("Title", "Artist", 120, "Pop");
        Playlist playlist = new Playlist("Name", List.of(song));

        assertThrows(IllegalArgumentException.class, () ->
                new Player(120, song, null));
    }

}