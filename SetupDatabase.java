
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class SetupDatabase {
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS expenses (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                     "amount DECIMAL(10, 2) NOT NULL," +
                     "category VARCHAR(50) NOT NULL," +
                     "description TEXT," +
                     "date DATE NOT NULL)";
        
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Expenses table created in MySQL.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        createTable();
    }
}

