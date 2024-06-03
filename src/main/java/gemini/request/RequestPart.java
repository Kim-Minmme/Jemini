package gemini.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestPart {
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