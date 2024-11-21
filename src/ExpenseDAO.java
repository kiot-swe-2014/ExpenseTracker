

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

    // Method to fetch filtered expenses from the database
    public List<String[]> getFilteredExpenses(String category, String sortOrder) {
        List<String[]> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses WHERE category LIKE ? ORDER BY date " + sortOrder;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] expense = new String[5];
                expense[0] = String.valueOf(resultSet.getInt("id"));
                expense[1] = String.valueOf(resultSet.getDouble("amount"));
                expense[2] = resultSet.getString("category");
                expense[3] = resultSet.getString("description");
                expense[4] = resultSet.getString("date");
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    // Method to fetch monthly summary (total amount spent in a specific month and year)
    public double getMonthlySummary(int month, int year) {
        double total = 0;
        String query = "SELECT SUM(amount) FROM expenses WHERE MONTH(date) = ? AND YEAR(date) = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, month);
            statement.setInt(2, year);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

   
}




