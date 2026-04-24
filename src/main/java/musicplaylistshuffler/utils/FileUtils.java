package main.java.musicplaylistshuffler.utils;

import java.io.File;

public class FileUtils {
    private FileUtils(){}

    public static boolean fileExists(String path) {
        File file = new File(path);
        // log
        return file.exists();
    }

    public static boolean isFileReadable(String path) {
        File file = new File(path);
        // log
        return file.canRead();
    }

    public static boolean hasValidFormat(String path, String fileFormat) {
        // log
        return path.toLowerCase().endsWith(fileFormat);
    }


}
