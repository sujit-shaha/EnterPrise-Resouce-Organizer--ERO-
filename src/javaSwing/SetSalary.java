package javaSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SetSalary extends JFrame {

    private Connection connection;
    private JPanel employeeListPanel = new JPanel(new GridLayout(0, 1, 10, 10));

    public SetSalary() {
        setTitle("Set Salary Page");
        setSize(600, 400);
        setLocationRelativeTo(null);

        try {
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "Sujit123");
            fetchEmployees();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(employeeListPanel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void fetchEmployees() throws SQLException {
        String query = "SELECT e.emp_id, e.name, e.email, e.hire_date, e.salary, " +
                "COALESCE(SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END), 0) AS present_days " +
                "FROM Employee e " +
                "LEFT JOIN Attendance a ON e.emp_id = a.emp_id " +
                "GROUP BY e.emp_id, e.name, e.email, e.hire_date, e.salary";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date hireDate = resultSet.getDate("hire_date");
                double salary = resultSet.getDouble("salary");
                int presentDays = resultSet.getInt("present_days");

                Employee employee = new Employee(empId, name, email, hireDate, salary, presentDays);
                JPanel cardPanel = createEmployeeCard(employee);
                employeeListPanel.add(cardPanel);
            }
        }
    }


    private JPanel createEmployeeCard(Employee employee) {
        JPanel cardPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel nameLabel = new JLabel("Name: " + employee.getName());
        JLabel attendanceLabel = new JLabel("Attendance: " + employee.getAttendanceCount() + " days");
        JLabel salaryLabel = new JLabel("Current Salary: $" + employee.getCurrentSalary());

        JButton setSalaryButton = new JButton("Set Salary");
        setSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSalaryDialog(employee);
            }
        });

        cardPanel.add(nameLabel);
        cardPanel.add(attendanceLabel);
        cardPanel.add(salaryLabel);
        cardPanel.add(setSalaryButton);

        return cardPanel;
    }

    private void showSalaryDialog(Employee employee) {
        JDialog dialog = new JDialog(this, "Set Salary for " + employee.getName(), true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(5, 1, 10, 10));
        dialog.setLocationRelativeTo(this);

        JLabel nameLabel = new JLabel("Name: " + employee.getName());
        JLabel currentSalaryLabel = new JLabel("Current Salary: $" + employee.getCurrentSalary());
        JLabel presentDaysLabel = new JLabel("Present Days: " + employee.getAttendanceCount());

        JComboBox<String> percentageComboBox = new JComboBox<>(new String[]{"10%", "20%", "30%", "40%", "50%"});

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPercentage = (String) percentageComboBox.getSelectedItem();
                double percentage = Double.parseDouble(selectedPercentage.replace("%", "")) / 100.0;
                double newSalary = employee.getCurrentSalary() * (1 + percentage);
                

                boolean success = updateSalary(employee.getEmpId(), newSalary);
                if (success) {
                    currentSalaryLabel.setText("Current Salary: $" + newSalary);
                    JOptionPane.showMessageDialog(SetSalary.this,
                            "New salary for " + employee.getName() + " is: $" + String.format("%.2f", newSalary),
                            "Salary Updated", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                    new SetSalary();
                } else {
                    JOptionPane.showMessageDialog(SetSalary.this,
                            "Failed to update salary for " + employee.getName(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                dialog.dispose();
            }
        });

        dialog.add(nameLabel);
        dialog.add(currentSalaryLabel);
        dialog.add(presentDaysLabel);
        dialog.add(percentageComboBox);
        dialog.add(confirmButton);

        dialog.setVisible(true);
    }

    private boolean updateSalary(int empId, double newSalary) {
        String updateQuery = "UPDATE Employee SET salary = ? WHERE emp_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setDouble(1, newSalary);
            statement.setInt(2, empId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SetSalary::new);
    }
}

//class Employee {
//    private int empId;
//    private String name;
//    private String email;
//    private Date hireDate;
//    private double currentSalary;
//    private int attendanceCount;
//
//    public Employee(int empId, String name, String email, Date hireDate, double currentSalary, int attendanceCount) {
//        this.empId = empId;
//        this.name = name;
//        this.email = email;
//        this.hireDate = hireDate;
//        this.currentSalary = currentSalary;
//        this.attendanceCount = attendanceCount;
//    }
//
//    public int getEmpId() {
//        return empId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public Date getHireDate() {
//        return hireDate;
//    }
//
//    public double getCurrentSalary() {
//        return currentSalary;
//    }
//
//    public int getAttendanceCount() {
//        return attendanceCount;
//    }
//}
