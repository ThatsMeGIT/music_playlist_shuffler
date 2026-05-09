package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {

    @Test
    void shouldCreatePlaylistWithValidValues() {
        Song song = new Song("Title", "Artist", 120, "Pop");
        Playlist playlist = new Playlist("Name", List.of(song));

        assertEquals("Name", playlist.getName());
        assertEquals(1, playlist.getSongs().size());
        assertEquals("Title", playlist.getSongs().get(0).getTitle());
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        Song song = new Song("Title", "Artist", 120, "Pop");

        assertThrows(IllegalArgumentException.class, () ->
                new Playlist(" ", List.of(song)));
    }

    @Test
    void shouldThrowWhenSongsIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Playlist("Name", null));
    }
}