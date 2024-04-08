package javaSwing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

public class First extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JButton btnAction;
    private JButton toggleButton;
    private boolean isSignUp = true; // Track if it's Sign Up or Login mode

    // Connection details
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Sujit123";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    First frame = new First();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setTitle("Login/Sign Up");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public First() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY);

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);

        btnAction = new JButton("Sign Up");
        btnAction.setFont(new Font("Arial", Font.PLAIN, 18));
        btnAction.setForeground(Color.WHITE);
        btnAction.setBackground(new Color(241, 57, 83));
        btnAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isSignUp) {
                    signUp();
                } else {
                    login();
                }
            }
        });

        JLabel lblNewLabel = new JLabel("USERNAME : ");
        lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        textField = new JTextField();
        textField.setForeground(Color.BLACK);
        textField.setColumns(10);

        JLabel lblPassword = new JLabel("PASSWORD : ");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        passwordField = new JPasswordField();

        JLabel lblReenterPassword = new JLabel("RE-ENTER PASSWORD : ");
        lblReenterPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        passwordField_1 = new JPasswordField();

        // Toggle Button
        toggleButton = new JButton("Login");
        toggleButton.setFont(new Font("Arial", Font.PLAIN, 14));
        toggleButton.setForeground(Color.BLUE);
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                toggleMode();
            }
            
            // Method to toggle between Sign Up and Login modes
            private void toggleMode() {
                if (isSignUp) {
                    isSignUp = false;
                    btnAction.setText("Login");
                    toggleButton.setText("Sign Up");
                    lblReenterPassword.setVisible(false);
                    passwordField_1.setVisible(false);
                } else {
                    isSignUp = true;
                    btnAction.setText("Sign Up");
                    toggleButton.setText("Login");
                    lblReenterPassword.setVisible(true);
                    passwordField_1.setVisible(true);
                }
            }
        });

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGap(22)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        				.addComponent(textField, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
        				.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        				.addComponent(passwordField)
        				.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE)
        				.addComponent(lblReenterPassword)
        				.addComponent(btnAction, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(toggleButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGap(26)
        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(textField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(lblReenterPassword, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnAction, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(toggleButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
        			.addGap(63))
        );

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("C:\\New folder\\javaSwing\\src\\javaSwing\\images\\Screenshot 2024-04-08 231716.png"));
        panel.add(lblNewLabel_1);
        contentPane.setLayout(gl_contentPane);
    }

    // Method to perform sign up
    private void signUp() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(passwordField_1.getPassword());

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match");
            return;
        }

        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare statement
            String query = "INSERT INTO Admin (username, password) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User signed up successfully!");
                setVisible(false);
                new AdminHomePage();
            }

            // Close connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to perform login
    private void login() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());

        try {
            // Establish connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare statement
            String query = "SELECT * FROM Admin WHERE username = ? AND password = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login successful!");
                setVisible(false);
                new AdminHomePage();
            } else {
                System.out.println("Invalid username or password!");
            }

            // Close connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
