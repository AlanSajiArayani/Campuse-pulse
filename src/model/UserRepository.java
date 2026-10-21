package model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserRepository {

    private static final String DEFAULT_DB_PATH = "data/campuspulse.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ignored) {
            // SQLite JDBC driver will load via ServiceLoader if present
        }
    }

    private final String dbPath;

    public UserRepository() {
        this(System.getProperty("campuspulse.db.path", DEFAULT_DB_PATH));
    }

    public UserRepository(String dbPath) {
        this.dbPath = (dbPath == null || dbPath.trim().isEmpty())
                ? System.getProperty("campuspulse.db.path", DEFAULT_DB_PATH)
                : dbPath.trim();
        initSchema();
    }

    public String getDbPath() {
        return dbPath;
    }

    private Connection getConnection() throws SQLException {
        if (!":memory:".equals(dbPath)) {
            File dbFile = new File(dbPath);
            File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs() && !parentDir.exists()) {
                    throw new SQLException("Failed to create database parent directory: " + parentDir.getAbsolutePath());
                }
            }
        }
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public void initSchema() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id TEXT PRIMARY KEY, "
                + "name TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE COLLATE NOCASE, "
                + "phone TEXT, "
                + "password TEXT NOT NULL, "
                + "role TEXT NOT NULL, "
                + "is_active INTEGER NOT NULL DEFAULT 1"
                + ");";

        String createIndexSql = "CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSql);
            stmt.executeUpdate(createIndexSql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database schema: " + e.getMessage(), e);
        }
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        String sql = "INSERT INTO users (user_id, name, email, phone, password, role, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId().trim());
            pstmt.setString(2, user.getName() != null ? user.getName() : "");
            pstmt.setString(3, user.getEmail().trim());
            pstmt.setString(4, user.getPhone() != null ? user.getPhone() : "");
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getRole() != null ? user.getRole() : "STUDENT");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to persist user: " + e.getMessage(), e);
        }
    }

    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, name, email, phone, password, role "
                + "FROM users WHERE email = ? COLLATE NOCASE";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query user by email: " + e.getMessage(), e);
        }
        return null;
    }

    public User findById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, name, email, phone, password, role "
                + "FROM users WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to query user by id: " + e.getMessage(), e);
        }
        return null;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT user_id, name, email, phone, password, role FROM users ORDER BY rowid ASC";
        List<User> list = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all users: " + e.getMessage(), e);
        }
        return Collections.unmodifiableList(list);
    }

    public boolean deactivateUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        String sql = "UPDATE users SET is_active = 0 WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId.trim());
            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to deactivate user: " + e.getMessage(), e);
        }
    }

    public boolean isUserActive(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        String sql = "SELECT is_active FROM users WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId.trim());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("is_active") == 1;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if user is active: " + e.getMessage(), e);
        }
        return false;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password"),
                rs.getString("role")
        );
    }
}
