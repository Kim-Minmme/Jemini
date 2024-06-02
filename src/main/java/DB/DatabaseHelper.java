package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:diary.db";

    public static Connection connect() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database", e);
        }
    }

    public static void createTables() throws SQLException {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL" +
                    ");";
            String createEntriesTable = "CREATE TABLE IF NOT EXISTS diary_entries (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "date TEXT," +
                    "content TEXT," +
                    "tags TEXT," +
                    "mood TEXT," +
                    "FOREIGN KEY(user_id) REFERENCES users(id)" +
                    ");";
            stmt.execute(createUsersTable);
            stmt.execute(createEntriesTable);
        } catch (SQLException e) {
            throw new SQLException("Failed to create tables", e);
        }
    }
}