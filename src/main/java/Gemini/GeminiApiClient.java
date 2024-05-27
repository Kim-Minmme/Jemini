package Gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiApiClient {

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:streamGenerateContent";
    private static final String API_KEY = System.getenv("GOOGLE_API_KEY");

    public static void main(String[] args) {
        try {
            streamContent("Harry Potter.", new StreamContentCallback() {
                @Override
                public void onContentReceived(String content) {
                    System.out.println("Extracted Text: " + content);
                }

                @Override
                public void onError(Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void streamContent(String prompt, StreamContentCallback callback) throws IOException {
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
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
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

    private interface StreamContentCallback {
        void onContentReceived(String content);
        void onError(Exception e);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GeminiApiResponse {
        private java.util.List<ApiResponseCandidate> candidates;

        public java.util.List<ApiResponseCandidate> getCandidates() {
            return candidates;
        }

        public void setCandidates(java.util.List<ApiResponseCandidate> candidates) {
            this.candidates = candidates;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ApiResponseCandidate {
        private ApiResponseContent content;

        public ApiResponseContent getContent() {
            return content;
        }

        public void setContent(ApiResponseContent content) {
            this.content = content;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ApiResponseContent {
        private java.util.List<ApiResponsePart> parts;

        public java.util.List<ApiResponsePart> getParts() {
            return parts;
        }

        public void setParts(java.util.List<ApiResponsePart> parts) {
            this.parts = parts;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ApiResponsePart {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    static class ContentRequestBody {
        @JsonProperty("contents")
        private RequestContent[] contents;

        public ContentRequestBody(String text) {
            this.contents = new RequestContent[]{new RequestContent(text)};
        }

        public RequestContent[] getContents() {
            return contents;
        }

        public void setContents(RequestContent[] contents) {
            this.contents = contents;
        }
    }

    static class RequestContent {
        @JsonProperty("parts")
        private RequestPart[] parts;

        public RequestContent(String text) {
            this.parts = new RequestPart[]{new RequestPart(text)};
        }

        public RequestPart[] getParts() {
            return parts;
        }

        public void setParts(RequestPart[] parts) {
            this.parts = parts;
        }
    }

    static class RequestPart {
        @JsonProperty("text")
        private String text;

        public RequestPart(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}