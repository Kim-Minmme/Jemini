package Gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import Gemini.ContentRequestBody.ContentRequestBody;
import Gemini.GeminiApiResponse.ApiResponseCandidate;
import Gemini.GeminiApiResponse.GeminiApiResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiApiClient {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:streamGenerateContent";
    private static final String API_KEY = System.getenv("GOOGLE_API_KEY");

    public static void streamContent(String prompt, StreamContentCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(API_URL + "?alt=sse&key=" + API_KEY);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String json = new ObjectMapper().writeValueAsString(new ContentRequestBody(prompt));
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(json.getBytes());
                }

                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String jsonResponse = line.substring(6);
                        String extractedText = extractTextFromStreamResponse(jsonResponse);
                        callback.onContentReceived(extractedText);
                    }
                }
            } catch (IOException e) {
                if (callback != null) {
                    callback.onError(e);
                }
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private static String extractTextFromStreamResponse(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GeminiApiResponse response = mapper.readValue(jsonResponse, GeminiApiResponse.class);
        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            ApiResponseCandidate candidate = response.getCandidates().get(0);
            if (candidate != null && candidate.getContent() != null && candidate.getContent().getParts() != null && !candidate.getContent().getParts().isEmpty()) {
                return candidate.getContent().getParts().get(0).getText();
            }
        }
        return "";
    }
}