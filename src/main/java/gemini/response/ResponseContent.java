package gemini.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseContent {
    private List<ResponsePart> parts;

    public List<ResponsePart> getParts() {
        return parts;
    }

    public void setParts(List<ResponsePart> parts) {
        this.parts = parts;
    }
}