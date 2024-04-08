package javaSwing;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SetAttendance extends JFrame {

    private Connection connection;
    private JComboBox<String>[] statusComboBoxes;
    private JPanel employeeListPanel = new JPanel(new GridLayout(0, 1));

    public SetAttendance() {
        setTitle("Set Attendance");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "Sujit123");
            fetchEmployees();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(employeeListPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit Attendance");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (statusComboBoxes != null) {
                        for (int i = 0; i < statusComboBoxes.length; i++) {
                            JComboBox<String> statusComboBox = statusComboBoxes[i];
                            if (statusComboBox != null) {
                                String status = (String) statusComboBox.getSelectedItem();
                                updateAttendance(i + 1, status);
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Attendance submitted successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "No employees to submit attendance.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to submit attendance.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mainPanel.add(submitButton, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private void fetchEmployees() throws SQLException {
        String query = "SELECT emp_id, name, email, hire_date, salary FROM Employee";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            statusComboBoxes = new JComboBox[100]; // Assuming a maximum of 100 employees

            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date hireDate = resultSet.getDate("hire_date");
                double salary = resultSet.getDouble("salary");

                JPanel cardPanel = createEmployeeCard(empId, name, email, hireDate, salary);
                employeeListPanel.add(cardPanel);
            }
        }
    }

    private JPanel createEmployeeCard(int empId, String name, String email, Date hireDate, double salary) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createEtchedBorder()));

        JLabel nameLabel = new JLabel("Name: " + name);
        JLabel emailLabel = new JLabel("Email: " + email);
        JLabel hireDateLabel = new JLabel("Hire Date: " + hireDate.toString());
        JLabel salaryLabel = new JLabel("Salary: $" + salary);

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        statusComboBoxes[empId - 1] = statusComboBox;

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(emailLabel);
        infoPanel.add(hireDateLabel);
        infoPanel.add(salaryLabel);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.add(new JLabel("Status:"));
        statusPanel.add(statusComboBox);

        cardPanel.add(infoPanel, BorderLayout.CENTER);
        cardPanel.add(statusPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    private void updateAttendance(int empId, String status) throws SQLException {
        String insertQuery = "INSERT INTO Attendance (attendance_id, emp_id, attendance_date, status) VALUES (?, ?, SYSDATE, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, getNextAttendanceId());
            statement.setInt(2, empId);
            statement.setString(3, status);
            statement.executeUpdate();
        }
    }

    private int getNextAttendanceId() throws SQLException {
        String query = "SELECT MAX(attendance_id) FROM Attendance";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            }
        }
        return 1; // Return 1 if no attendance records yet
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SetAttendance::new);
    }
}
