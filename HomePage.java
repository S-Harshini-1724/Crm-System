import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Campus Recruitment Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));  

        JLabel heading = new JLabel("Welcome to Campus Recruitment Management System", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 32));

        JButton studentBtn = new JButton("Student Registration");
        JButton companyBtn = new JButton("Company Registration");
        JButton shortlistBtn = new JButton("Shortlist Students"); 
        JButton exitBtn = new JButton("Exit");

        studentBtn.addActionListener(_ -> new StudentRegistration());

        companyBtn.addActionListener(_ -> {
            new LoginForm(() -> new CompanyRegistration()); 
        });

        shortlistBtn.addActionListener(_ -> {
    new LoginForm(() -> {
        ShortlistStudents shortlistStudents = new ShortlistStudents();
        shortlistStudents.setVisible(true);
    });
});


        exitBtn.addActionListener(_ -> System.exit(0));
        add(heading);
        add(studentBtn);
        add(companyBtn);
        add(shortlistBtn);  
        add(exitBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePage::new);
    }
}
