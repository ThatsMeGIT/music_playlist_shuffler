package musicplaylistshuffler.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import musicplaylistshuffler.model.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonUtils {

    private static final Logger logger = Logger.getLogger(JsonUtils.class.getName());
    private static ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T fromJson(JsonNode node, Class<T> tclass) {
        try {
            return mapper.treeToValue(node, tclass);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    public static Player loadFromFile(String path, String name) {
        if (!FileUtils.exists(path)) {
            logger.log(Level.SEVERE, "Error: File couldn't be found");
            return null;
        } else if (!FileUtils.isReadable(path)) {
            logger.log(Level.SEVERE, "Error: File couldn't be read");
            return null;
        } else if (!FileUtils.checkExtension(path, "json")) {
            logger.log(Level.SEVERE, "Error: File isn't needed type");
            return null;
        } else {
            return null;
        }

    }


}
