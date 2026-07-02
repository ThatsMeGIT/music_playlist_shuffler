package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {

    @Test
    void shouldCreatePlaylistWithValidValues() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");

        Playlist playlist = new Playlist("Playlist1", List.of(song1));

        assertEquals("Playlist1", playlist.getName());
        assertEquals(1, playlist.getSongs().size());
        assertEquals("Title1", playlist.getSongs().get(0).getTitle());
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        Song song = new Song("Title1", "Artist1", 100, "Pop");

        assertThrows(IllegalArgumentException.class, () ->
                new Playlist(" ", List.of(song)));
    }

    @Test
    void shouldThrowWhenSongsIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Playlist("Playlist1", null));
    }

    @Test
    void shouldCalculateAverageSongLength() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");
        Song song2 = new Song("Title2", "Artist2", 150, "Rock");
        Song song3 = new Song("Title3", "Artist3", 200, "Jazz");
        Song song4 = new Song("Title4", "Artist4", 250, "Jazz");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2, song3, song4));

        assertEquals(175, playlist.averageSongLength());
    }

    @Test
    void shouldCalculatePlaylistTimeLength() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");
        Song song2 = new Song("Title2", "Artist2", 150, "Rock");
        Song song3 = new Song("Title3", "Artist3", 200, "Jazz");
        Song song4 = new Song("Title4", "Artist4", 250, "Jazz");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2, song3, song4));

        assertEquals(700, playlist.playlistTimeLength());
    }

    @Test
    void shouldSearchForTopGenre() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");
        Song song2 = new Song("Title2", "Artist2", 150, "Rock");
        Song song3 = new Song("Title3", "Artist3", 200, "Jazz");
        Song song4 = new Song("Title4", "Artist4", 250, "Jazz");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2, song3, song4));

        assertEquals("Jazz", playlist.topGenre());
    }


}