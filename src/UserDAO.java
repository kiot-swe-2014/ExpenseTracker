

import java.sql.*;

public class UserDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker_db";
    private static final String USER = "root";
    private static final String PASS = "1234"; 

    // Method for user registration
    public boolean registerUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
}

