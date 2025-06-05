import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class CompanyRegistration extends JFrame {

    private DefaultTableModel tableModel;
    private JTable companyTable;

    public CompanyRegistration() {
        setTitle("Company Registration");
        setSize(900, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField roleField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField minCGPAField = new JTextField();
        JTextField min10thField = new JTextField();
        JTextField min12thField = new JTextField();

        JButton registerBtn = new JButton("Register Company");
        JButton viewBtn = new JButton("View Registered Companies");
        JButton updateBtn = new JButton("Update Selected Company");

        // Add all input labels and fields to form panel
        formPanel.add(new JLabel("Company Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Job Role:")); formPanel.add(roleField);
        formPanel.add(new JLabel("Salary:")); formPanel.add(salaryField);
        formPanel.add(new JLabel("Location:")); formPanel.add(locationField);
        formPanel.add(new JLabel("Min CGPA:")); formPanel.add(minCGPAField);
        formPanel.add(new JLabel("Min 10th %:")); formPanel.add(min10thField);
        formPanel.add(new JLabel("Min 12th %:")); formPanel.add(min12thField);
        formPanel.add(registerBtn); formPanel.add(viewBtn);
        formPanel.add(updateBtn);

        add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        companyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(companyTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create bottom panel to hold Back button aligned left
        JButton backBtn = new JButton("Back");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Clear fields helper
        Runnable clearFields = () -> {
            nameField.setText(""); emailField.setText(""); roleField.setText(""); salaryField.setText("");
            locationField.setText(""); minCGPAField.setText(""); min10thField.setText(""); min12thField.setText("");
        };

        // Register Company action
        registerBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String role = roleField.getText().trim();
                String salaryText = salaryField.getText().trim();
                String location = locationField.getText().trim();
                String cgpaText = minCGPAField.getText().trim();
                String tenthText = min10thField.getText().trim();
                String twelfthText = min12thField.getText().trim();

                if (!validateInputs(name, email, role, salaryText, location, cgpaText, tenthText, twelfthText)) return;

                double salary = Double.parseDouble(salaryText);
                double cgpa = Double.parseDouble(cgpaText);
                double tenth = Double.parseDouble(tenthText);
                double twelfth = Double.parseDouble(twelfthText);

                Connection conn = DBConnection.connect();
                String sql = "INSERT INTO companies (name, email, job_role, salary, location, min_cgpa, min_10th, min_12th) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, role);
                ps.setDouble(4, salary);
                ps.setString(5, location);
                ps.setDouble(6, cgpa);
                ps.setDouble(7, tenth);
                ps.setDouble(8, twelfth);
                ps.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Company registered successfully!");
                clearFields.run();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // View Registered Companies action
        viewBtn.addActionListener(e -> {
            try {
                Connection conn = DBConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM companies");

                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);
                tableModel.addColumn("Name");
                tableModel.addColumn("Email");
                tableModel.addColumn("Job Role");
                tableModel.addColumn("Salary");
                tableModel.addColumn("Location");
                tableModel.addColumn("Min CGPA");
                tableModel.addColumn("Min 10th %");
                tableModel.addColumn("Min 12th %");

                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("job_role"),
                            rs.getDouble("salary"),
                            rs.getString("location"),
                            rs.getDouble("min_cgpa"),
                            rs.getDouble("min_10th"),
                            rs.getDouble("min_12th")
                    });
                }
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to load data.");
            }
        });

        // Populate form when selecting table row
        companyTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = companyTable.getSelectedRow();
            if (selectedRow >= 0) {
                nameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                roleField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                salaryField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                locationField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                minCGPAField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                min10thField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                min12thField.setText(tableModel.getValueAt(selectedRow, 7).toString());
            }
        });

        // Update selected company action
        updateBtn.addActionListener(e -> {
            int selectedRow = companyTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a company to update.");
                return;
            }

            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String role = roleField.getText().trim();
                String salaryText = salaryField.getText().trim();
                String location = locationField.getText().trim();
                String cgpaText = minCGPAField.getText().trim();
                String tenthText = min10thField.getText().trim();
                String twelfthText = min12thField.getText().trim();

                if (!validateInputs(name, email, role, salaryText, location, cgpaText, tenthText, twelfthText)) return;

                double salary = Double.parseDouble(salaryText);
                double cgpa = Double.parseDouble(cgpaText);
                double tenth = Double.parseDouble(tenthText);
                double twelfth = Double.parseDouble(twelfthText);

                Connection conn = DBConnection.connect();
                String sql = "UPDATE companies SET job_role=?, salary=?, location=?, min_cgpa=?, min_10th=?, min_12th=? WHERE name=? AND email=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, role);
                ps.setDouble(2, salary);
                ps.setString(3, location);
                ps.setDouble(4, cgpa);
                ps.setDouble(5, tenth);
                ps.setDouble(6, twelfth);
                ps.setString(7, name);
                ps.setString(8, email);
                ps.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Company details updated successfully!");
                clearFields.run();

                // Refresh table
                viewBtn.doClick();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage());
            }
        });

        // Back button action - close this form and open HomePage
        backBtn.addActionListener(_ -> {
            dispose();      // close current window
            new HomePage();      // open HomePage window
        });

        setVisible(true);
    };

    private boolean validateInputs(String name, String email, String role, String salaryText, String location, String cgpaText, String tenthText, String twelfthText) {
        if (!Pattern.matches("^[A-Za-z ]+$", name)) {
            JOptionPane.showMessageDialog(this, "Company name must contain only letters and spaces.");
            return false;
        }

        if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", email)) {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z ]+$", role)) {
            JOptionPane.showMessageDialog(this, "Job role must contain only letters and spaces.");
            return false;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            if (salary < 0) {
                JOptionPane.showMessageDialog(this, "Salary must be a positive number.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric salary.");
            return false;
        }

        if (!Pattern.matches("^[A-Za-z ]+$", location)) {
            JOptionPane.showMessageDialog(this, "Location must contain only letters and spaces.");
            return false;
        }

        try {
            double cgpa = Double.parseDouble(cgpaText);
            if (cgpa < 0 || cgpa > 10) {
                JOptionPane.showMessageDialog(this, "CGPA must be between 0 and 10.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid CGPA.");
            return false;
        }

        try {
            double tenth = Double.parseDouble(tenthText);
            if (tenth < 0 || tenth > 100) {
                JOptionPane.showMessageDialog(this, "10th percentage must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid 10th percentage.");
            return false;
        }

        try {
            double twelfth = Double.parseDouble(twelfthText);
            if (twelfth < 0 || twelfth > 100) {
                JOptionPane.showMessageDialog(this, "12th percentage must be between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid 12th percentage.");
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CompanyRegistration::new);
    }
}
