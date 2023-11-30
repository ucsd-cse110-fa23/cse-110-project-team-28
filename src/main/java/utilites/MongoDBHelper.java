package utilites;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;

import java.util.ArrayList;

import config.Config;

/**
 * MongoDB class is a singleton class that initializes the MongoDB client upon
 * application startup
 */
public class MongoDBHelper {
    private static MongoClient mongoClient;

    public static void init() {
        mongoClient = MongoClients.create(Config.getMongoDBUri());
        System.out.println("MongoDBHelper initialized");
    }

    public static MongoClient getMongoClient() {
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
        Bson filter = Filters.eq("username", username);
        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .find(filter).first() != null;
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
    public static String insertUser(String username, String password) {
        InsertOneResult result = mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .insertOne(new Document("username", username)
                        .append("password", password));

        String userId = result
                .getInsertedId()
                .toString();

        System.out.println("Inserted user with id: " + userId);

        return userId;
    }

    /**
     * Finds a user in the database with the given username and password
     * 
     * @param username
     * @param password
     * @return
     */
    public static Document findUser(String username, String password) {
        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .find()
                .filter(Filters.and(Filters.eq("username", username), Filters.eq("password", password)))
                .first();
    }
}
