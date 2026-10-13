package musicplaylistshuffler.storage;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.utils.CsvUtils;
import musicplaylistshuffler.utils.JsonUtils;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

public class StorageService{

    /** Player and save policy for a session started from a JSON file. */
    public record StartupState(Player player, boolean saveAllowed) {
    }

    /**
     * Restores saved data, or uses the fallback without allowing an existing
     * unreadable file to be overwritten when the application closes.
     */
    public static StartupState loadPlayerForStartup(String path, Player fallback) {
        Player loaded = loadPlayerFromJson(path);
        if (loaded != null) {
            return new StartupState(loaded, true);
        }
        boolean missing = false;
        if (path != null && !path.isBlank()) {
            try {
                missing = Files.notExists(Path.of(path));
            } catch (InvalidPathException | SecurityException e) {
                // Invalid or inaccessible paths must not enable automatic saving.
            }
        }
        return new StartupState(fallback, missing);
    }

    public static boolean addNewPlaylistFromCsv(Player player, String path, String playlistName) {
        if (player == null || path == null || path.isBlank() || playlistName == null || playlistName.isBlank()) {
            return false;
        }

        try {
            List<Song> songs = CsvUtils.loadSongsFromCsv(path);

            if (songs == null || songs.isEmpty()) {
                return false;
            }
            Playlist playlist = new Playlist(playlistName, songs);
            return player.addPlaylist(playlist);

        } catch (Exception e) {
            System.out.println("Error while creating Playlist from Csv: " + e);
            return false;
        }
    }

    public static Player loadPlayerFromJson(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        try {
            return JsonUtils.loadPlayerFromJson(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean savePlayerToJson(Player player, String path) {
        if (path == null || path.isBlank()) {
            return false;
        }

        try {
            JsonUtils.savePlayerToJson(player, path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
