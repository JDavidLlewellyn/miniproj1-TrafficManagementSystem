import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginPage() {
        super("Traffic Management Login");

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Administrator", "User"});
        panel.add(roleBox);

        JButton loginBtn = new JButton("Login");
        JButton clearBtn = new JButton("Clear");

        panel.add(loginBtn);
        panel.add(clearBtn);

        add(panel);

        // LOGIN BUTTON ACTION
        loginBtn.addActionListener(e -> login());

        // CLEAR BUTTON ACTION
        clearBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
        });
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleBox.getSelectedItem().toString();

        // ADMIN LOGIN
        if(role.equals("Administrator")) {
            if(username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "Admin Login Successful!");

                dispose();
                new TrafficManagementSystem().setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!");
            }
        }

        // USER LOGIN
        else {
            if(username.equals("user") && password.equals("user123")) {
                JOptionPane.showMessageDialog(this, "User Login Successful!");

                dispose();
                new TrafficUserView().setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(this, "Invalid User Credentials!");
            }
        }
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "JDBC Driver Error");
        }

        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}