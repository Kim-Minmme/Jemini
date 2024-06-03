package db.models;

public class DiaryEntry {
    private int id;
    private int userId;
    private String date;
    private String content;
    private String tags;
    private String mood;

    public DiaryEntry(int id, int userId, String date, String content, String tags, String mood) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.content = content;
        this.tags = tags;
        this.mood = mood;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}