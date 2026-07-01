package musicplaylistshuffler.storage;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.utils.CsvUtils;

import java.util.List;

public class StorageService {

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


    public static boolean loadPlayerFromJson(String path) {
        if (path == null || path.isEmpty()) {
            System.out.println("Path is not valid");
            return false;
        }

        return true;

    }

    public static boolean savePlayerToJson(Player player, String path) {
        if (path == null || path.isEmpty()) {
            System.out.println("Path is not valid");
            return false;
        }
        if (player == null){
            System.out.println("Player is not valid");
            return false;
        }

        return true;
    }

}