package musicplaylistshuffler.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
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


}
