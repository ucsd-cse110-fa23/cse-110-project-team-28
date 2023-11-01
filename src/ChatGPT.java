import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "text-davinci-003";

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
         
        if (args.length != 2) {
            System.out.println("Usage: java ChatGPT <maxTokens> <prompt>");
            return;
        }

        int maxTokens;
        try {
            maxTokens = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("The maxTokens argument must be an integer.");
            return;
        }
        String prompt = args[1];

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", 1.0);

        // Create the HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        // Create the request object
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", String.format("Bearer %s", API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        JSONObject responseJson = new JSONObject(responseBody);

        JSONArray choices = responseJson.getJSONArray("choices");
        String generatedText = choices.getJSONObject(0).getString("text");

        System.out.println(generatedText);

    }
}