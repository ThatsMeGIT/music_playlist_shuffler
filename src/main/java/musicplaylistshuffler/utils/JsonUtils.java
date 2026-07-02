package musicplaylistshuffler.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import musicplaylistshuffler.model.Player;

import java.nio.file.Path;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    private JsonUtils() {
    }

    public static Player loadPlayerFromJson(String path) throws Exception {
        Path jsonPath = Path.of(path);
        return OBJECT_MAPPER.readValue(jsonPath.toFile(), Player.class);
    }

    public static void savePlayerToJson(Player player, String path) throws Exception {
        Path jsonPath = Path.of(path);
        OBJECT_MAPPER.writeValue(jsonPath.toFile(), player);
    }


}
