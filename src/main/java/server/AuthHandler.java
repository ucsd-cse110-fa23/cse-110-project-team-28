package server;

import java.io.IOException;
import java.io.InputStream;

import org.bson.Document;
import org.bson.types.ObjectId;
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

    public void loginHandler(HttpExchange exchange) throws IOException {
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
            exchange.close();
            return;
        }

        // find user
        Document userDocument = MongoDBHelper.findUser(username, password);

        // if user does not exist
        if (userDocument == null) {
            exchange.sendResponseHeaders(401, 0);
            exchange.close();
            return;
        }

        // send user's ObjectId as response
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(userDocument.getObjectId("_id").toString().getBytes());
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
            exchange.close();
            return;
        }

        // if username already exists
        if (MongoDBHelper.doesUsernameExist(username)) {
            exchange.sendResponseHeaders(409, 0);
            exchange.close();
            return;
        }

        // create user
        ObjectId userObjectId = MongoDBHelper.insertUser(username, password);

        // send userObjectId as response
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(userObjectId.toString().getBytes());
        exchange.close();
    }

}
