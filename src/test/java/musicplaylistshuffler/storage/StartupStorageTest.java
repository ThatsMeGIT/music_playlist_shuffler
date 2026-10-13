package musicplaylistshuffler.storage;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartupStorageTest {
    @TempDir
    Path tempDir;

    @Test
    void restoresSavedPlayerInsteadOfSamples() throws Exception {
        Song song = new Song("Saved Song", "Artist", 120, "Pop");
        Playlist playlist = new Playlist("Saved Playlist", List.of(song));
        Player saved = new Player(30, song, List.of(playlist), 0, 0, Status.PAUSED);
        Path path = tempDir.resolve("player.json");
        assertTrue(StorageService.savePlayerToJson(saved, path.toString()));
        String original = Files.readString(path);
        Player fallback = fallback();

        StorageService.StartupState startup = StorageService.loadPlayerForStartup(path.toString(), fallback);

        assertNotSame(fallback, startup.player());
        assertEquals("Saved Playlist", startup.player().getCurrentPlaylist().getName());
        assertEquals("Saved Song", startup.player().getCurrentSong().getTitle());
        assertEquals(30, startup.player().getPlayedSeconds());
        assertTrue(startup.saveAllowed());
        assertEquals(original, Files.readString(path));
    }

    @Test
    void allowsSavingFallbackWhenFileDoesNotExist() {
        Path path = tempDir.resolve("missing.json");
        Player fallback = fallback();

        StorageService.StartupState startup = StorageService.loadPlayerForStartup(path.toString(), fallback);

        assertSame(fallback, startup.player());
        assertTrue(startup.saveAllowed());
        assertFalse(Files.exists(path));
    }

    @Test
    void protectsExistingFileWhenLoadingFails() throws Exception {
        Path path = tempDir.resolve("broken.json");
        String original = "{broken JSON containing saved data";
        Files.writeString(path, original);
        Player fallback = fallback();

        StorageService.StartupState startup = StorageService.loadPlayerForStartup(path.toString(), fallback);

        assertSame(fallback, startup.player());
        assertFalse(startup.saveAllowed());
        assertEquals(original, Files.readString(path));
    }

    @Test
    void protectsFileContainingInvalidCollectionEntries() throws Exception {
        Path path = tempDir.resolve("invalid.json");
        String original = "{\"playlists\":[null],\"status\":\"PLAYING\"}";
        Files.writeString(path, original);

        StorageService.StartupState startup = StorageService.loadPlayerForStartup(path.toString(), fallback());

        assertFalse(startup.saveAllowed());
        assertEquals(original, Files.readString(path));
    }

    @Test
    void disablesSavingForInvalidOrDirectoryPaths() {
        Player fallback = fallback();
        assertFalse(StorageService.loadPlayerForStartup(null, fallback).saveAllowed());
        assertFalse(StorageService.loadPlayerForStartup("", fallback).saveAllowed());
        assertFalse(StorageService.loadPlayerForStartup("\u0000", fallback).saveAllowed());
        assertFalse(StorageService.loadPlayerForStartup(tempDir.toString(), fallback).saveAllowed());
    }

    private static Player fallback() {
        return new Player(0, null, List.of(), 0, 0, Status.PAUSED);
    }
}
