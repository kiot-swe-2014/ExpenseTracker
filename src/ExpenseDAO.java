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

    // Add a new expense for a user
    public void addExpense(double amount, String category, String description, String date, String username) {
        String query = "INSERT INTO expenses (amount, category, description, date, username) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDouble(1, amount);
            statement.setString(2, category);
            statement.setString(3, description);
            statement.setString(4, date);
            statement.setString(5, username);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
        }
    }

    // Fetch filtered expenses for a user
    public List<String[]> getFilteredExpenses(String username, String category, String sortOrder) {
    List<String[]> expenses = new ArrayList<>();

    // Validate sortOrder
    if (!"ASC".equalsIgnoreCase(sortOrder) && !"DESC".equalsIgnoreCase(sortOrder)) {
        sortOrder = "ASC"; // Default to ascending order
    }

    // SQL query
    String query = "SELECT * FROM expenses WHERE username = ? AND category LIKE ? ORDER BY date " + sortOrder;

    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement statement = connection.prepareStatement(query)) {

        // Set parameters
        statement.setString(1, username);  // Correctly set username
        statement.setString(2, category); // Correctly set category

        System.out.println("Executing query: " + statement.toString()); // Debugging output
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

        System.out.println("Fetched " + expenses.size() + " rows."); // Debug output
    } catch (SQLException e) {
        System.err.println("Error fetching filtered expenses: " + e.getMessage());
    }

    return expenses;
}



    // Get monthly summary for a user
    public double getMonthlySummary(int month, int year, String username) {
        double total = 0;
        String query = "SELECT SUM(amount) FROM expenses WHERE MONTH(date) = ? AND YEAR(date) = ? AND username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, month);
            statement.setInt(2, year);
            statement.setString(3, username);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching monthly summary: " + e.getMessage());
        }

        return total;
    }
    
    public void deleteExpense(int expenseId, String username) {
    String query = "DELETE FROM expenses WHERE id = ? AND username = ?";

    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, expenseId);
        statement.setString(2, username);

        int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("No expense found with the given ID and username.");
        }
    } catch (SQLException e) {
        System.err.println("Error deleting expense: " + e.getMessage());
    }
}

    // Export expenses for a user to CSV
    public void exportExpensesToCSV(String filePath, String username) {
        String query = "SELECT * FROM expenses WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery();
                 BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

                writer.write("ID,Amount,Category,Description,Date\n");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    double amount = resultSet.getDouble("amount");
                    String category = escapeCSV(resultSet.getString("category"));
                    String description = escapeCSV(resultSet.getString("description"));
                    String date = resultSet.getString("date");

                    writer.write(id + "," + amount + "," + category + "," + description + "," + date + "\n");
                }
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error exporting expenses to CSV: " + e.getMessage());
        }
    }

    // Helper method to escape special characters in CSV
    private String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
