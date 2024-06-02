package Gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseContent {
    private List<ApiResponsePart> parts;

    public List<ApiResponsePart> getParts() {
        return parts;
    }

    public void setParts(List<ApiResponsePart> parts) {
        this.parts = parts;
    }
}