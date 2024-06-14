package gemini.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestBody {
    @JsonProperty("contents")
    private RequestContent[] contents;

    public RequestBody(String text) {
        this.contents = new RequestContent[]{new RequestContent(text)};
    }

    public RequestContent[] getContents() {
        return contents;
    }

    public void setContents(RequestContent[] contents) {
        this.contents = contents;
    }
}