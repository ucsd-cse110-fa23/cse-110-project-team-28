package utilites;

import org.bson.Document;
import org.bson.conversions.Bson;

import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import config.Config;
import model.Recipe;

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

    public static synchronized MongoClient getMongoClient() {
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
        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .find(Filters.eq("username", username)).first() != null;
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
    public static Document findUserByUsernamePassword(String username, String password) {
        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .find(Filters.and(Filters.eq("username", username), Filters.eq("password", password)))
                .first();
    }

    public static Document findUserById(String userId) {
        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .find(Filters.eq("_id", new ObjectId(userId)))
                .first();
    }

    public static Recipe findRecipeById(String recipeId) {
        System.out.println("Finding recipe with id: " + recipeId);

        Document recipeDocument = mongoClient
                .getDatabase("pantrypal")
                .getCollection("recipes")
                .find(Filters.eq("_id", new ObjectId(recipeId)))
                .first();

        System.out.println("Recipe document query result: " + recipeDocument);

        Recipe recipe = Recipe.fromDocument(recipeDocument);

        return recipe;
    }

    public static List<Recipe> findRecipesByUserId(String userId) {
        System.out.println("Finding recipes for user with id: " + userId);

        Document user = findUserById(userId);

        System.out.println("Found user: " + user.toString());

        if (!user.containsKey("recipes")) {
            System.out.println("User does not have any recipes");
            return new ArrayList<>();
        }

        List<String> recipeIdList = user.getList("recipes", String.class);

        System.out.println("Found recipeIdList: " + recipeIdList.toString());

        List<Recipe> recipes = new ArrayList<>();

        for (String recipeId : recipeIdList) {
            System.out.println("Finding recipe with id: " + recipeId);

            Recipe recipe = findRecipeById(recipeId);

            System.out.println("Found recipe");

            if (recipe == null)
                continue;

            recipes.add(recipe);
        }

        return recipes;
    }

    public static InsertOneResult insertRecipe(JSONObject requestJsonObject) {
        Document recipeDocument = new Document()
                .append("name", requestJsonObject.getString("name"))
                .append("mealType", requestJsonObject.getString("mealType"))
                .append("ingredients", requestJsonObject.getString("ingredients"))
                .append("steps", requestJsonObject.getString("steps"));
        // todo: implement image uploading
        // .append("imageURL", requestJsonObject.getString("imageURL"));

        System.out.println("Inserting recipe: " + recipeDocument.toString());

        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getRecipeCollectionName())
                .insertOne(recipeDocument);
    }

    public static Document insertUserRecipeId(String userId, String recipeId) {
        System.out.println("Inserting recipeId: " + recipeId + " into user with id: " + userId);

        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getUserCollectionName())
                .findOneAndUpdate(Filters.eq("_id", new ObjectId(userId)),
                        new Document("$push", new Document("recipes", recipeId)));
    }

    public static UpdateResult updateRecipe(JSONObject requestJsonObject) {
        System.out.println("Updating recipe: " + requestJsonObject.toString());

        ObjectId recipeId = new ObjectId(requestJsonObject.getString("id"));

        Bson updates = Updates.set("steps", requestJsonObject.getString("steps"));

        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getRecipeCollectionName())
                .updateOne(Filters.eq("_id", recipeId), updates);
    }

    public static DeleteResult deleteRecipe(String recipeId) {
        System.out.println("Deleting recipe with id: " + recipeId);

        return mongoClient
                .getDatabase(Config.getDatabaseName())
                .getCollection(Config.getRecipeCollectionName())
                .deleteOne(Filters.eq("_id", new ObjectId(recipeId)));
    }
}