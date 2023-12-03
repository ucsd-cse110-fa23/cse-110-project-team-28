package utilites;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URISyntaxException;

import org.bson.types.ObjectId;

import org.json.JSONObject;

import com.google.gson.Gson;

import config.Config;
import model.Recipe;

public class RecipeHelper {
    public static Recipe getRecipe(ObjectId objectId) throws IOException, URISyntaxException {

        URIBuilder uriBuilder = new URIBuilder();

        String URL = uriBuilder.setHost(Config.getServerHostname())
                .setPort(Config.getServerPort())
                .setPath("api/getrecipe")
                .addParameter("_id", objectId.toString())
                .build();

        System.out.println(URL);

        URL url = new URL(URL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();

        if (responseCode != 200) {
            return null;
        }

        InputStream inputStream = connection.getInputStream();
        String response = new String(inputStream.readAllBytes());
        connection.getInputStream().close();

        System.out.println(response);

        Gson gson = new Gson();

        Recipe recipe = gson.fromJson(response, Recipe.class);

        return recipe;
    }
}