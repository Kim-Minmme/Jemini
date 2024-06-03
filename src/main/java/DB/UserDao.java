package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UserDao {

    public boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
            }
            return false;
        } catch (SQLException e) {
            throw new SQLException("Failed to authenticate user", e);
        }
    }

    public boolean register(String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) { // SQLITE_CONSTRAINT
                return false;
            } else {
                throw new SQLException("Failed to register user", e);
            }
        }
    }

    public int getUserIdByUsername(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("User not found");
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to get user ID by username", e);
        }
    }
}