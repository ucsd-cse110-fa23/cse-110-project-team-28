package model;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RecipeData {
    private final static String DEFAULT_FILE = "recipes.json";
    private static RecipeData instance = null;

    private String fileLocation;
    private ArrayList<Recipe> recipes;
    private String username;

    public static RecipeData getInstance() {
        if (instance == null) {
            instance = new RecipeData();
            instance.setFileLocation(DEFAULT_FILE);
        }

        return instance;
    }

    public RecipeData() {
        this.recipes = new ArrayList<Recipe>();
    }

    public static void setInstance(RecipeData recipeData) {
        instance = recipeData;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        saveRecipes(recipe);
    }

    public void clear() {
        recipes.clear();
    }

    /**
     * Deletes a recipe from the RecipeData
     * 
     * @param recipeName the name of the recipe to delete
     * @return 0 if successful, -1 if not
     */
    public void deleteRecipe(String recipeName, String username) {
        // Create an HTTP DELETE request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8000/api/deleteRecipe?recipeName=" + recipeName + "&username=" + username)) 
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    if (recipes != null) {
                        setRecipes(recipes);
                        for (int i = 0; i < recipes.size(); i++) {
                            if (recipes.get(i).getName().equals(recipeName)) {
                                recipes.remove(i);
                            }
                        }
                    }
                })
                .join();
    }

    // Call this method when the user saves their changes
    public void saveRecipeChanges(Recipe recipe) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/api/?username=" + username)) 
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(recipe)))
                .header("Content-Type", "application/json")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    System.out.println("Server response: " + response);
                })
                .join();
    }

    /**
     * Method to load recipes from a file
     */
    public void loadRecipes(String username) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/api/?username=" + username))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(jsonResponse -> {
                    ArrayList<Recipe> recipes = new Gson().fromJson(jsonResponse, new TypeToken<List<Recipe>>() {
                    }.getType());
                    if (recipes != null) {
                        setRecipes(recipes);
                    }
                })
                .join();
    }

    public void saveRecipes(Recipe recipe) {
        username = recipe.getUsername();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8000/api/?username=" + username))
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(recipe)))
                .header("Content-Type", "application/json")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
    }
}
