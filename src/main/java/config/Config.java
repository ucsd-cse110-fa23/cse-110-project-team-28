package config;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class Config {
    // Path: src/main/resources/config.json
    private static final String CONFIG_FILE = "config.json";

    private static String MONGODB_URI;
    private static String DATABASE;
    private static String USER_COLLECTION;

    /**
     * Loads the config file
     * 
     * @throws IOException
     */
    public static void init() throws IOException, JSONException {
        // get config file as stream
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE);

        // read config file as string
        String configString = new String(inputStream.readAllBytes());

        // parse config file as JSON
        JSONObject config = new JSONObject(configString);

        // set config variables
        MONGODB_URI = config.getString("mongodb_uri");
        DATABASE = config.getString("database");
        USER_COLLECTION = config.getString("user_collection");

        System.out.println("Config initialized");
    }

    public static String getMongoDBUri() {
        return MONGODB_URI;
    }

    public static String getDatabaseName() {
        return DATABASE;
    }

    public static String getUserCollectionName() {
        return USER_COLLECTION;
    }
}
