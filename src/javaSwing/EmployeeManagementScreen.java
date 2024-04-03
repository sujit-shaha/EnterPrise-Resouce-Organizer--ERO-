package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EmployeeManagementScreen extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Map<String, String[]> dummyData;

    private Connection connection;

    private JTextArea taskListArea;
    
    public EmployeeManagementScreen() {
        setTitle("Employee Management");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel navigationPanel = createNavigationPanel();
        mainPanel.add(navigationPanel, BorderLayout.WEST);

        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel taskListPanel = createTaskListPanel();
        mainPanel.add(taskListPanel, BorderLayout.EAST);

        getContentPane().add(mainPanel);
        setVisible(true);

        // Initialize dummy data
        initializeDummyData();

        // Connect to the database
        connectToDatabase();
        if (connection != null) {
            retrieveTasksFromDatabase();
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

    public void retrieveTasksFromDatabase() {
        try {
            String query = "SELECT * FROM tasks";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println(resultSet);
            while (resultSet.next()) {
                String taskName = resultSet.getString(1);
                addTaskToTextArea(taskName);
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving tasks from database.");
            e.printStackTrace();
        }
    }

    public void addTaskToTextArea(String taskName) {
        // Check if taskListArea is null
        if (taskListArea == null) {
            System.out.println("Task list area is not initialized.");
            return;
        }
        taskListArea.append(taskName + "\n");
    }


    public JPanel createSearchPanel() {
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
        for (Map.Entry<String, String[]> entry : dummyData.entrySet()) {
            String[] employeeData = entry.getValue();
            if (matchesSearch(employeeData, keyword)) {
                JPanel cardPanel = createCardPanel(employeeData);
                cardsPanel.add(cardPanel);
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private boolean matchesSearch(String[] employeeData, String keyword) {
        for (String info : employeeData) {
            if (info.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private JPanel createCardPanel(String[] employeeData) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        for (int i = 0; i < employeeData.length; i++) {
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(getLabel(i) + ": ");
            label.setFont(new Font("Arial", Font.BOLD, 14));
            JLabel value = new JLabel(employeeData[i]);
            infoPanel.add(label);
            infoPanel.add(value);
            cardPanel.add(infoPanel);
        }

        return cardPanel;
    }

    private static String getLabel(int index) {
        switch (index) {
            case 0:
                return "Name";
            case 1:
                return "DOB";
            case 2:
                return "Salary";
            default:
                return "";
        }
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton addEmployeeButton = new JButton("Add Employee", new ImageIcon("C:\\Users\\Dell\\Downloads\\add_employee_icon.png"));
        JButton viewEmployeeButton = new JButton("View Employees", new ImageIcon("C:\\Users\\Dell\\Downloads\\view_employee_icon.jpg"));
        JButton setSalaryButton = new JButton("Set Salary", new ImageIcon("c:\\Users\\Dell\\Downloads\\set_salary_icon.jpg"));
        JButton setAttendanceyButton = new JButton("set Attendance", new ImageIcon("c:\\Users\\Dell\\Downloads\\set_attendance_icon.jpg"));
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

        // Task List Text Area
        JTextArea taskListArea = new JTextArea(); // Initialize the taskListArea variable
        this.taskListArea = taskListArea; // Assign to the member variable
        taskListArea.setEditable(false);
        JScrollPane taskListScrollPane = new JScrollPane(taskListArea);
        taskListPanel.add(taskListScrollPane, BorderLayout.CENTER);

        // Add Task Button
        JButton addTaskButton = new JButton("Add Task");
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement action to add task
            }
        });

        taskListPanel.add(addTaskButton, BorderLayout.NORTH);

        return taskListPanel;
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
