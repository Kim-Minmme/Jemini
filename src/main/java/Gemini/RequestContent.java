package Gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestContent {
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