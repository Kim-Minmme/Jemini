package DB;

import DB.Models.DiaryEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiaryEntryDao {

    public void createEntry(DiaryEntry entry) throws SQLException {
        String query = "INSERT INTO diary_entries (user_id, date, content, tags, mood) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, entry.getUserId());
            stmt.setString(2, entry.getDate());
            stmt.setString(3, entry.getContent());
            stmt.setString(4, entry.getTags());
            stmt.setString(5, entry.getMood());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to create diary entry", e);
        }
    }

    public DiaryEntry readEntry(int id) throws SQLException {
        String query = "SELECT * FROM diary_entries WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DiaryEntry(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getString("tags"),
                        rs.getString("mood")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Failed to read diary entry", e);
        }
    }

    public List<DiaryEntry> readAllEntries(int userId) throws SQLException {
        String query = "SELECT * FROM diary_entries WHERE user_id = ?";
        List<DiaryEntry> entries = new ArrayList<>();
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(new DiaryEntry(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getString("tags"),
                        rs.getString("mood")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to read all diary entries", e);
        }
        return entries;
    }

    public void updateEntry(DiaryEntry entry) throws SQLException {
        String query = "UPDATE diary_entries SET date = ?, content = ?, tags = ?, mood = ? WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, entry.getDate());
            stmt.setString(2, entry.getContent());
            stmt.setString(3, entry.getTags());
            stmt.setString(4, entry.getMood());
            stmt.setInt(5, entry.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update diary entry", e);
        }
    }

    public void deleteEntry(int id) throws SQLException {
        String query = "DELETE FROM diary_entries WHERE id = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to delete diary entry", e);
        }
    }
}