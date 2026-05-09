package musicplaylistshuffler.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SongTest {

    @Test
    void shouldCreateSongWithValidValues() {
        Song song = new Song("Title", "Artist", 120, "Pop");

        assertEquals("Title", song.getTitle());
        assertEquals("Artist", song.getArtist());
        assertEquals(120, song.getDuration());
        assertEquals("Pop", song.getGenre());
    }

    @Test
    void shouldThrowWhenTitleIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Song(" ", "Artist", 120, "Pop"));
    }
}