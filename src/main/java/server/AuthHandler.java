package server;

import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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

    private void sendResponse(HttpExchange exchange, int statusCode, String responseBody) throws IOException {
        exchange.sendResponseHeaders(statusCode, responseBody.length());
        exchange.getResponseBody().write(responseBody.getBytes());
        exchange.close();
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
            sendResponse(exchange, 400, "Invalid username or password.");
            return;
        }

        // find user
        Document userDocument = MongoDBHelper.findUserByUsernamePassword(username, password);

        // if user does not exist
        if (userDocument == null) {
            sendResponse(exchange, 401, "Invalid username or password.");
            return;
        }

        JSONObject responseBody = new JSONObject();
        responseBody.put("userId", userDocument.getObjectId("_id").toString());
        responseBody.put("username", userDocument.getString("username"));

        // send user's ObjectId as response
        sendResponse(exchange, 200, responseBody.toString());
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
            sendResponse(exchange, 400, "Invalid username or password.");
            return;
        }

        // if username already exists
        if (MongoDBHelper.doesUsernameExist(username)) {
            sendResponse(exchange, 409, "Username already exists.");
            return;
        }

        // create user
        String userId = MongoDBHelper.insertUser(username, password);

        JSONObject responseBody = new JSONObject();
        responseBody.put("userId", userId);
        responseBody.put("username", username);

        // send userObjectId as response
        sendResponse(exchange, 200, responseBody.toString());
    }

}