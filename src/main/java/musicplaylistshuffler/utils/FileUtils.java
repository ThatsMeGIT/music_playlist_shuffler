package main.java.musicplaylistshuffler.utils;

import java.io.File;

public class FileUtils {

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean isFileReadable(String path) {
        File file = new File(path);
        return file.canRead();
    }

    public static boolean hasValidFormat(String path, String fileFormat) {
        return path.toLowerCase().endsWith(fileFormat);
    }


}
