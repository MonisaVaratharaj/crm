package crmsignupform;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class crmsignupform {

    public static void main(String[] args) {
        // Create a new frame
        JFrame frame = new JFrame("Form");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Panel to hold form elements
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Using GridBagLayout for better alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        
        // Set background color for the panel
        panel.setBackground(new Color(240, 248, 255));

        // Create components with some styling
        JLabel lblTitle = new JLabel("Sign Up Form");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.DARK_GRAY);
        
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField txtUsername = new JTextField(20);
        
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField txtEmail = new JTextField(20);
        
        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        lblPhoneNumber.setFont(new Font("Arial", Font.PLAIN, 16));
        JTextField txtPhoneNumber = new JTextField(20);
        
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField txtPassword = new JPasswordField(20);
        
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        JPasswordField txtConfirmPassword = new JPasswordField(20);
        
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 16));
        btnSubmit.setBackground(new Color(100, 149, 237));
        btnSubmit.setForeground(Color.WHITE);

        JLabel lblMessage = new JLabel("", JLabel.CENTER);
        lblMessage.setFont(new Font("Arial", Font.PLAIN, 14));

        // Layout the components using GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblTitle, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblUsername, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(lblPhoneNumber, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtPhoneNumber, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(lblConfirmPassword, gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(txtConfirmPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 1;
        panel.add(btnSubmit, gbc);

        gbc.gridy = 7;
        panel.add(lblMessage, gbc);

        // Add panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Add action listener to submit button
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String email = txtEmail.getText();
                String phoneNumber = txtPhoneNumber.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());

                // Basic validation
                if (username.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    lblMessage.setText("Please fill all fields");
                    lblMessage.setForeground(Color.RED);
                } else if (!password.equals(confirmPassword)) {
                    lblMessage.setText("Passwords do not match");
                    lblMessage.setForeground(Color.RED);
                } else if (!phoneNumber.matches("\\d{10}")) {  // Basic validation for 10-digit phone number
                    lblMessage.setText("Invalid phone number");
                    lblMessage.setForeground(Color.RED);
                } else {
                    // Call method to save user data to MySQL
                    if (saveToDatabase(username, email, phoneNumber, password)) {
                        lblMessage.setText("Sign Up Successful!");
                        lblMessage.setForeground(Color.GREEN);
                    } else {
                        lblMessage.setText("Error: Unable to register.");
                        lblMessage.setForeground(Color.RED);
                    }
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to save user data to MySQL database
    private static boolean saveToDatabase(String username, String email, String phoneNumber, String password) {
        boolean isSaved = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/user_registration";  // Your MySQL URL
            String user = "root";  // Default MySQL username in XAMPP
            String pass = "";      // Leave blank if there's no password

            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            conn = DriverManager.getConnection(url, user, pass);

            // SQL query to insert data
            String sql = "INSERT INTO users (username, email, phone, password) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, password);

            // Execute update
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                isSaved = true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return isSaved;
    }
}
