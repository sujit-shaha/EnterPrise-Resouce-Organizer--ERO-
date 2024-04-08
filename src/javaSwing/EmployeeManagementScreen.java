package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EmployeeManagementScreen extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Map<String, String[]> dummyData;

     Connection connection;

    private JTextArea taskListArea;

    public EmployeeManagementScreen() {
        setTitle("Employee Management");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel navigationPanel = createNavigationPanel();
        mainPanel.add(navigationPanel, BorderLayout.WEST);

        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.setBackground(new Color(173, 216, 230)); // Light Blue Background


        cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel taskListPanel = createTaskListPanel();
        mainPanel.add(taskListPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel);
        setVisible(true);
        cardsPanel.setBackground(new Color(173, 216, 230));
        taskListPanel.setBackground(new Color(173, 216, 230));
        getContentPane().setBackground(new Color(173, 216, 230));
        // Initialize dummy data
        initializeDummyData();

        // Connect to the database
        connectToDatabase();
        if (connection != null) {
            retrieveEmployeesFromDatabase();
            retrieveTasksFromDatabase(); // Retrieve tasks initially
        }
    }

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

    public void retrieveEmployeesFromDatabase() {
        try {
            String query = "SELECT * FROM Employee";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date hireDate = resultSet.getDate("hire_date");
                double currentSalary = resultSet.getDouble("salary");
                int attendanceCount = getAttendanceCount(empId);

                Employee employee = new Employee(empId, name, email, hireDate, currentSalary, attendanceCount);
                JPanel cardPanel = createCardPanel(employee);
                cardsPanel.add(cardPanel);
            }

            statement.close();
            cardsPanel.revalidate();
            cardsPanel.repaint();
        } catch (SQLException e) {
            System.out.println("Error retrieving employees from database.");
            e.printStackTrace();
        }
    }

    public int getAttendanceCount(int empId) {
        int attendanceCount = 0;
        try {
            String query = "SELECT COUNT(*) FROM Attendance WHERE emp_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, empId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                attendanceCount = resultSet.getInt(1);
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving attendance count for employee: " + empId);
            e.printStackTrace();
        }
        return attendanceCount;
    }

    private JPanel createCardPanel(Employee employee) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + employee.getName());
        JLabel emailLabel = new JLabel("Email: " + employee.getEmail());
        JLabel hireDateLabel = new JLabel("Hire Date: " + employee.getHireDate());
        JLabel salaryLabel = new JLabel("Salary: $" + String.format("%.2f", employee.getCurrentSalary()));
        JLabel attendanceLabel = new JLabel("Attendance Count: " + employee.getAttendanceCount());

        cardPanel.add(nameLabel);
        cardPanel.add(emailLabel);
        cardPanel.add(hireDateLabel);
        cardPanel.add(salaryLabel);
        cardPanel.add(attendanceLabel);

        return cardPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Employee"));
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchEmployee(searchField.getText());
            }
        });
        searchPanel.add(searchField);
        return searchPanel;
    }

    private void initializeDummyData() {
        dummyData = new HashMap<>();
        dummyData.put("EMP001", new String[]{"John Doe", "1980-01-01", "$50000"});
        // Add more dummy data if needed
    }

    private void searchEmployee(String keyword) {
        cardsPanel.removeAll();
        try {
            String query = "SELECT * FROM Employee WHERE LOWER(name) LIKE ? OR LOWER(email) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword.toLowerCase() + "%");
            statement.setString(2, "%" + keyword.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int empId = resultSet.getInt("emp_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date hireDate = resultSet.getDate("hire_date");
                double currentSalary = resultSet.getDouble("salary");
                int attendanceCount = getAttendanceCount(empId);

                Employee employee = new Employee(empId, name, email, hireDate, currentSalary, attendanceCount);
                JPanel cardPanel = createCardPanel(employee);
                cardsPanel.add(cardPanel);
            }

            statement.close();
            cardsPanel.revalidate();
            cardsPanel.repaint();
        } catch (SQLException e) {
            System.out.println("Error searching for employees in the database.");
            e.printStackTrace();
        }
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton addEmployeeButton = new JButton("Add Employee", new ImageIcon("C:\\Users\\Dell\\Downloads\\add_employee_icon.png"));
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement add employee functionality
                new addEmployee();            }
        });
        JButton viewEmployeeButton = new JButton("View Employees", new ImageIcon("C:\\Users\\Dell\\Downloads\\view_employee_icon.jpg"));
        viewEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement view employees functionality
                 new EmployeeSearchScreen();
            }
        });
        JButton setSalaryButton = new JButton("Set Salary", new ImageIcon("c:\\Users\\Dell\\Downloads\\set_salary_icon.jpg"));
        setSalaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement set salary functionality
                 new SetSalary();
            }
        });
        JButton setAttendanceyButton = new JButton("Set Attendance", new ImageIcon("c:\\Users\\Dell\\Downloads\\set_attendance_icon.jpg"));
        setAttendanceyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Implement set attendance functionality
                 new SetAttendance();
            }
        });
        setButtonStyle(setAttendanceyButton);
        setButtonStyle(addEmployeeButton);
        setButtonStyle(viewEmployeeButton);
        setButtonStyle(setSalaryButton);
        navigationPanel.add(addEmployeeButton);
        navigationPanel.add(viewEmployeeButton);
        navigationPanel.add(setSalaryButton);
        navigationPanel.add(setAttendanceyButton);
        return navigationPanel;
    }

    private JPanel createTaskListPanel() {
        JPanel taskListPanel = new JPanel(new BorderLayout());
        taskListPanel.setBorder(BorderFactory.createTitledBorder("Task List"));

        taskListArea = new JTextArea();
        taskListArea.setEditable(false);
        JScrollPane taskListScrollPane = new JScrollPane(taskListArea);
        taskListPanel.add(taskListScrollPane, BorderLayout.CENTER);

        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddTaskDialog();
            }
        });
        taskListPanel.add(addTaskButton, BorderLayout.NORTH);

        return taskListPanel;
    }

    private void openAddTaskDialog() {
        AddTaskDialog addTaskDialog = new AddTaskDialog(this);
        addTaskDialog.setVisible(true);
    }

    void retrieveTasksFromDatabase() {
        try {
            String query = "SELECT * FROM tasks";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            StringBuilder tasksText = new StringBuilder();
            while (resultSet.next()) {
                String taskName = resultSet.getString("TASK_NAME");
                int taskId = resultSet.getInt("TASK_ID");

                tasksText.append("Task Name: ").append(taskName).append("\n");
                tasksText.append("-------------------------------------\n");
            }

            taskListArea.setText(tasksText.toString());

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setButtonStyle(JButton button) {
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.BLACK); // Set text color
        button.setBackground(Color.WHITE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementScreen::new);
    }
}

class Employee {
    private int empId;
    private String name;
    private String email;
    private Date hireDate;
    private double currentSalary;
    private int attendanceCount;

    public Employee(int empId, String name, String email, Date hireDate, double currentSalary, int attendanceCount) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.hireDate = hireDate;
        this.currentSalary = currentSalary;
        this.attendanceCount = attendanceCount;
    }

    public int getEmpId() {
        return empId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public double getCurrentSalary() {
        return currentSalary;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }
}

 class AddTaskDialog extends JDialog {
    private JTextField taskNameField;
    private JButton addButton;
    private Connection connection;

    public AddTaskDialog(EmployeeManagementScreen parent) {
        super(parent, "Add Task", true);
        this.connection = parent.connection;
        setSize(300, 150);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel taskNameLabel = new JLabel("Task Name:");
        taskNameField = new JTextField(20);
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.add(taskNameLabel, BorderLayout.WEST);
        taskPanel.add(taskNameField, BorderLayout.CENTER);
        mainPanel.add(taskPanel);

        addButton = new JButton("Add Task");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTaskToDatabase();
                parent.retrieveTasksFromDatabase();
            }
        });
        mainPanel.add(Box.createVerticalStrut(10)); // Vertical space
        mainPanel.add(addButton);

        add(mainPanel);
    }

    private void addTaskToDatabase() {
        String taskName = taskNameField.getText().trim();
        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Task Name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "INSERT INTO tasks (TASK_NAME) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, taskName);
            statement.executeUpdate();
            statement.close();

            JOptionPane.showMessageDialog(this, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            taskNameField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding task: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}