package utilites;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;

import config.Config;
import model.UserData;
import server.AuthResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthHelper {
        private static final String SIGNUP_PATH = "/auth/signup";
        private static final String LOGIN_PATH = "/auth/login";

        public static AuthResponse signup(String username, String password) throws IOException, URISyntaxException {
                return auth(username, password, SIGNUP_PATH);
        }

        public static AuthResponse login(String username, String password) throws IOException, URISyntaxException {
                return auth(username, password, LOGIN_PATH);
        }

        private static AuthResponse auth(String username, String password, String path) {
                HttpClient client = HttpClient.newHttpClient();

                JSONObject requestBody = new JSONObject();
                requestBody.put("username", username);
                requestBody.put("password", password);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(
                                                new URIBuilder()
                                                                .setHost(Config.getServerHostname())
                                                                .setPort(Config.getServerPort())
                                                                .setPath(path)
                                                                .build()))
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                                .header("Content-Type", "application/json")
                                .build();

                return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                                .thenApply(response -> {
                                        if (response.statusCode() != 200) {
                                                Logger.log("Received non-200 status code from server: "
                                                                + response.statusCode());
                                                return new AuthResponse()
                                                                .setSuccess(false)
                                                                .setMessage(response.body());
                                        }

                                        JSONObject responseBody = new JSONObject(response.body());

                                        UserData userData = new UserData()
                                                        .setUserId(responseBody.getString("userId"))
                                                        .setUsername(responseBody.getString("username"));

                                        return new AuthResponse()
                                                        .setSuccess(true)
                                                        .setUserData(userData);
                                })
                                .join();
        }
}