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

    @Test
    void shouldReturnOriginalListWhenShuffleOff() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");
        Song song2 = new Song("Title2", "Artist2", 150, "Rock");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2));

        playlist.shuffleOff();

        List<Song> result = playlist.getCurrentList();

        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
    }

    @Test
    void shouldReturnOriginalOrderAfterShuffleOff() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");
        Song song2 = new Song("Title2", "Artist2", 150, "Rock");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2));

        playlist.shuffleOn();
        playlist.shuffleOff();

        List<Song> result = playlist.getCurrentList();

        assertEquals("Title1", result.get(0).getTitle());
        assertEquals("Title2", result.get(1).getTitle());
    }

    @Test
    void shouldShuffleSongsButKeepAllElements() {
        Song song1 = new Song("Title1", "Artist1", 100, "Pop");
        Song song2 = new Song("Title2", "Artist2", 150, "Rock");
        Song song3 = new Song("Title3", "Artist3", 200, "Jazz");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2, song3));

        playlist.shuffleOn();

        List<Song> result = playlist.getCurrentList();

        assertEquals(3, result.size());

        assertTrue(result.contains(song1));
        assertTrue(result.contains(song2));
        assertTrue(result.contains(song3));
    }

    @Test
    void shouldReturnShuffledListWhenModeIsShuffle() {
        Song song1 = new Song("Title1", "Artist", 100, "Pop");
        Song song2 = new Song("Title2", "Artist", 120, "Rock");

        Playlist playlist = new Playlist("Playlist1", List.of(song1, song2));

        playlist.shuffleOn();

        assertEquals(Mode.SHUFFLED, playlist.getMode());
    }
}