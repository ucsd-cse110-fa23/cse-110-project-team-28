package server;

import com.sun.net.httpserver.*;

import model.Recipe;
import java.io.*;
import java.util.*;

import utilites.Logger;
import utilites.MongoDBHelper;

import com.google.gson.Gson;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.json.JSONObject;

public class APIHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {

        String requestPath = httpExchange.getRequestURI().getPath();
        String httpContext = httpExchange.getHttpContext().getPath();
        String path = requestPath.substring(httpContext.length());

        Logger.log("Received request (uri: " + httpExchange.getRequestURI().toString() + ")");

        switch (path) {
            case "recipes":
                recipesHandler(httpExchange);
                break;
            case "recipe":
                recipeHandler(httpExchange);
                break;
            default:
                break;
        }
    }

    private void recipesHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                recipesGetHandler(httpExchange);
                break;
            case "POST":
                recipesPostHandler(httpExchange);
                break;
            case "PUT":
                recipesPutHandler(httpExchange);
                break;
            case "DELETE":
                recipeDeleteHandler(httpExchange);
                break;
            default:
                Logger.warn("Unsupported request method: " + requestMethod);
                break;
        }
    }

    private void recipeHandler(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();

        switch (requestMethod) {
            case "GET":
                recipeGetHandler(httpExchange);
                break;
            default:
                Logger.warn("Unsupported request method: " + requestMethod);
                break;
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        Logger.log("Sending response (status code: " + statusCode + ", length: " + responseBody.length() + ")");
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().write(responseBody.getBytes());
        exchange.close();
    }

    private void sendResponse(HttpExchange exchange, int statusCode) throws IOException {
        Logger.log("Sending response (status code: " + statusCode + ", length: 0)");
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().write("".getBytes());
        exchange.close();
    }

    private HashMap<String, String> extractQueryParameters(String query) {
        if (query == null) {
            System.err.println("No query parameters provided");
            return null;
        }

        String[] queryParams = query.split("&");

        HashMap<String, String> params = new HashMap<>();

        for (String param : queryParams) {
            params.put(param.split("=")[0], param.split("=")[1]);
        }

        return params;
    }

    private void recipeGetHandler(HttpExchange httpExchange) throws IOException {
        // Parse the query string
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) {
            System.err.println("No query parameters provided");
            sendResponse(httpExchange, 400, "No query parameters provided");
            return;
        }

        HashMap<String, String> params = extractQueryParameters(query);

        if (!params.containsKey("recipeId")) {
            System.err.println("No recipeId parameter provided");
            sendResponse(httpExchange, 400, "No recipeId parameter provided");
            return;
        }

        String recipeId = params.get("recipeId");

        if (recipeId == null || recipeId.isEmpty()) {
            System.err.println("No recipeId parameter provided");
            sendResponse(httpExchange, 400, "No recipeId parameter provided");
            return;
        }

        Logger.log("Fetching recipe (id: " + recipeId + ")");

        Recipe recipe = MongoDBHelper.findRecipeById(recipeId);

        if (recipe == null) {
            System.err.println("Error finding recipe");
            sendResponse(httpExchange, 500, "Error finding recipe");
            return;
        }

        Logger.log("Found recipe (id: " + recipe.getId() + ")");

        Gson gson = new Gson();
        String responseBody = gson.toJson(recipe);

        // Sending back response to the client
        sendResponse(httpExchange, 200, responseBody);
    }

    private void recipesGetHandler(HttpExchange httpExchange) throws IOException {
        // Parse the query string
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) {
            System.err.println("No query parameters provided");
            sendResponse(httpExchange, 400, "No query parameters provided");
            return;
        }

        HashMap<String, String> params = extractQueryParameters(query);

        if (!params.containsKey("userId")) {
            System.err.println("No userId parameter provided");
            sendResponse(httpExchange, 400, "No userId parameter provided");
            return;
        }

        String userId = params.get("userId");

        if (userId == null || userId.isEmpty()) {
            System.err.println("No userId parameter provided");
            sendResponse(httpExchange, 400, "No userId parameter provided");
            return;
        }

        Logger.log("Finding recipes for user (id: " + userId + ")");

        List<Recipe> userRecipes = MongoDBHelper.findRecipesByUserId(userId);

        Logger.log("Found " + userRecipes.size()
                + (userRecipes.size() == 1 ? " recipe for user (id: " : " recipes for user (id: ") + userId + ")");

        Gson gson = new Gson();
        String responseBody = gson.toJson(userRecipes);

        // Sending back response to the client
        sendResponse(httpExchange, 200, responseBody);
    }

    private void recipesPostHandler(HttpExchange httpExchange) throws IOException {
        // get query parameters
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) {
            System.err.println("No query parameters provided");
            sendResponse(httpExchange, 400, "No query parameters provided");
            return;
        }

        HashMap<String, String> params = extractQueryParameters(query);
        String userId = params.get("userId");

        if (userId == null) {
            System.err.println("No userId parameter provided");
            sendResponse(httpExchange, 400, "No userId parameter provided");
            return;
        }

        Logger.log("userId: " + userId);

        // parse request body
        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes());
        JSONObject requestJsonObject = new JSONObject(requestBody);

        Logger.log("Request body (length: " + requestBody.length() + ")");

        InsertOneResult result = MongoDBHelper.insertRecipe(requestJsonObject);

        if (result == null) {
            System.err.println("Error inserting recipe");
            sendResponse(httpExchange, 500, "Error inserting recipe");
            return;
        }

        String recipeId = result.getInsertedId().asObjectId().getValue().toString();

        Logger.log("Inserted recipe (id: " + recipeId + ")");

        Document user = MongoDBHelper.insertUserRecipeId(userId, recipeId);

        if (user == null) {
            System.err.println("Error adding recipe to user");
            sendResponse(httpExchange, 500, "Error adding recipe to user");
            return;
        }

        Logger.log("Added recipe to user (id: " + user.getObjectId("_id").toString() + ")");

        // send recipeId back to client
        sendResponse(httpExchange, 200, recipeId);
    }

    private void recipesPutHandler(HttpExchange httpExchange) throws IOException {
        // read request body
        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes());

        Logger.log("Request body: " + requestBody);

        // parse request body
        JSONObject requestJsonObject = new JSONObject(requestBody);

        Logger.log("Request JSON object: " + requestJsonObject.toString());

        UpdateResult result = MongoDBHelper.updateRecipe(requestJsonObject);

        if (result == null || result.getModifiedCount() == 0) {
            System.err.println("Error updating recipe");
            sendResponse(httpExchange, 500, "Error updating recipe");
            return;
        }

        Logger.log("Updated recipe");

        // send recipeId back to client
        sendResponse(httpExchange, 200);
    }

    private void recipeDeleteHandler(HttpExchange httpExchange) throws IOException {
        // Parse the query string
        String query = httpExchange.getRequestURI().getQuery();

        if (query == null) {
            System.err.println("No query parameters provided");
            sendResponse(httpExchange, 400, "No query parameters provided");
            return;
        }

        HashMap<String, String> params = extractQueryParameters(query);

        if (!params.containsKey("recipeId") || !params.containsKey("userId")) {
            System.err.println("No recipeId parameter provided");
            sendResponse(httpExchange, 400, "No recipeId parameter provided");
            return;
        }

        String recipeId = params.get("recipeId");
        String userId = params.get("userId");

        DeleteResult deleteResult = MongoDBHelper.deleteRecipeFromCollection(recipeId);

        if (deleteResult == null || deleteResult.getDeletedCount() == 0) {
            System.err.println("Error deleting recipe from collection");
            sendResponse(httpExchange, 500, "Error deleting recipe from collection");
            return;
        }

        UpdateResult updateResult = MongoDBHelper.deleteRecipeFromUser(userId, recipeId);

        if (updateResult == null || updateResult.getModifiedCount() == 0) {
            System.err.println("Error deleting recipe from user");
            sendResponse(httpExchange, 500, "Error deleting recipe from user");
            return;
        }

        Logger.log("Deleted recipe (" + recipeId + ") from collection and user (" + userId + ")");

        // send recipeId back to client
        sendResponse(httpExchange, 200);
    }
}