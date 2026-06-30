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
    private static String DELIMITER = ";";
    private static int EXPECTED_COLUMNS = 4;

    public static List<Song> loadSongsFromCsv(String path) {
        List<Song> songs = new ArrayList<>();
        try (Reader reader = new FileReader(path)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .builder()
                    .setDelimiter(DELIMITER)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);
            for (CSVRecord record : records) {
                if (record.size() > EXPECTED_COLUMNS) {
                    System.out.println(record.getRecordNumber() + "Line is invalid");
                    return songs;
                } else {
                    songs.add(parseLine(record));
                }
            }
            return songs;
        } catch (Exception e){
            System.out.println("Error with file " + e);
            return songs;
        }

    }


    private static Song parseLine(CSVRecord record) {
        String title = record.get(0);
        String artist = record.get(1);
        int duration = Integer.parseInt(record.get(2));
        String genre = record.get(3);

        return new Song(title, artist, duration, genre);
    }
}


