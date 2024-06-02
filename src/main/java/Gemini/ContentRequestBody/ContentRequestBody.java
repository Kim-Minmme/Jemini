package Gemini.ContentRequestBody;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentRequestBody {
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