
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ExpenseTrackerGUI extends JFrame {

    private final String loggedInUsername; // To store the logged-in username
    private final ExpenseDAO expenseDAO;  // DAO instance for database operations

    // Constructor
    public ExpenseTrackerGUI(String username) {
        this.loggedInUsername = username;
        this.expenseDAO = new ExpenseDAO(); // Initialize DAO instance

        setTitle("Expense Tracker - Welcome, " + username);
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

        // Input Fields
        JTextField amountField = createStyledTextField();
        JComboBox<String> addCategory = new JComboBox<>(new String[]{"Food", "Rent", "Transport", "Others"});
        addCategory.setFont(new Font("Arial", Font.PLAIN, 14));
        addCategory.setBackground(Color.WHITE);

        JTextField descriptionField = createStyledTextField();
        JTextField dateField = createStyledTextField();

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
        addButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String category = addCategory.getSelectedItem().toString();
                String description = descriptionField.getText();
                String date = dateField.getText();
                expenseDAO.addExpense(amount, category, description, date, loggedInUsername); // Pass username
                JOptionPane.showMessageDialog(this, "Expense added successfully!");
                clearFields(amountField, descriptionField, dateField);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format");
            }
        });

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

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void openDataViewWindow() {
    JFrame dataViewFrame = new JFrame("Data View");
    dataViewFrame.setSize(700, 500);
    dataViewFrame.setLayout(new BorderLayout());
    dataViewFrame.getContentPane().setBackground(new Color(245, 245, 245));

    JTable expenseTable = new JTable();
    JScrollPane scrollPane = new JScrollPane(expenseTable);
    dataViewFrame.add(scrollPane, BorderLayout.CENTER);

    // Filter Panel
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
    filterPanel.setBackground(new Color(245, 245, 245));

    JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"All", "Food", "Rent", "Transport", "Others"});
    JComboBox<String> sortOrder = new JComboBox<>(new String[]{"ASC", "DESC"});
    JButton filterButton = createStyledButton("Filter");
    JButton addNewExpenseButton = createStyledButton("Add New Expense"); // Add button for navigation

    filterPanel.add(new JLabel("Category:"));
    filterPanel.add(categoryFilter);
    filterPanel.add(new JLabel("Sort by Date:"));
    filterPanel.add(sortOrder);
    filterPanel.add(filterButton);
    filterPanel.add(addNewExpenseButton); // Add the new button to the panel
    dataViewFrame.add(filterPanel, BorderLayout.NORTH);

    // Summary and Export Panel
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
    dataViewFrame.add(summaryPanel, BorderLayout.SOUTH);

    // Default data loading
    List<String[]> expenses = expenseDAO.getFilteredExpenses(loggedInUsername, "%", "ASC");
    updateTable(expenses, expenseTable);

    // Action Listeners
    filterButton.addActionListener(e -> filterExpenses(categoryFilter, sortOrder, expenseTable));
    summaryButton.addActionListener(e -> showMonthlySummary(monthField, yearField, totalLabel));
    exportButton.addActionListener(e -> exportExpenses());
    addNewExpenseButton.addActionListener(e -> {
        dataViewFrame.dispose(); // Close Data View window
        this.setVisible(true);  // Navigate back to Add Expense GUI
    });

    dataViewFrame.setLocationRelativeTo(this);
    dataViewFrame.setVisible(true);
}


    private void updateTable(List<String[]> data, JTable expenseTable) {
        String[] columns = {"ID", "Amount", "Category", "Description", "Date", "Action"};
        Object[][] tableData = new Object[data.size()][6];

        for (int i = 0; i < data.size(); i++) {
            tableData[i][0] = data.get(i)[0]; // ID
            tableData[i][1] = data.get(i)[1]; // Amount
            tableData[i][2] = data.get(i)[2]; // Category
            tableData[i][3] = data.get(i)[3]; // Description
            tableData[i][4] = data.get(i)[4]; // Date
            tableData[i][5] = "Delete";       // Button label
        }

        expenseTable.setModel(new javax.swing.table.DefaultTableModel(tableData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the "Action" column is editable
            }
        });

        // Apply button renderer and editor
        expenseTable.getColumnModel().getColumn(5).setCellRenderer(renderDeleteButton());
        expenseTable.getColumnModel().getColumn(5).setCellEditor(editDeleteButton(expenseTable));
    }

    private TableCellRenderer renderDeleteButton() {
        return (table, value, isSelected, hasFocus, row, column) -> {
            JButton button = new JButton("Delete");
            button.setOpaque(true);
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(255, 69, 0)); // Red button for delete
            return button;
        };
    }

    private TableCellEditor editDeleteButton(JTable expenseTable) {
        return new DefaultCellEditor(new JCheckBox()) {
            private final JButton button = new JButton("Delete");

            {
                button.setOpaque(true);
                button.setForeground(Color.WHITE);
                button.setBackground(new Color(255, 69, 0));
                button.addActionListener(e -> {
                    fireEditingStopped(); // Stop editing before performing the action
                    int selectedRow = expenseTable.getSelectedRow();
                    int expenseId = Integer.parseInt(expenseTable.getValueAt(selectedRow, 0).toString()); // Get ID
                    deleteExpense(expenseId, expenseTable);
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return button;
            }

        };
    }

    private void deleteExpense(int expenseId, JTable expenseTable) {
        this.setVisible(false);
        try {
            int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this expense?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                // Call DAO method to delete the expense
                expenseDAO.deleteExpense(expenseId, loggedInUsername);

                // Refresh the table after deletion
                List<String[]> updatedExpenses = expenseDAO.getFilteredExpenses(loggedInUsername, "%", "ASC");
                updateTable(updatedExpenses, expenseTable);
            }
        } finally {
//            this.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }

    private void filterExpenses(JComboBox<String> categoryFilter, JComboBox<String> sortOrder, JTable expenseTable) {
        // Get the selected category and sort order
        String category = categoryFilter.getSelectedItem().toString();
        String order = sortOrder.getSelectedItem().toString();

        // Validate sortOrder
        if (!"ASC".equalsIgnoreCase(order) && !"DESC".equalsIgnoreCase(order)) {
            order = "ASC"; // Default sort order
        }

        // Convert "All" to SQL wildcard
        String categoryFilterValue = category.equals("All") ? "%" : category;

        // Fetch filtered data using ExpenseDAO
        List<String[]> expenses = expenseDAO.getFilteredExpenses(loggedInUsername, categoryFilterValue, order);

        // Update the table with filtered data
        updateTable(expenses, expenseTable);

        // Inform the user if no data is found
        if (expenses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data found for the selected filter.");
        }
    }

    private void showMonthlySummary(JTextField monthField, JTextField yearField, JLabel totalLabel) {
        try {
            int month = Integer.parseInt(monthField.getText().trim());
            int year = Integer.parseInt(yearField.getText().trim());

            if (month < 1 || month > 12) {
                throw new IllegalArgumentException("Invalid month. Please enter a value between 1 and 12.");
            }

            double total = expenseDAO.getMonthlySummary(month, year, loggedInUsername);

            totalLabel.setText("Total: $" + total);
            if (total == 0) {
                JOptionPane.showMessageDialog(this, "No expenses found for the selected month and year.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for month and year.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportExpenses() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            expenseDAO.exportExpensesToCSV(filePath, loggedInUsername);

            JOptionPane.showMessageDialog(this, "Expenses exported successfully to: " + filePath);
        }
    }

}
