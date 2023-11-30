package model;

import org.bson.types.ObjectId;
import org.json.JSONObject;

import utilites.RecipeHelper;

import java.util.SortedMap;
import java.util.TreeMap;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Singleton class that stores the current user's data
 */
public class UserData {
    private static UserData instance;

    private String userId;
    private String username;
    private SortedMap<String, Recipe> recipes;

    public static UserData getInstance() {
        return instance;
    }

    public static UserData setInstance(UserData userData) {
        instance = userData;
        return instance;
    }

    public UserData setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserData setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserData setRecipeIds(List<String> recipeIds) {
        this.recipes = new TreeMap<String, Recipe>();

        for (String recipeId : recipeIds) {
            this.recipes.put(recipeId, null);
        }

        return this;
    }

    // public UserData setRecipesByObjectId(List<ObjectId> recipeIds) {
    // this.recipes = new TreeMap<ObjectId, Recipe>();

    // for (ObjectId recipeId : recipeIds) {
    // Recipe recipe;
    // try {
    // recipe = RecipeHelper.getRecipe(recipeId);
    // this.recipes.put(recipeId, recipe);
    // } catch (IOException | URISyntaxException e) {
    // System.err.println("Error retrieving recipe " + recipeId.toString() + " from
    // server.");
    // e.printStackTrace();
    // }
    // }

    // return this;
    // }

    public String getUsername() {
        return this.username;
    }

    public String getUserId() {
        return this.userId;
    }

    public static UserData fromJSON(JSONObject responseJSON) {
        UserData userData = new UserData();

        userData.setUserId(responseJSON.getString("_id"));
        userData.setRecipeIds(responseJSON.getJSONArray("recipes").toList().stream().map(Object::toString).toList());

        return userData;
    }
}
