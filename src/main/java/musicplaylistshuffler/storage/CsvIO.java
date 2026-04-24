package main.java.musicplaylistshuffler.storage;

import main.java.musicplaylistshuffler.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CsvIO {

    public void printCsv(String path) {
        if (FileUtils.fileExists(path) && FileUtils.isFileReadable(path) && FileUtils.hasValidFormat(path, "csv")){

            try (Scanner scanner = new Scanner(new File(path))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                //log
                System.out.println(e);
            }
        }
    }
}
