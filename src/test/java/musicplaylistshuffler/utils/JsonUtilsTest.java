package musicplaylistshuffler.utils;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void savePlayerToJsonCreatesFile() throws Exception {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        Path jsonFile = tempDir.resolve("player.json");

        JsonUtils.savePlayerToJson(player, jsonFile.toString());

        assertTrue(jsonFile.toFile().exists());
        assertTrue(jsonFile.toFile().length() > 0);
    }

    @Test
    void loadPlayerFromJsonReturnsSavedPlayer() throws Exception {
        Player player = new Player(
                42,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PLAYING
        );

        Path jsonFile = tempDir.resolve("player.json");

        JsonUtils.savePlayerToJson(player, jsonFile.toString());

        Player loadedPlayer = JsonUtils.loadPlayerFromJson(jsonFile.toString());

        assertNotNull(loadedPlayer);
        assertEquals(42, loadedPlayer.getPlayedSeconds());
        assertEquals(Status.PLAYING, loadedPlayer.getStatus());
        assertNotNull(loadedPlayer.getPlaylists());
        assertEquals(0, loadedPlayer.getPlaylists().size());
    }

    @Test
    void loadPlayerFromJsonFileDoesNotExist() {
        Path jsonFile = tempDir.resolve("doesNotExist.json");

        assertThrows(Exception.class, () ->
                JsonUtils.loadPlayerFromJson(jsonFile.toString())
        );
    }

    @Test
    void savePlayerToJsonInvalidPath() {
        Player player = new Player(
                0,
                null,
                new ArrayList<>(),
                0,
                0,
                Status.PAUSED
        );

        assertThrows(Exception.class, () ->
                JsonUtils.savePlayerToJson(player, "")
        );
    }
}