

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense_tracker_db";
    private static final String USER = "root";
    private static final String PASS = "1234";

    // Method to add a new expense to the database
    public void addExpense(double amount, String category, String description, String date) {
        String query = "INSERT INTO expenses (amount, category, description, date) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, amount);
            statement.setString(2, category);
            statement.setString(3, description);
            statement.setString(4, date);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}




