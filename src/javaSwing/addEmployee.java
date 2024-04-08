package javaSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class addEmployee extends JFrame {

    private Connection connection;

    public void connectToDatabase() {
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Update with your Oracle connection URL
        String username = "system"; // Update with your Oracle username
        String password = "Sujit123"; // Update with your Oracle password

        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    public addEmployee() {
        setTitle("Add Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Disposes only the current window when closed
        setSize(500, 400);
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Form components
        JPanel formPanel = new JPanel(new GridLayout(6, 2,10,10)); // 5 rows, 2 columns
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JLabel hireDateLabel = new JLabel("Hire Date (YYYY-MM-DD):");
        JTextField hireDateField = new JTextField();
        JLabel salaryLabel = new JLabel("Salary:");
        JTextField salaryField = new JTextField();

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(hireDateLabel);
        formPanel.add(hireDateField);
        formPanel.add(salaryLabel);
        formPanel.add(salaryField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployeeToDatabase(nameField.getText(), emailField.getText(), phoneField.getText(), addressField.getText(),
                        hireDateField.getText(), salaryField.getText());
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(addButton);
        buttonsPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);

        connectToDatabase(); // Connect to the database when the form is created

        setVisible(true);
    }

    // Method to add employee to the database
    private void addEmployeeToDatabase(String name, String email, String phone, String address, String hireDate, String salary) {
        try {
            // Prepare insert query
            String query = "INSERT INTO Employee (emp_id, name, email, phone, address, hire_date, salary) " +
                    "VALUES (emp_id_seq.nextval, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";

            // Create PreparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, hireDate);
            preparedStatement.setBigDecimal(6, new java.math.BigDecimal(salary));

            // Execute the query
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Please Enter Value Correctly");
            }

            // Close PreparedStatement
            preparedStatement.close();
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: Failed to add employee. Please Enter Value Correctly\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (NumberFormatException e) {
			// TODO: handle exception
        	JOptionPane.showMessageDialog(this, "Error: Failed to add employee. Please Enter Value Correctly\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}
        catch (NullPointerException e) {
			// TODO: handle exception
        	JOptionPane.showMessageDialog(this, "Error: Failed to add employee. Please Enter Value Correctly\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

		}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(addEmployee::new);
    }
}
