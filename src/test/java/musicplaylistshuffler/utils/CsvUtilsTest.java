package musicplaylistshuffler.utils;

import musicplaylistshuffler.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void loadSongsFromCsvValidCsvReturnsSongs() throws Exception {

        Path csv = tempDir.resolve("songs.csv");

        Files.writeString(csv,
                """
                title;artist;duration;genre
                Song1;Artist1;180;Rock
                Song2;Artist2;240;Pop
                """);

        List<Song> songs = CsvUtils.loadSongsFromCsv(csv.toString());

        assertEquals(2, songs.size());
        assertEquals("Song1", songs.get(0).getTitle());
        assertEquals("Artist1", songs.get(0).getArtist());
        assertEquals(180, songs.get(0).getDuration());
        assertEquals("Rock", songs.get(0).getGenre());
    }

    @Test
    void loadSongsFromCsvInvalidColumnsReturnsOnlyValidSongs() throws Exception {

        Path csv = tempDir.resolve("songs.csv");

        Files.writeString(csv,
                """
                title;artist;duration;genre
                Song1;Artist1;180;Rock
                Song2;Artist2;240
                """);

        List<Song> songs = CsvUtils.loadSongsFromCsv(csv.toString());

        assertEquals(1, songs.size());
        assertEquals("Song1", songs.get(0).getTitle());
    }

    @Test
    void loadSongsFromCsvFileDoesNotExist() {
        assertThrows(Exception.class,
                () -> CsvUtils.loadSongsFromCsv(tempDir.resolve("gibtEsNicht.csv").toString()));
    }
}