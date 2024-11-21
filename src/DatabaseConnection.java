
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = "1234";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL.");
        } catch (SQLException e) {
            System.out.println("Error connecting to MySQL: " + e.getMessage());
        }
        return conn;
    }
}
