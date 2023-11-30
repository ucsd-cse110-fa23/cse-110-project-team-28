package utilites;

import java.io.IOException;

import org.json.JSONException;

import config.Config;

public class InitializeHelper {
    public static void init() throws JSONException, IOException {
        Config.init();
        MongoDBHelper.init();
    }
}
