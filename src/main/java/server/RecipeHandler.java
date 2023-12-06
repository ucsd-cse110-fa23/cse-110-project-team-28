package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.Recipe;
import utilites.Logger;
import utilites.MongoDBHelper;

public class RecipeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestPath = httpExchange.getRequestURI().getPath();
        String httpContext = httpExchange.getHttpContext().getPath();
        String recipeId = requestPath.substring(httpContext.length());

        Logger.log("Received request (uri: " + httpExchange.getRequestURI().toString() + ")");

        Recipe recipe;

        try {
            recipe = MongoDBHelper.findRecipeById(recipeId);
        } catch (IllegalArgumentException e) {
            Logger.log("Recipe not found");
            send404Page(httpExchange);
            return;
        }

        if (recipe == null) {
            Logger.log("Recipe not found");
            send404Page(httpExchange);
            return;
        }

        Logger.log("Recipe found (id: " + recipeId + ")");

        // now you have a valid recipe

        // for example, i will read the simple html template i created and replace the
        // placeholders with the recipe's data

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(RecipeHandler.class.getResourceAsStream("/html/recipeTemplate.html"), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();

        String html = stringBuilder.toString();

        html = html.replace("{{recipeName}}", recipe.getName())
                    .replace("{{mealType}}", recipe.getMealType())
                    .replace("{{recipeIngredients}}", recipe.getIngredients())
                    .replace("{{recipeSteps}}", recipe.getSteps())
                    .replace("{{recipeImage}}", recipe.getImageURL());
        /**
         * 
         * todo: add other placeholders and replace them with the recipe's data
         * 
         */


        sendResponse(httpExchange, 200, html, "text/html");
    }

    private void send404Page(HttpExchange exchange) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(RecipeHandler.class.getResourceAsStream("/html/404.html"), "UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();

        sendResponse(exchange, 404, stringBuilder.toString(), "text/html");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseBody, String contentType)
            throws IOException {
        Logger.log("Sending response (status code: " + statusCode + ", length: " + responseBody.length()
                + ", content type: " + contentType + ")");
        exchange.getResponseHeaders().add("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, 0);
        exchange.getResponseBody().write(responseBody.getBytes());
        exchange.close();
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

}
