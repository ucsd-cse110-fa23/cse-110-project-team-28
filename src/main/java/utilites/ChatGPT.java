package utilites;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import config.Config;

public class ChatGPT {
    public static String getGPTResponse(int maxTokens, String prompt)
            throws IOException, InterruptedException, URISyntaxException {

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("model", Config.getChatGPTModel());
            requestBody.put("prompt", prompt);
            requestBody.put("max_tokens", maxTokens);
            requestBody.put("temperature", 1.0);

            // Create the HTTP Client
            HttpClient client = HttpClient.newHttpClient();

            // Create the request object
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(Config.getChatGPTApiEndpoint()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", String.format("Bearer %s", Config.getOpenAiApiKey()))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JSONObject responseJson = new JSONObject(responseBody);

            JSONArray choices = responseJson.getJSONArray("choices");
            String generatedText = choices.getJSONObject(0).getString("text");

            return generatedText;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}