package musicplaylistshuffler.storage;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Status;
import musicplaylistshuffler.model.Playlist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void savePlayerToJsonValidPathReturnsTrue() {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        Path jsonFile = tempDir.resolve("player.json");

        boolean result = StorageService.savePlayerToJson(player, jsonFile.toString());

        assertTrue(result);
        assertTrue(Files.exists(jsonFile));
    }

    @Test
    void savePlayerToJsonBlankPathReturnsFalse() {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        assertFalse(StorageService.savePlayerToJson(player, ""));
    }

    @Test
    void loadPlayerFromJsonExistingFileReturnsPlayer() {
        Player player = new Player(
                42,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PLAYING
        );

        Path jsonFile = tempDir.resolve("player.json");
        StorageService.savePlayerToJson(player, jsonFile.toString());

        Player loadedPlayer = StorageService.loadPlayerFromJson(jsonFile.toString());

        assertNotNull(loadedPlayer);
        assertEquals(42, loadedPlayer.getPlayedSeconds());
        assertEquals(Status.PLAYING, loadedPlayer.getStatus());
    }

    @Test
    void loadPlayerFromJsonMissingFileReturnsNull() {
        Path jsonFile = tempDir.resolve("missing.json");

        assertNull(StorageService.loadPlayerFromJson(jsonFile.toString()));
    }

    @Test
    void addNewPlaylistFromCsvValidFileAddsPlaylist() throws Exception {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        Path csvFile = tempDir.resolve("songs.csv");

        Files.writeString(csvFile, """
            title;artist;duration;genre
            Song1;Artist1;180;Rock
            Song2;Artist2;240;Pop
            """);

        boolean result = StorageService.addNewPlaylistFromCsv(
                player,
                csvFile.toString(),
                "Favorites"
        );

        assertTrue(result);
        assertEquals(1, player.getPlaylists().size());

        Playlist playlist = player.getPlaylists().get(0);
        assertEquals("Favorites", playlist.getName());
        assertEquals(2, playlist.getSongs().size());
    }

    @Test
    void addNewPlaylistFromCsvBlankPlaylistNameReturnsFalse() throws Exception {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        Path csvFile = tempDir.resolve("songs.csv");

        Files.writeString(csvFile, """
            title;artist;duration;genre
            Song1;Artist1;180;Rock
            """);

        boolean result = StorageService.addNewPlaylistFromCsv(
                player,
                csvFile.toString(),
                ""
        );

        assertFalse(result);
        assertTrue(player.getPlaylists().isEmpty());
    }

    @Test
    void addNewPlaylistFromCsvMissingFileReturnsFalse() {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        Path csvFile = tempDir.resolve("missing.csv");

        boolean result = StorageService.addNewPlaylistFromCsv(
                player,
                csvFile.toString(),
                "Favorites"
        );

        assertFalse(result);
        assertTrue(player.getPlaylists().isEmpty());
    }
}