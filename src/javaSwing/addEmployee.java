package javaSwing;

import javax.swing.*;
import java.awt.*;

public class addEmployee extends JFrame {

    public addEmployee() {
        setTitle("Add Employee");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Disposes only the current window when closed
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Form components
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel dobLabel = new JLabel("Date of Birth (DOB):");
        JTextField dobField = new JTextField(); // Consider using a date picker for a better user experience
        JLabel salaryLabel = new JLabel("Salary:");
        JTextField salaryField = new JTextField();
        JLabel employeeIdLabel = new JLabel("Employee ID:");
        JTextField employeeIdField = new JTextField();

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(dobLabel);
        formPanel.add(dobField);
        formPanel.add(salaryLabel);
        formPanel.add(salaryField);
        formPanel.add(employeeIdLabel);
        formPanel.add(employeeIdField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonsPanel.add(addButton);
        buttonsPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(addEmployee::new);
    }
}
