package utilites;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URISyntaxException;

import org.bson.types.ObjectId;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;

import config.Config;
import model.Recipe;
import model.UserData;
import server.AuthResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

public class RecipeHelper {
    public static Recipe getRecipe(String recipeId) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        new URIBuilder()
                                .setHost(Config.getServerHostname())
                                .setPort(Config.getServerPort())
                                .setPath("api")
                                .addParameter("_id", recipeId)
                                .build()))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    Recipe recipe = new Gson().fromJson(response.body(), Recipe.class);
                    return recipe;
                })
                .join();
    }

    public static List<Recipe> getUserRecipes(String userId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        new URIBuilder()
                                .setHost(Config.getServerHostname())
                                .setPort(Config.getServerPort())
                                .setPath("api/recipes")
                                .addParameter("userId", userId)
                                .build()))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.out.println("Received non-200 status code from server: " + response.statusCode() + " "
                                + response.body());
                        throw new RuntimeException("Error getting recipes"); // todo: implement safer error handling
                    }

                    Gson gson = new Gson();
                    List<Recipe> recipes = gson.fromJson(response.body(), new TypeToken<List<Recipe>>() {
                    }.getType());

                    return recipes;
                })
                .join();
    }

    public static void addRecipe(Recipe recipe) {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", recipe.getName());
        requestBody.put("mealType", recipe.getMealType());
        requestBody.put("ingredients", recipe.getIngredients());
        requestBody.put("steps", recipe.getSteps());
        //requestBody.put("imageURL", recipe.getImageURL());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        new URIBuilder()
                                .setHost(Config.getServerHostname())
                                .setPort(Config.getServerPort())
                                .setPath("api/recipes")
                                .addParameter("userId", UserData.getInstance().getUserId())
                                .build()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.out.println("Received non-200 status code from server: " + response.statusCode() + " "
                                + response.body());
                        throw new RuntimeException("Error adding recipe"); // todo: implement safer error handling
                    }

                    return response;
                })
                .join();
    }

    public static void editRecipe(Recipe recipe) {
        HttpClient client = HttpClient.newHttpClient();

        JSONObject requestBody = new JSONObject();
        requestBody.put("steps", recipe.getSteps()); // only steps needs to be updated
        requestBody.put("id", recipe.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        new URIBuilder()
                                .setHost(Config.getServerHostname())
                                .setPort(Config.getServerPort())
                                .setPath("api/recipes")
                                .build()))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.out.println("Received non-200 status code from server: " + response.statusCode() + " "
                                + response.body());
                        throw new RuntimeException("Error editing recipe"); // todo: implement safer error handling
                    }

                    return response;
                })
                .join();
    }

    public static void deleteRecipe(Recipe recipe) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        new URIBuilder()
                                .setHost(Config.getServerHostname())
                                .setPort(Config.getServerPort())
                                .setPath("api/recipes")
                                .addParameter("recipeId", recipe.getId())
                                .build()))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() != 200) {
                        System.out.println("Received non-200 status code from server: " + response.statusCode() + " "
                                + response.body());
                        throw new RuntimeException("Error deleting recipe"); // todo: implement safer error handling
                    }

                    return response;
                })
                .join();
    }
}