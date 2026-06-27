package musicplaylistshuffler.storage;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import musicplaylistshuffler.model.Player;
import musicplaylistshuffler.model.Playlist;
import musicplaylistshuffler.model.Song;
import musicplaylistshuffler.utils.FileUtils;
import musicplaylistshuffler.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.security.spec.ECField;

public class StorageService {

    private static final ObjectMapper mapper = new ObjectMapper();

    private StorageService() {
    }


    public static Player startPlayer(String path) {
        if (FileUtils.exists(path) && FileUtils.isReadable(path) && FileUtils.checkExtension(path, "json")) {
            try {
                String json = FileUtils.readAll(path);
                JsonNode node = mapper.readTree(json);
                return JsonUtils.fromJson(node, Player.class);
            } catch (Exception e) {
                System.out.println("Error: " + e);
                return null;
            }
        } else {
            return null;
        }
    }

    public static void savePlayerChanges() {

    }

    public static void createNewPlaylist() {

    }

}
