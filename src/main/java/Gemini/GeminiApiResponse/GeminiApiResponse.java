package Gemini.GeminiApiResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiApiResponse {
    private List<ApiResponseCandidate> candidates;

    public List<ApiResponseCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<ApiResponseCandidate> candidates) {
        this.candidates = candidates;
    }
}