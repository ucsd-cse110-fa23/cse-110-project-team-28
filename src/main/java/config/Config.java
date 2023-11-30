package config;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

public class Config {
    // Path: src/main/resources/config.json
    private static final String CONFIG_FILE = "config.json";

    private static boolean isLoaded = false;

    private static String MONGODB_URI;
    private static String DATABASE_NAME;
    private static String USER_COLLECTION_NAME;

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
        MONGODB_URI = config.getString("MONGODB_URI");
        DATABASE_NAME = config.getString("DATABASE_NAME");
        USER_COLLECTION_NAME = config.getString("USER_COLLECTION_NAME");

        // set isLoaded to true
        isLoaded = true;
    }

    public static String getMongoDBUri() {
        if (!isLoaded)
            throw new RuntimeException("Config not loaded");
        return MONGODB_URI;
    }

    public static String getDatabaseName() {
        if (!isLoaded)
            throw new RuntimeException("Config not loaded");
        return DATABASE_NAME;
    }

    public static String getUserCollectionName() {
        if (!isLoaded)
            throw new RuntimeException("Config not loaded");
        return USER_COLLECTION_NAME;
    }
}
