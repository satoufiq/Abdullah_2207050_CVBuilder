package CVBuilder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String DB_FILE = "cvbuilder.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;
    private static DBUtil instance;

    private DBUtil() {}

    public static synchronized DBUtil getInstance() {
        if (instance == null) {
            instance = new DBUtil();
            instance.init();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS cv (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fullName TEXT NOT NULL,
                email TEXT,
                phone TEXT,
                address TEXT,
                profileImageURI TEXT,
                educations TEXT,
                skills TEXT,
                experiences TEXT,
                projects TEXT,
                created_at DATETIME DEFAULT (datetime('now'))
            );
            """;
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
