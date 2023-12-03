package server;

import com.sun.net.httpserver.*;

import model.Recipe;
import model.RecipeData;

import java.io.*;
import java.util.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import utilites.MongoDBHelper;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import org.bson.Document;

public class APIHandler implements HttpHandler {

    public APIHandler() {
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        // get the request body as an InputStream
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // read the request body
        int buff;
        StringBuilder buffer = new StringBuilder(512);
        while ((buff = bufferedReader.read()) != -1) {
            buffer.append((char) buff);
        }

        bufferedReader.close();
        inputStreamReader.close();

        // convert the request body to a Recipe object
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(buffer.toString(), Recipe.class);

        // Update the recipe in MongoDB
        int status = updateRecipeInMongoDB(recipe);

        if (status == -1) {
            return "Recipe not found or error in updating";
        }

        return "Recipe successfully updated";
    }

    private int updateRecipeInMongoDB(Recipe recipe) {
        try (MongoClient mongoClient = MongoDBHelper.getMongoClient();) {
            MongoDatabase database = mongoClient.getDatabase("recipeDatabase");
            MongoCollection<Document> collection = database.getCollection("recipes");

            // Building the update document
            Document updateDoc = new Document()
                    .append("name", recipe.getName())
                    .append("mealType", recipe.getMealType())
                    .append("ingredients", recipe.getIngredients())
                    .append("steps", recipe.getSteps());

            long updatedCount = collection
                    .updateOne(Filters.eq("name", recipe.getName()), new Document("$set", updateDoc))
                    .getModifiedCount();

            if (updatedCount == 0) {
                System.out.println("No recipe found with name: " + recipe.getName());
                return -1; // Recipe not found or no update made
            }

            return 0; // Success
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Error occurred
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        // Parse the query to get recipeId and username
        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
        String recipeName = params.get("recipeName");
        String username = params.get("username");

        // Perform the deletion
        if (deleteRecipeFromMongoDB(recipeName, username)) {
            return "Recipe successfully deleted";
        } else {
            return "Failed to delete recipe";
        }
    }

    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], "");
                }
            }
        }
        return result;
    }

    private boolean deleteRecipeFromMongoDB(String recipeName, String username) {
        try (MongoClient mongoClient = MongoDBHelper.getMongoClient();) {
            MongoDatabase database = mongoClient.getDatabase("recipeDatabase");
            MongoCollection<Document> collection = database.getCollection("recipes");

            // Check if the recipe belongs to the user
            Document foundRecipe = collection.find(Filters.eq("name", recipeName)).first();
            if (foundRecipe != null && foundRecipe.getString("username").equals(username)) {
                // Delete the recipe
                collection.deleteOne(foundRecipe);
                System.out.println("Deleted recipe: " + recipeName);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String handleGet(HttpExchange httpExchange) {
        Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());

        String recipeName = params.get("name");
        String username = params.get("username");

        if (recipeName != null && username != null) {
            // Fetch single recipe
            Recipe recipe = fetchRecipeFromMongoDB(recipeName, username);
            if (recipe != null) {
                Gson gson = new Gson();
                return gson.toJson(recipe);
            } else {
                return "Recipe not found";
            }
        } else {
            // Fetch all recipes from MongoDB for the given username
            List<Recipe> userRecipes = fetchRecipesFromMongoDB(username);

            Gson gson = new Gson();
            return gson.toJson(userRecipes);
        }
    }

    private List<Recipe> fetchRecipesFromMongoDB(String username) {
        List<Recipe> recipes = new ArrayList<>();
        try (MongoClient mongoClient = MongoDBHelper.getMongoClient();) {
            MongoDatabase database = mongoClient.getDatabase("recipeDatabase");
            MongoCollection<Document> collection = database.getCollection("recipes");

            FindIterable<Document> docs = collection.find(Filters.eq("username", username));
            for (Document doc : docs) {
                Recipe recipe = convertDocumentToRecipe(doc);
                recipes.add(recipe);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
        return recipes;
    }

    private Recipe fetchRecipeFromMongoDB(String recipeName, String username) {
        try (MongoClient mongoClient = MongoDBHelper.getMongoClient();) {
            MongoDatabase database = mongoClient.getDatabase("recipeDatabase");
            MongoCollection<Document> collection = database.getCollection("recipes");

            Document doc = collection
                    .find(Filters.and(Filters.eq("name", recipeName), Filters.eq("username", username))).first();
            if (doc != null) {
                return convertDocumentToRecipe(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        }
        return null;
    }

    private String extractUsernameFromQuery(HttpExchange httpExchange) {
        // Parse the query string
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] pair = param.split("=");
                if (pair.length > 1 && "username".equals(pair[0])) {
                    return pair[1];
                }
            }
        }
        return null;
    }

    private Recipe convertDocumentToRecipe(Document doc) {
        String name = doc.getString("name");
        String mealType = doc.getString("mealType");
        String ingredients = doc.getString("ingredients");
        String steps = doc.getString("steps");
        String username = doc.getString("username");

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setMealType(mealType);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
        recipe.setUsername(username);

        return recipe;
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        // get the request body as an InputStream
        InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        System.out.println("Request URI: " + httpExchange.getRequestURI().toString());

        // read the request body
        int buff;
        StringBuilder buffer = new StringBuilder(512);
        while ((buff = bufferedReader.read()) != -1) {
            buffer.append((char) buff);
        }

        bufferedReader.close();
        inputStreamReader.close();

        // convert the request body to a Recipe object
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(buffer.toString(), Recipe.class);

        // Add username from query parameters or request body
        String username = extractUsername(httpExchange);
        recipe.setUsername(username);

        // Save recipe to MongoDB
        saveRecipeToMongoDB(recipe);

        return "Recipe successfully added";
    }

    private String extractUsername(HttpExchange httpExchange) {
        try {
            String query = httpExchange.getRequestURI().getQuery();
            System.out.println("Query string: " + query); // Log the query string

            if (query != null) {
                for (String param : query.split("&")) {
                    System.out.println("Parameter: " + param); // Log each parameter

                    String[] pair = param.split("=");
                    if (pair.length > 1) {
                        String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8.name());
                        String value = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name());

                        System.out.println("Key: " + key + ", Value: " + value); // Log key-value pairs

                        if (key.equals("username")) {
                            return value; // Return the decoded username
                        }
                    }
                }
            }
            return "defaultUsername"; // Default username if not found
        } catch (Exception e) {
            e.printStackTrace();
            return "errorUsername"; // Username in case of error
        }
    }

    private void saveRecipeToMongoDB(Recipe recipe) {
        // MongoDB interaction logic
        try (MongoClient mongoClient = MongoDBHelper.getMongoClient();) {
            MongoDatabase database = mongoClient.getDatabase("recipeDatabase");
            MongoCollection<Document> collection = database.getCollection("recipes");

            Document recipeDoc = new Document("name", recipe.getName())
                    .append("mealType", recipe.getMealType())
                    .append("ingredients", recipe.getIngredients())
                    .append("steps", recipe.getSteps())
                    .append("username", recipe.getUsername());

            collection.insertOne(recipeDoc);
            System.out.println("Recipe saved to MongoDB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}