package server;

import com.sun.net.httpserver.*;

import model.Recipe;
import model.RecipeData;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;

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

        // update the recipe in RecipeData
        int status = RecipeData.getInstance().updateRecipe(recipe);

        if (status == -1) {
            return "Recipe not found";
        }

        return "Recipe successfully updated";
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
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

        // delete recipe with matching name from RecipeData
        int status = RecipeData.getInstance().deleteRecipe(buffer.toString());

        if (status == -1) {
            return "Recipe not found";
        }

        return "Recipe successfully deleted";
    }

    private String handleGet(HttpExchange httpExchange) {
        // send all recipes from RecipeData as a JSON string
        ArrayList<Recipe> recipeData = RecipeData.getInstance().getRecipes();

        Gson gson = new Gson();

        String response = gson.toJson(recipeData);

        return response;
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
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

        // add the recipe to RecipeData
        RecipeData.getInstance().addRecipe(recipe);

        return "Recipe successfully added";
    }

}