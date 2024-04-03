package javaSwing;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class setAttendance extends JFrame {

    private List<Employee> employees;
    private Connection connection;

    public setAttendance() {
        setTitle("Set Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); // Set your desired window size
        setLocationRelativeTo(null); // Center the window on the screen

        // Sample employee data (replace with your actual data)
        employees = new ArrayList<>();
        employees.add(new Employee("John Doe"));
        employees.add(new Employee("Jane Smith"));
        employees.add(new Employee("Mike Johnson"));
        employees.add(new Employee("Emily Davis"));

        // Create main panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel employeeListPanel = new JPanel(new GridLayout(0, 1));

        // Create checkboxes for each employee
        for (Employee employee : employees) {
            JCheckBox checkBox = new JCheckBox(employee.getName());
            employeeListPanel.add(checkBox);
        }

        // Scroll pane for the employee list
        JScrollPane scrollPane = new JScrollPane(employeeListPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button to submit attendance
        JButton submitButton = new JButton("Submit Attendance");
        submitButton.addActionListener(e -> {
            // Perform action when submit button is clicked
            try {
                connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "Sujit123");
                for (int i = 0; i < employees.size(); i++) {
                    JCheckBox checkBox = (JCheckBox) employeeListPanel.getComponent(i);
                    boolean attended = checkBox.isSelected();
                    employees.get(i).setAttended(attended);
                    insertAttendance(employees.get(i).getName(), attended);
                }
                JOptionPane.showMessageDialog(this, "Attendance submitted successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        mainPanel.add(submitButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void insertAttendance(String name, boolean attended) throws SQLException {
        if(attended) {
        	String sql = "INSERT INTO attendance (name, attDate) VALUES (?, SYSDATE)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                System.out.println(name + " : " + attended);
//                int status = attended ? 1 : 0; // Convert boolean to 1 or 0 for database
//                statement.setInt(2, status);
                statement.executeUpdate();
            }
        }
    }

    private static class Employee {
        private String name;
        private boolean attended;

        public Employee(String name) {
            this.name = name;
            this.attended = false; // Default to not attended
        }

        public String getName() {
            return name;
        }

        public boolean isAttended() {
            return attended;
        }

        public void setAttended(boolean attended) {
            this.attended = attended;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setAttendance attendancePage = new setAttendance();
            attendancePage.setVisible(true);
        });
    }
}
