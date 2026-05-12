package musicplaylistshuffler.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvUtils {
    private CsvUtils() {
    }

    public static int countRows(String path) throws IOException {
        return readLines(path).size();
    }

    public static List<String> readLines(String path) throws IOException {
        return Files.readAllLines(Path.of(path));
    }

    public static String[] splitCsvLine(String line, String delimiter) {
        return line.split(",");
    }

}
