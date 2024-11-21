
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ExpenseTrackerGUI extends JFrame {
    private ExpenseDAO expenseDAO;

    // Fields for Add Expense
    private JTextField amountField, descriptionField, dateField;
    private JComboBox<String> addCategory;

    public ExpenseTrackerGUI() {
        expenseDAO = new ExpenseDAO();

        setTitle("Expense Tracker");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel for Adding Expenses
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        addPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add New Expense");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        
        amountField = createStyledTextField();
        addCategory = new JComboBox<>(new String[] {"Food", "Rent", "Transport", "Others"});
        addCategory.setFont(new Font("Arial", Font.PLAIN, 14));
        addCategory.setBackground(Color.WHITE);
        
        descriptionField = createStyledTextField();
        dateField = createStyledTextField();
        
        JButton addButton = createStyledButton("Add Expense");
        JButton viewExpensesButton = createStyledButton("View Expenses");

        gbc.gridy = 1;
        addPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        addPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        addPanel.add(addCategory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        addPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        addPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        addPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addPanel.add(addButton, gbc);

        gbc.gridy = 6;
        addPanel.add(viewExpensesButton, gbc);

        add(addPanel, BorderLayout.CENTER);

        // Action Listeners
        addButton.addActionListener(e -> addExpense());
        viewExpensesButton.addActionListener(e -> openDataViewWindow());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Helper method for creating styled text fields
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(12);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 5, 5, 5)));
        return textField;
    }

    // Helper method for creating styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void addExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String category = addCategory.getSelectedItem().toString();
            String description = descriptionField.getText();
            String date = dateField.getText();
            expenseDAO.addExpense(amount, category, description, date);
            JOptionPane.showMessageDialog(this, "Expense added successfully!");
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format");
        }
    }

    private void clearFields() {
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText("");
    }

    // Opens the Data View window
    private void openDataViewWindow() {
        JFrame dataViewFrame = new JFrame("Data View");
        dataViewFrame.setSize(700, 500);
        dataViewFrame.setLayout(new BorderLayout());
        dataViewFrame.getContentPane().setBackground(new Color(245, 245, 245));

        // Filter Panel at the top
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(245, 245, 245));
        
        JComboBox<String> categoryFilter = new JComboBox<>(new String[] {"All", "Food", "Rent", "Transport", "Others"});
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JComboBox<String> sortOrder = new JComboBox<>(new String[] {"ASC", "DESC"});
        sortOrder.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton filterButton = createStyledButton("Filter");

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Sort by Date:"));
        filterPanel.add(sortOrder);
        filterPanel.add(filterButton);

        // Table to display expenses
        JTable expenseTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(expenseTable);

        // Summary and Export Panel at the bottom
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        summaryPanel.setBackground(new Color(245, 245, 245));
        
        JTextField monthField = new JTextField(5);
        JTextField yearField = new JTextField(5);
        
        JButton summaryButton = createStyledButton("View Monthly Summary");
        JLabel totalLabel = new JLabel("Total: $0.00");
        JButton exportButton = createStyledButton("Export to CSV");

        summaryPanel.add(new JLabel("Month:"));
        summaryPanel.add(monthField);
        summaryPanel.add(new JLabel("Year:"));
        summaryPanel.add(yearField);
        summaryPanel.add(summaryButton);
        summaryPanel.add(totalLabel);
        summaryPanel.add(exportButton);

        // Adding components to the Data View frame
        dataViewFrame.add(filterPanel, BorderLayout.NORTH);
        dataViewFrame.add(scrollPane, BorderLayout.CENTER);
        dataViewFrame.add(summaryPanel, BorderLayout.SOUTH);

        // Display expenses by default
        List<String[]> expenses = expenseDAO.getFilteredExpenses("%", "ASC");
        updateTable(expenses, expenseTable);

        dataViewFrame.setLocationRelativeTo(this);
        dataViewFrame.setVisible(true);

        // Action Listeners for Data View frame buttons
        filterButton.addActionListener(e -> filterExpenses(categoryFilter, sortOrder, expenseTable));
        summaryButton.addActionListener(e -> showMonthlySummary(monthField, yearField, totalLabel));
        exportButton.addActionListener(e -> exportExpenses());
    }

    private void filterExpenses(JComboBox<String> categoryFilter, JComboBox<String> sortOrder, JTable expenseTable) {
        String category = categoryFilter.getSelectedItem().toString();
        String order = sortOrder.getSelectedItem().toString();
        List<String[]> expenses = expenseDAO.getFilteredExpenses(category.equals("All") ? "%" : category, order);
        updateTable(expenses, expenseTable);
    }

    private void showMonthlySummary(JTextField monthField, JTextField yearField, JLabel totalLabel) {
        try {
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());
            double total = expenseDAO.getMonthlySummary(month, year);
            totalLabel.setText("Total: $" + total);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid month or year format");
        }
    }

    private void exportExpenses() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            expenseDAO.exportExpensesToCSV(filePath);
            JOptionPane.showMessageDialog(this, "Expenses exported successfully!");
        }
    }

    private void updateTable(List<String[]> data, JTable expenseTable) {
        String[] columns = {"ID", "Amount", "Category", "Description", "Date"};
        String[][] tableData = data.toArray(new String[0][]);
        expenseTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columns));
    }
        
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
}

}



