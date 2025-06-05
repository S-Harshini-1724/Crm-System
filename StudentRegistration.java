import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class StudentRegistration extends JFrame {

    private DefaultTableModel tableModel;
    private JTable studentTable;

    public StudentRegistration() {
        setTitle("Student Registration");
        setSize(900, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5)); // 10 rows (excluding back button)
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField branchField = new JTextField();
        JTextField cgpaField = new JTextField();
        JTextField tenthField = new JTextField();
        JTextField twelfthField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField locationField = new JTextField();
        JButton registerBtn = new JButton("Register");
        JButton viewBtn = new JButton("View Registered Students");
        JButton backBtn = new JButton("Back"); // Back button

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Branch:")); formPanel.add(branchField);
        formPanel.add(new JLabel("CGPA:")); formPanel.add(cgpaField);
        formPanel.add(new JLabel("10th %:")); formPanel.add(tenthField);
        formPanel.add(new JLabel("12th %:")); formPanel.add(twelfthField);
        formPanel.add(new JLabel("Contact No:")); formPanel.add(contactField);
        formPanel.add(new JLabel("Location:")); formPanel.add(locationField);
        formPanel.add(registerBtn); formPanel.add(viewBtn);

        add(formPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel for Back Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligns to right
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Register Button Logic
        registerBtn.addActionListener(_ -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String branch = branchField.getText().trim();
                String cgpaText = cgpaField.getText().trim();
                String tenthText = tenthField.getText().trim();
                String twelfthText = twelfthField.getText().trim();
                String contact = contactField.getText().trim();
                String location = locationField.getText().trim();

                // Validation
                if (name.isEmpty() || !Pattern.matches("^[A-Za-z ]+$", name)) {
                    JOptionPane.showMessageDialog(this, "Enter a valid name!");
                    return;
                }

                if (email.isEmpty() || !Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", email)) {
                    JOptionPane.showMessageDialog(this, "Enter a valid email!");
                    return;
                }

                if (branch.isEmpty() || !Pattern.matches("^[A-Za-z ]+$", branch)) {
                    JOptionPane.showMessageDialog(this, "Enter a valid branch!");
                    return;
                }

                double cgpa = Double.parseDouble(cgpaText);
                if (cgpa < 0 || cgpa > 10) {
                    JOptionPane.showMessageDialog(this, "CGPA must be between 0 and 10!");
                    return;
                }

                double tenth = Double.parseDouble(tenthText);
                if (tenth < 0 || tenth > 100) {
                    JOptionPane.showMessageDialog(this, "10th % must be between 0 and 100!");
                    return;
                }

                double twelfth = Double.parseDouble(twelfthText);
                if (twelfth < 0 || twelfth > 100) {
                    JOptionPane.showMessageDialog(this, "12th % must be between 0 and 100!");
                    return;
                }

                if (!Pattern.matches("^\\d{10}$", contact)) {
                    JOptionPane.showMessageDialog(this, "Contact must be 10 digits!");
                    return;
                }

                if (location.isEmpty() || !Pattern.matches("^[A-Za-z ]+$", location)) {
                    JOptionPane.showMessageDialog(this, "Enter a valid location!");
                    return;
                }

                // DB Insert
                Connection conn = DBConnection.connect();

                // Check if contact already exists
                String checkSql = "SELECT COUNT(*) FROM students WHERE contact_number = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, contact);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Contact number already exists!");
                    conn.close();
                    return;
                }

                // Insert
                String sql = "INSERT INTO students (name, email, branch, cgpa, tenth_percentage, twelfth_percentage, contact_number, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, branch);
                ps.setDouble(4, cgpa);
                ps.setDouble(5, tenth);
                ps.setDouble(6, twelfth);
                ps.setString(7, contact);
                ps.setString(8, location);
                ps.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Student Registered Successfully!");

                // Clear fields
                nameField.setText(""); emailField.setText(""); branchField.setText("");
                cgpaField.setText(""); tenthField.setText(""); twelfthField.setText("");
                contactField.setText(""); locationField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while registering student!");
            }
        });

        // View Registered Students
        viewBtn.addActionListener(_ -> {
            try {
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM students");
                ResultSet rs = ps.executeQuery();

                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);
                tableModel.addColumn("Name");
                tableModel.addColumn("Email");
                tableModel.addColumn("Branch");
                tableModel.addColumn("CGPA");
                tableModel.addColumn("10th %");
                tableModel.addColumn("12th %");
                tableModel.addColumn("Contact No");
                tableModel.addColumn("Location");

                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("branch"),
                            rs.getDouble("cgpa"),
                            rs.getDouble("tenth_percentage"),
                            rs.getDouble("twelfth_percentage"),
                            rs.getString("contact_number"),
                            rs.getString("location")
                    });
                }

                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching student data!");
            }
        });

        // Back Button Logic
        backBtn.addActionListener(_ -> {
            dispose(); // Close this window
            new HomePage(); // Open home page
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistration::new);
    }
}
