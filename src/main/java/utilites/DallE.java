package utilites;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import config.Config;

public class DallE {
        public static String generateImageURL(String recipeName)
                        throws IOException, InterruptedException, URISyntaxException {

                // Set request parameters
                int n = 1;

                // Create a request body which you will pass into request object
                JSONObject requestBody = new JSONObject();
                requestBody.put("model", Config.getDallEModel());
                requestBody.put("prompt", Config.getDallEPrompt().replace("[recipeName]", recipeName));
                requestBody.put("n", n);
                requestBody.put("size", "512x512");
                requestBody.put("response_format", "b64_json");

                // create HTTP client
                HttpClient client = HttpClient.newHttpClient();

                // create the request object
                HttpRequest request = HttpRequest
                                .newBuilder()
                                .uri(URI.create(Config.getDallEApiEndpoint()))
                                .header("Content-Type", "application/json")
                                .header("Authorization", String.format("Bearer %s", Config.getOpenAiApiKey()))
                                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                                .build();

                // Send the request and receive the response
                HttpResponse<String> response = client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString());

                // Process the response
                String responseBody = response.body();

                JSONObject responseJSON = new JSONObject(responseBody);

                // System.out.println(responseBody);

                // Processing the response
                JSONArray data = responseJSON.getJSONArray("data");
                String generatedImageB64 = data.getJSONObject(0).getString("b64_json");

                return "data:image/png;base64," + generatedImageB64;

        }
}
