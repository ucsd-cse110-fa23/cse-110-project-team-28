package utilites;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URISyntaxException;

import org.json.JSONObject;

import com.google.gson.Gson;
import config.Config;
import model.UserData;

public class AuthHelper {
    private static final String SIGNUP_PATH = "/auth/signup";
    private static final String LOGIN_PATH = "/auth/login";

    public static AuthResponse signup(String username, String password) throws IOException, URISyntaxException {
        return auth(username, password, SIGNUP_PATH);
    }

    public static AuthResponse login(String username, String password) throws IOException, URISyntaxException {
        return auth(username, password, LOGIN_PATH);
    }

    private static AuthResponse auth(String username, String password, String path)
            throws IOException, URISyntaxException {

        URIBuilder uriBuilder = new URIBuilder();

        String URL = uriBuilder.setHost(Config.getServerHostname())
                .setPort(Config.getServerPort())
                .setPath(path)
                .build();

        System.out.println(URL);

        URL url = new URL(URL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        connection.getOutputStream().write(requestBody.toString().getBytes());
        connection.getOutputStream().close();

        int responseCode = connection.getResponseCode();

        if (responseCode != 200) {
            InputStream errorStream = connection.getErrorStream();
            String response = new String(errorStream.readAllBytes());
            connection.getErrorStream().close();

            return new AuthResponse()
                    .setSuccess(false)
                    .setMessage(response);
        }

        InputStream inputStream = connection.getInputStream();
        String response = new String(inputStream.readAllBytes());
        connection.getInputStream().close();

        System.out.println(response);

        Gson gson = new Gson();

        UserData userData = gson.fromJson(response, UserData.class);

        return new AuthResponse()
                .setSuccess(true)
                .setUserData(userData);
    }
}
