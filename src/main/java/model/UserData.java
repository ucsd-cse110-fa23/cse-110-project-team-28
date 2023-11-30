package model;

import org.bson.types.ObjectId;

/**
 * Singleton class that stores the current user's data
 */
public class UserData {
    private static UserData instance = new UserData();

    private String username;
    private RecipeData recipeData;
    private ObjectId objectId;

    public static UserData getInstance() {
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return this.username;
    }
}
