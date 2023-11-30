package server;

import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.Recipe;
import utilites.MongoDBHelper;

public class AuthHandler implements HttpHandler {

    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();

        // handle only post requests
        if (!requestMethod.equals("POST")) {
            // send 405 Method Not Allowed response
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
            return;
        }

        String requestPath = exchange.getRequestURI().getPath();
        String httpContext = exchange.getHttpContext().getPath();
        String path = requestPath.substring(httpContext.length());

        switch (path) {
            case "login":
                loginHandler(exchange);
                break;
            case "signup":
                signupHandler(exchange);
                break;
            default:
                break;
        }
    }

    public void loginHandler(HttpExchange exchange) throws IOException {
        // read request body
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes());

        // parse request body
        JSONObject requestBodyJSON = new JSONObject(requestBody);

        String username = requestBodyJSON.getString("username");
        String password = requestBodyJSON.getString("password");

        // check not null in case frontend validation fails
        if (username == null || password == null) {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().write("Invalid username or password.".getBytes());
            exchange.close();
            return;
        }

        // find user
        Document userDocument = MongoDBHelper.findUser(username, password);

        // if user does not exist
        if (userDocument == null) {
            exchange.sendResponseHeaders(401, 0);
            exchange.getResponseBody().write("Invalid username or password.".getBytes());
            exchange.close();
            return;
        }

        JSONObject response = new JSONObject();
        response.put("_id", userDocument.getObjectId("_id").toString());
        response.put("username", userDocument.getString("username"));
        response.put("recipes", new JSONArray(userDocument.getList("recipes", String.class)));

        // send user's ObjectId as response
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(response.toString().getBytes());
        exchange.close();
    }

    public void signupHandler(HttpExchange exchange) throws IOException {
        // read request body
        InputStream inputStream = exchange.getRequestBody();
        String requestBody = new String(inputStream.readAllBytes());

        // parse request body
        JSONObject requestBodyJSON = new JSONObject(requestBody);

        String username = requestBodyJSON.getString("username");
        String password = requestBodyJSON.getString("password");

        // check not null
        if (username == null || password == null) {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().write("Invalid username or password.".getBytes());
            exchange.close();
            return;
        }

        System.out.println("username: " + username);
        System.out.println("password: " + password);

        // if username already exists
        if (MongoDBHelper.doesUsernameExist(username)) {
            exchange.sendResponseHeaders(409, 0);
            exchange.getResponseBody().write("Username already exists.".getBytes());
            exchange.close();
            return;
        }

        // create user
        String userId = MongoDBHelper.insertUser(username, password);

        JSONObject response = new JSONObject();
        response.put("_id", userId);
        response.put("username", username);
        response.put("recipes", new JSONArray());

        // send userObjectId as response
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(response.toString().getBytes());
        exchange.close();
    }

}
