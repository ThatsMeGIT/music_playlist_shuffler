package musicplaylistshuffler.utils;

import musicplaylistshuffler.model.Song;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    public static List<Song> loadSongsFromCsv(String path) {
        List<Song> songs = new ArrayList<>();
        try (Reader reader = new FileReader(path)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setDelimiter(';')
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);
            for (CSVRecord record : records) {
                Song song = new Song(record.get(0), record.get(1), Integer.parseInt(record.get(2)), record.get(3));
                songs.add(song);
            }

            return songs;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}
