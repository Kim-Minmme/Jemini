package db.models;

public class DiaryEntry {
    private int id;
    private String title;
    private String date;
    private String content;
    private int userId;

    public DiaryEntry(int id, String title, String date, String content, int userId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return title;
    }
}