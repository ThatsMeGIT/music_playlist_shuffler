package main.java.musicplaylistshuffler.utils;

import java.io.File;

public class FileUtils {

    public boolean fileExists(String path) {
        File file = new File(path);
        // log
        return file.exists();
    }

    public boolean isFileReadable(String path) {
        File file = new File(path);
        // log
        return file.canRead();
    }

    public boolean hasValidFormat(String path, String fileFormat) {
        // log
        return path.toLowerCase().endsWith(fileFormat);
    }


}
