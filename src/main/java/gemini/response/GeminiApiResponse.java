package gemini.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiApiResponse {
    private List<ResponseCandidate> candidates;

    public List<ResponseCandidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<ResponseCandidate> candidates) {
        this.candidates = candidates;
    }
}