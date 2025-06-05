import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    public LoginForm(Runnable onLoginSuccess) {
        setTitle("Campus Recruitment - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Campus Recruitment Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Login Form Panel (Center)
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");

        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));

        // Row 1: Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(userField, gbc);

        // Row 2: Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passField, gbc);

        // Row 3: Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginBtn, gbc);

        add(formPanel, BorderLayout.CENTER); // Add form in the center

        // Footer or status (optional)
        JLabel footer = new JLabel("© 2025 Campus Placement System", SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(footer, BorderLayout.SOUTH);

        // Action Listener
        loginBtn.addActionListener(_ -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin@123")) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                onLoginSuccess.run();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm(() -> {
            System.out.println("Login successful, redirecting...");
        });
    }
}
