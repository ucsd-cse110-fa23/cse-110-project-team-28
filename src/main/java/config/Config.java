package config;

import org.json.JSONException;
import org.json.JSONObject;

import utilites.Logger;

import java.io.IOException;
import java.io.InputStream;

public class Config {
    // Path: src/main/resources/config.json
    private static final String CONFIG_FILE = "config.json";

    private static String MONGODB_URI;
    private static String DATABASE;
    private static String USER_COLLECTION;
    private static String RECIPE_COLLECTION;
    private static String SERVER_HOSTNAME;
    private static int SERVER_PORT;

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
        RECIPE_COLLECTION = config.getString("recipe_collection");
        SERVER_HOSTNAME = config.getString("server_hostname");
        SERVER_PORT = config.getInt("server_port");

        Logger.log("Config initialized");
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

    public static String getRecipeCollectionName() {
        return RECIPE_COLLECTION;
    }

    public static String getServerHostname() {
        return SERVER_HOSTNAME;
    }

    public static int getServerPort() {
        return SERVER_PORT;
    }

}