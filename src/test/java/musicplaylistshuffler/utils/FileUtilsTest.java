package musicplaylistshuffler.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReturnTrueWhenPathExists() throws IOException {
        Path existingFile = Files.createFile(tempDir.resolve("existing.txt"));

        assertTrue(FileUtils.exists(existingFile.toString()));
    }

    @Test
    void shouldReturnFalseWhenPathDoesNotExist() {
        Path missingFile = tempDir.resolve("missing.txt");

        assertFalse(FileUtils.exists(missingFile.toString()));
    }

    @Test
    void shouldReturnTrueWhenPathIsReadable() throws IOException {
        Path readableFile = Files.createFile(tempDir.resolve("readable.txt"));

        assertTrue(FileUtils.isReadable(readableFile.toString()));
    }

    @Test
    void shouldReturnFalseWhenPathIsNotReadableBecauseItDoesNotExist() {
        Path missingFile = tempDir.resolve("missing.txt");

        assertFalse(FileUtils.isReadable(missingFile.toString()));
    }

    @Test
    void shouldMatchExtensionCaseInsensitively() {
        assertTrue(FileUtils.checkExtension("playlist.JSON", "json"));
    }

    @Test
    void shouldReturnFalseForNonMatchingExtension() {
        assertFalse(FileUtils.checkExtension("playlist.csv", "json"));
    }

    @Test
    void shouldCreateFileWhenItDoesNotExist() {
        Path newFile = tempDir.resolve("new-file.txt");

        boolean created = FileUtils.createFile(newFile.toString());

        assertTrue(created);
        assertTrue(Files.exists(newFile));
    }

    @Test
    void shouldReturnFalseWhenCreatingFileThatAlreadyExists() throws IOException {
        Path existingFile = Files.createFile(tempDir.resolve("already-there.txt"));

        boolean created = FileUtils.createFile(existingFile.toString());

        assertFalse(created);
    }

    @Test
    void shouldDeleteExistingFile() throws IOException {
        Path existingFile = Files.createFile(tempDir.resolve("delete-me.txt"));

        boolean deleted = FileUtils.deleteFile(existingFile.toString());

        assertTrue(deleted);
        assertFalse(Files.exists(existingFile));
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingFile() {
        Path missingFile = tempDir.resolve("missing.txt");

        assertFalse(FileUtils.deleteFile(missingFile.toString()));
    }
}