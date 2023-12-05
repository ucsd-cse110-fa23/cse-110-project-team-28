package utilites;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import utilites.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class DallE {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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";
    private static final String MODEL = "dall-e-2";
    private static final String imagePromptTemplate = "[recipeName], shallow-focus, 35mm, Canon EOS 5D Mark IV DSLR, f/5.6 aperture, 1/125 second shutter speed, ISO 100 --ar 2:3 --q 2 --v 4";

    public static String generateImageURL(String recipeName) throws IOException, InterruptedException, URISyntaxException {
        
        //Set request parameters
        int n = 1;

        //Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", imagePromptTemplate.replace("[recipeName]", recipeName));
        requestBody.put("n", n);
        requestBody.put("size", "512x512");
        requestBody.put("response_format", "b64_json");

        //create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        //create the request object
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create(API_ENDPOINT))
            .header("Content-Type", "application/json")
            .header("Authorization", String.format("Bearer %s", API_KEY))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
            .build();

        //Send the request and receive the response
        HttpResponse<String> response = client.send(
            request,
            HttpResponse.BodyHandlers.ofString()
        );

        //Process the response
        String responseBody = response.body();

        JSONObject responseJSON = new JSONObject(responseBody);

        //System.out.println(responseBody);

        //Processing the response
        JSONArray data = responseJSON.getJSONArray("data");
        String generatedImageB64 = data.getJSONObject(0).getString("b64_json");

        return "data:image/png;base64," + generatedImageB64;

        
    }
}
