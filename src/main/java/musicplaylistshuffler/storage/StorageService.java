package musicplaylistshuffler.storage;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.utils.CsvUtils;
import musicplaylistshuffler.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

public class StorageService{

    public static boolean addNewPlaylistFromCsv(Player player, String path, String playlistName) {
        if (player == null || path == null || path.isBlank() || playlistName == null || playlistName.isBlank()) {
            return false;
        }

        try {
            List<Song> songs = CsvUtils.loadSongsFromCsv(path);

            if (songs == null || songs.isEmpty()) {
                return false;
            } else {
                Playlist playlist = new Playlist(playlistName, songs);
                return player.addPlaylist(playlist);
            }

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