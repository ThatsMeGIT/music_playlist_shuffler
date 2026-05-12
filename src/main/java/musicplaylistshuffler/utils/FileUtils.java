package musicplaylistshuffler.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    private FileUtils() {
    }

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean isReadable(String path) {
        File file = new File(path);
        return file.canRead();
    }

    public static boolean isWritable(String path) {
        File file = new File(path);
        return file.canWrite();
    }

    public static boolean checkExtension(String path, String extension) {
        return path.toLowerCase()
                .endsWith("." + extension.toLowerCase());
    }

    public static boolean createFile(String path) {
        File file = new File(path);
        try {

            return file.createNewFile();

        } catch (IOException e){
            System.out.println("Error: " + e);
            return false;
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

}
