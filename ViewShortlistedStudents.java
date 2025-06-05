import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewShortlistedStudents extends JFrame {

    JTable table;
    DefaultTableModel model;

    public ViewShortlistedStudents() {
        setTitle("View Shortlisted Students");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table Setup
        model = new DefaultTableModel(new String[]{
                "Name", "Email", "Branch", "CGPA", "10th %", "12th %", "Contact", "Location", "Company"
        }, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        loadData();

        setVisible(true);
    }

    private void loadData() {
        try {
            // Your DB connection string
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ss", "root", "Harshini@2407");
            String sql = "SELECT * FROM shortlisted_students";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("student_name"),
                        rs.getString("email"),
                        rs.getString("branch"),
                        rs.getDouble("cgpa"),
                        rs.getDouble("tenth_percentage"),
                        rs.getDouble("twelfth_percentage"),
                        rs.getString("contact_number"),
                        rs.getString("location"),
                        rs.getString("company_name")
                });
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewShortlistedStudents::new);
    }
}
