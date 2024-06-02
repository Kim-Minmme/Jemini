package Gemini;

public interface StreamContentCallback {
    void onContentReceived(String content);
    void onError(Exception e);
}