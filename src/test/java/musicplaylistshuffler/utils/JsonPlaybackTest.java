package musicplaylistshuffler.utils;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonPlaybackTest {
    @TempDir
    Path tempDir;

    @Test
    void clearsInvalidPlaybackSelectionWhenLoadingJson() throws Exception {
        Path path = tempDir.resolve("invalid-selection.json");
        Files.writeString(path, """
                {
                  "playedSeconds": 42,
                  "currentSong": {"title":"Absent","artist":"Artist","duration":60,"genre":"Pop"},
                  "playlists": [{"name":"Favorites","songs":[
                    {"title":"Present","artist":"Artist","duration":60,"genre":"Pop"}
                  ]}],
                  "currentPlaylistIndex": 0,
                  "currentSongIndex": -2,
                  "status": "PLAYING",
                  "loop": "REPEAT_ON"
                }
                """);

        Player loaded = JsonUtils.loadPlayerFromJson(path.toString());

        assertNull(loaded.getCurrentSong());
        assertEquals(0, loaded.getCurrentSongIndex());
        assertEquals(0, loaded.getPlayedSeconds());
        assertEquals(Status.PAUSED, loaded.getStatus());
        assertTrue(loaded.getCurrentPlaylist().isOnRepeat());
        assertDoesNotThrow(loaded::skip);
        assertDoesNotThrow(loaded::previous);
    }

    @Test
    void loadsLegacyJsonWithSongIndexFromShuffleOrder() throws Exception {
        Path path = tempDir.resolve("legacy.json");
        Files.writeString(path, """
                {
                  "playedSeconds": 30,
                  "currentSong": {"title":"Second","artist":"Artist","duration":90,"genre":"Rock"},
                  "playlists": [{"name":"Favorites","songs":[
                    {"title":"First","artist":"Artist","duration":60,"genre":"Pop"},
                    {"title":"Second","artist":"Artist","duration":90,"genre":"Rock"}
                  ]}],
                  "currentPlaylistIndex": 0,
                  "currentSongIndex": 0,
                  "status": "PAUSED"
                }
                """);

        Player loaded = JsonUtils.loadPlayerFromJson(path.toString());

        assertEquals("Second", loaded.getCurrentSong().getTitle());
        assertEquals(1, loaded.getCurrentSongIndex());
        assertSame(loaded.getCurrentPlaylist().getSpecificSong(1), loaded.getCurrentSong());
        assertEquals(30, loaded.getPlayedSeconds());
        assertFalse(loaded.getCurrentPlaylist().isOnRepeat());
    }

    @Test
    void loadsSongsAndKeepsPlaybackConsistentAfterShuffle() throws Exception {
        Song first = new Song("First", "Artist", 60, "Pop");
        Song second = new Song("Second", "Other Artist", 90, "Rock");
        Playlist playlist = new Playlist("Favorites", List.of(first, second));
        Player original = new Player(30, second, List.of(playlist), 0, 1, Status.PLAYING);
        original.repeatAll();
        Path path = tempDir.resolve("player.json");

        JsonUtils.savePlayerToJson(original, path.toString());
        Player loaded = JsonUtils.loadPlayerFromJson(path.toString());

        assertEquals("Second", loaded.getCurrentSong().getTitle());
        assertEquals("Other Artist", loaded.getCurrentSong().getArtist());
        assertEquals(90, loaded.getCurrentSong().getDuration());
        assertEquals("Rock", loaded.getCurrentSong().getGenre());
        assertEquals(30, loaded.getPlayedSeconds());
        assertSame(loaded.getCurrentPlaylist().getSpecificSong(1), loaded.getCurrentSong());
        assertTrue(loaded.getCurrentPlaylist().isOnRepeat());

        Song selected = loaded.getCurrentSong();
        loaded.toggleShuffle();
        assertSame(selected, loaded.getCurrentSong());
        assertSame(selected, loaded.getCurrentPlaylist().getSpecificSong(loaded.getCurrentSongIndex()));
        loaded.toggleShuffle();
        loaded.skip();
        assertEquals("First", loaded.getCurrentSong().getTitle());
        assertEquals(Status.PLAYING, loaded.getStatus());
    }
}
