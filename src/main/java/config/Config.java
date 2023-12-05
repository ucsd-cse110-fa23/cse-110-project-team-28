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

    private static String OPEN_AI_API_KEY;

    // dall-e
    private static String DALL_E_API_ENDPOINT;
    private static String DALL_E_MODEL;
    private static String DALL_E_PROMPT;

    // chat gpt
    private static String CHAT_GPT_API_ENDPOINT;
    private static String CHAT_GPT_MODEL;
    // private static String CHAT_GPT_PROMPT;

    // whisper
    private static String WHISPER_API_ENDPOINT;
    private static String WHISPER_MODEL;

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

        // openai
        OPEN_AI_API_KEY = config.getString("open_ai_api_key");

        // dall-e
        JSONObject dallEConfig = config.getJSONObject("dall_e");
        DALL_E_API_ENDPOINT = dallEConfig.getString("api_endpoint");
        DALL_E_MODEL = dallEConfig.getString("model");
        DALL_E_PROMPT = dallEConfig.getString("prompt");

        // chat gpt
        JSONObject chatGPTConfig = config.getJSONObject("chat_gpt");
        CHAT_GPT_API_ENDPOINT = chatGPTConfig.getString("api_endpoint");
        CHAT_GPT_MODEL = chatGPTConfig.getString("model");

        // whisper
        JSONObject whisperConfig = config.getJSONObject("whisper");
        WHISPER_API_ENDPOINT = whisperConfig.getString("api_endpoint");
        WHISPER_MODEL = whisperConfig.getString("model");

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

    public static String getOpenAiApiKey() {
        return OPEN_AI_API_KEY;
    }

    public static String getDallEApiEndpoint() {
        return DALL_E_API_ENDPOINT;
    }

    public static String getDallEModel() {
        return DALL_E_MODEL;
    }

    public static String getDallEPrompt() {
        return DALL_E_PROMPT;
    }

    public static String getChatGPTApiEndpoint() {
        return CHAT_GPT_API_ENDPOINT;
    }

    public static String getChatGPTModel() {
        return CHAT_GPT_MODEL;
    }

    public static String getWhisperApiEndpoint() {
        return WHISPER_API_ENDPOINT;
    }

    public static String getWhisperModel() {
        return WHISPER_MODEL;
    }

}