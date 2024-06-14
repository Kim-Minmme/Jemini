package gemini.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseCandidate {
    private ResponseContent content;

    public ResponseContent getContent() {
        return content;
    }

    public void setContent(ResponseContent content) {
        this.content = content;
    }
}