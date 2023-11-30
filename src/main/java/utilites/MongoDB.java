package utilites;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import config.Config;

/**
 * MongoDB class is a singleton class that initializes the MongoDB client upon
 * application startup
 */
public class MongoDB {
    private static MongoClient mongoClient;

    private static boolean isLoaded = false;

    public static void init() {
        mongoClient = MongoClients.create(Config.getMongoDBUri());
        isLoaded = true;
    }

    public static MongoClient getMongoClient() {
        if (!isLoaded)
            throw new RuntimeException("MongoDB not loaded");
        return mongoClient;
    }

    public static void closeMongoClient() {
        mongoClient.close();
    }

    /**
     * Checks if a username exists in the database
     * 
     * @param username
     * @return
     */
    public static Boolean doesUsernameExist(String username) {
        return mongoClient.getDatabase(Config.getDatabaseName()).getCollection(Config.getUserCollectionName())
                .find().filter(Filters.eq("username", username)).first() != null;
    }

    /**
     * Inserts a new user into the database, does not check if the username already
     * exists
     * 
     * @param mongoClient
     * @param username
     * @param password
     * @return ObjectId of the inserted user
     */
    public static ObjectId insertUser(String username, String password) {
        InsertOneResult result = mongoClient.getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .insertOne(new Document("username", username).append("password", password));

        return result.getInsertedId().asObjectId().getValue();
    }

    /**
     * Finds a user in the database with the given username and password
     * 
     * @param username
     * @param password
     * @return
     */
    public static Document findUser(String username, String password) {
        return mongoClient.getDatabase(Config.getDatabaseName()).getCollection(Config.getUserCollectionName())
                .find().filter(Filters.and(Filters.eq("username", username), Filters.eq("password", password)))
                .first();
    }
}
