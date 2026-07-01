package musicplaylistshuffler.utils;

import musicplaylistshuffler.model.Song;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void loadSongsFromCsvWithValidValues() throws Exception {
        Path csvFile = tempDir.resolve("test.csv");

        Files.writeString(csvFile,
                "Title;Artist;Duration[s];Genre\n" +
                        "Bohemian Rhapsody;Queen;355;Rock\n" +
                        "Billie Jean;Michael Jackson;290;Pop\n" +
                        "Stairway to Heaven;Led Zeppelin;475;Rock");


        List<Song> songs = CsvUtils.loadSongsFromCsv(csvFile.toString());

        assertEquals(3, songs.size());

        assertEquals("Bohemian Rhapsody", songs.get(0).getTitle());
        assertEquals("Queen", songs.get(0).getArtist());
        assertEquals(355, songs.get(0).getDuration());
        assertEquals("Rock", songs.get(0).getGenre());
    }

    @Test
    void shouldReturnEmptyListWhenDurationIsInvalid() throws IOException {
        Path csvFile = tempDir.resolve("invalid-duration.csv");

        Files.writeString(csvFile, """
            Title;Artist;Duration[s];Genre
            Broken Song;Unknown;abc;Rock
            """);

        List<Song> songs = CsvUtils.loadSongsFromCsv(csvFile.toString());

        assertEquals(0, songs.size());
    }
}
