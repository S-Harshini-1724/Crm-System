import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ShortlistStudents extends JFrame {
    private JTextField txtCompanyName;
    private JTable table;
    private DefaultTableModel model;

    public ShortlistStudents() {
        setTitle("Shortlist Students");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter Company Name:"));
        txtCompanyName = new JTextField(20);
        inputPanel.add(txtCompanyName);
        JButton btnShortlist = new JButton("Shortlist Students");
        inputPanel.add(btnShortlist);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        model.setColumnIdentifiers(new String[] {
            "ID", "Name", "Email", "Contact", "CGPA", "10th %", "12th %", "Location"
        });

        btnShortlist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shortlistStudents();
            }
        });
    }

    private void shortlistStudents() {
        String companyName = txtCompanyName.getText().trim();

        if (companyName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a company name.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crms", "root", "Harshini@2407");
             PreparedStatement companyStmt = conn.prepareStatement(
                 "SELECT min_cgpa, min_10th, min_12th FROM companies WHERE name = ?")) {

            companyStmt.setString(1, companyName);
            ResultSet rs = companyStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Company not found.");
                return;
            }

            double minCgpa = rs.getDouble("min_cgpa");
            double min10 = rs.getDouble("min_10th");
            double min12 = rs.getDouble("min_12th");

            PreparedStatement studentStmt = conn.prepareStatement(
                "SELECT * FROM students WHERE cgpa >= ? AND tenth_percentage >= ? AND twelfth_percentage >= ?");
            studentStmt.setDouble(1, minCgpa);
            studentStmt.setDouble(2, min10);
            studentStmt.setDouble(3, min12);
            ResultSet studentRS = studentStmt.executeQuery();

            model.setRowCount(0); // Clear previous data

            boolean hasData = false;
            while (studentRS.next()) {
                hasData = true;
                model.addRow(new Object[] {
                    studentRS.getInt("id"),
                    studentRS.getString("name"),
                    studentRS.getString("email"),
                    studentRS.getString("contact_number"),
                    studentRS.getDouble("cgpa"),
                    studentRS.getDouble("tenth_percentage"),
                    studentRS.getDouble("twelfth_percentage"),
                    studentRS.getString("location")
                });
            }

            if (!hasData) {
                JOptionPane.showMessageDialog(this, "No students matched the criteria.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShortlistStudents().setVisible(true));
    }
}
