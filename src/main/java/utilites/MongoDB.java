package utilites;

import java.io.IOException;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import config.Config;

/**
 * MongoDB class is a singleton class that initializes the MongoDB client upon
 * application startup
 */
public class MongoDB {
    private static MongoClient mongoClient;

    private static boolean isLoaded = false;

    public static void init() throws IOException{
        mongoClient = MongoClients.create(Config.getMongoDBUri());
        isLoaded = true;
    }

    public static MongoClient getMongoClient() throws IOException{
        init();
        if (!isLoaded)
            throw new RuntimeException("MongoDB not loaded");
        return mongoClient;
    }
}
