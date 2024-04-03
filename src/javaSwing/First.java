package javaSwing;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

public class First extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;

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
                    frame.setUndecorated(true);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setTitle("Login");
                   
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
        setBounds(100, 100, 966, 558);
        contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY);

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY);

        JButton btnNewButton = new JButton("Sign Up");
        btnNewButton.setFont(new Font("Arial", Font.PLAIN, 18));
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBackground(new Color(241, 57, 83));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        JLabel lblNewLabel = new JLabel("USERNAME : ");
        lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        textField = new JTextField();
        textField.setForeground(Color.BLACK);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setForeground(Color.BLACK);
        textField_1.setColumns(10);

        JLabel lblEmail = new JLabel("EMAIL : ");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel lblPassword = new JLabel("PASSWORD : ");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        passwordField = new JPasswordField();

        JLabel lblReenterPassword = new JLabel("RE-ENTER PASSWORD : ");
        lblReenterPassword.setFont(new Font("Arial", Font.PLAIN, 18));

        passwordField_1 = new JPasswordField();

        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE)
                                .addGap(105)
                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textField, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                                                .addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(textField_1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
                                        .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(passwordField)
                                                        .addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblReenterPassword))
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addGap(104)
                                                        .addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(223, Short.MAX_VALUE))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addGap(26)
                                .addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addGap(6)
                                .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(lblReenterPassword, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField_1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                .addGap(49))
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
        );

        JLabel lblNewLabel_1 = new JLabel("New label");
        panel.add(lblNewLabel_1);
        contentPane.setLayout(gl_contentPane);
    }

    // Method to perform signup
    private void signUp() {
        String username = textField.getText();
        String email = textField_1.getText();
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
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);

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
}
