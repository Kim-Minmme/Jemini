package db;

import db.models.DiaryEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiaryEntryDao {

    public boolean createDiaryEntry(DiaryEntry diaryEntry) throws SQLException {
        String query = "INSERT INTO diary_entries (user_id, title, date, content) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, diaryEntry.getUserId());
            stmt.setString(2, diaryEntry.getTitle());
            stmt.setString(3, diaryEntry.getDate());
            stmt.setString(4, diaryEntry.getContent());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new SQLException("Failed to create diary entry", e);
        }
    }

    public DiaryEntry readDiaryEntry(int id) throws SQLException {
        String query = "SELECT * FROM diary_entries WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DiaryEntry(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getInt("user_id")
                );
            } else {
                throw new SQLException("Diary entry not found");
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to read diary entry", e);
        }
    }

    public boolean updateDiaryEntry(DiaryEntry diaryEntry) throws SQLException {
        String query = "UPDATE diary_entries SET title = ?, date = ?, content = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, diaryEntry.getTitle());
            stmt.setString(2, diaryEntry.getDate());
            stmt.setString(3, diaryEntry.getContent());
            stmt.setInt(4, diaryEntry.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Failed to update diary entry", e);
        }
    }

    public boolean deleteDiaryEntry(int id) throws SQLException {
        String query = "DELETE FROM diary_entries WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new SQLException("Failed to delete diary entry", e);
        }
    }

    public List<DiaryEntry> listDiaryEntriesByUser(int userId) throws SQLException {
        String query = "SELECT * FROM diary_entries WHERE user_id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<DiaryEntry> entries = new ArrayList<>();
            while (rs.next()) {
                entries.add(new DiaryEntry(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getInt("user_id")
                ));
            }
            return entries;
        } catch (SQLException e) {
            throw new SQLException("Failed to list diary entries by user", e);
        }
    }
}
