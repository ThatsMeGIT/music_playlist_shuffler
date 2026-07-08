package musicplaylistshuffler.utils;

import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    private static String DELIMITER = ";";
    private static int EXPECTED_COLUMNS = 4;

    private CsvUtils(){}

    public static List<Song> loadSongsFromCsv(String path) throws Exception {
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
                if (record.size() != EXPECTED_COLUMNS) {
                    System.out.println(record.getRecordNumber() + "Line is invalid");
                    return songs;
                } else {
                    songs.add(parseLine(record));
                }
            }
        }

        return songs;
    }


    private static Song parseLine(CSVRecord record) throws Exception {
        String title = record.get(0);
        String artist = record.get(1);
        int duration = Integer.parseInt(record.get(2));
        String genre = record.get(3);

        return new Song(title, artist, duration, genre);
    }

    public static void exportPlaylistToCsv(Playlist playlist) throws Exception {
        String fileName = playlist.getName();
        fileName.trim();
        fileName = fileName.replace(" ", "_");
        fileName = fileName + ".csv";
        List<Song> songs = playlist.getSongs();

        Writer writer = new FileWriter(fileName);
        writer.write("Title;Artist;Duration[s];Genre\n");
        for (Song song : songs) {
            writer.write(song.getTitle() + DELIMITER + song.getArtist() + DELIMITER + song.getDuration() + DELIMITER + song.getGenre() + "\n");

        }

        writer.close();
    }
}


