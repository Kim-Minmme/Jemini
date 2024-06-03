package gemini.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseCandidate {
    private ApiResponseContent content;

    public ApiResponseContent getContent() {
        return content;
    }

    public void setContent(ApiResponseContent content) {
        this.content = content;
    }
}