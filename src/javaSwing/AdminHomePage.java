package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.border.Border;

public class AdminHomePage extends JFrame {

    private Connection connection;
    private JTextArea announcementsArea = new JTextArea();
    private JLabel totalEmployeesValue;
    private JLabel totalInventoryValue;
    String EmployeeCount;
    String ItemCount;
    private JTextArea taskListArea = new JTextArea();

    public AdminHomePage() {
        connectToDatabase();
        if (connection != null) {
            retrieveCountsFromDatabase();
            retrieveTasksFromDatabase();
        }

        setTitle("Enterprise Resource Organizer (ERO) - Admin Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Main Panels
        JPanel headerPanel = new JPanel(new GridLayout(0, 1)); // Grid layout to stack components vertically
        JPanel dashboardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dashboardPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        JPanel navigationPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JPanel quickAccessPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPanel summaryPanel = createSummaryPanel(); // Border layout with spacing between components
        JPanel announcementPanel = createAnnouncementPanel(); // Border layout with spacing between components

        // Set background color to light blue for all panels
        Color lightBlue = new Color(173, 216, 230);
        headerPanel.setBackground(lightBlue);
        dashboardPanel.setBackground(lightBlue);
        navigationPanel.setBackground(lightBlue);
        quickAccessPanel.setBackground(lightBlue);
        summaryPanel.setBackground(lightBlue);
        announcementPanel.setBackground(lightBlue);

        // Header Panel
        JLabel welcomeLabel = new JLabel("Hello, Admin!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(welcomeLabel);

        // Dashboard Panel
        JButton employeeManagementDashboardButton = createDashboardButton("Employee Management");
        employeeManagementDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeManagementScreen();
            }
        });

        JButton inventoryManagementDashboardButton = createDashboardButton("Inventory Management");
        inventoryManagementDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new InventoryManagementPage();
            }
        });

        JButton logoutDashboardButton = createDashboardButton("Logout");
        logoutDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutFunction();
            }
        });

        dashboardPanel.add(employeeManagementDashboardButton);
        dashboardPanel.add(inventoryManagementDashboardButton);
        dashboardPanel.add(logoutDashboardButton);

        // Navigation Panel with Icons
        JButton addEmployeeButton = createNavigationButton("Add Employee", "addEmployee", "C:\\Users\\Dell\\Downloads\\add_employee_icon.png");
        JButton viewEmployeeListButton = createNavigationButton("View Employees", "viewEmployee", "C:\\Users\\Dell\\Downloads\\view_employee_icon.jpg");
        JButton addInventoryItemButton = createNavigationButton("Add Item", "addInventory", "C:\\Users\\Dell\\Downloads\\add_inventory_icon.png");
        JButton viewInventoryStatusButton = createNavigationButton("View Items", "viewInventory", "C:\\Users\\Dell\\Downloads\\view_inventory_icon.jpg");

        navigationPanel.add(addEmployeeButton);
        navigationPanel.add(viewEmployeeListButton);
        navigationPanel.add(addInventoryItemButton);
        navigationPanel.add(viewInventoryStatusButton);

        // Quick Access Panel with Icons
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing

        // Refresh Button
        JButton refreshButton = createDashboardButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        dashboardPanel.add(refreshButton);

        // Add panels to main frame
        getContentPane().setLayout(new BorderLayout(10, 10)); // Added spacing between components
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(navigationPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(summaryPanel, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(announcementPanel, BorderLayout.EAST);

        headerPanel.add(dashboardPanel); // Added dashboard panel to the headerPanel

        // Make the frame visible
        setVisible(true);
    }

    private JButton createDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(173, 216, 230));
        button.setPreferredSize(new Dimension(200, 30));
        button.setBorder(new RoundBtn(25));
        return button;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for better control
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL; // Stretch labels horizontally
        constraints.insets = new Insets(10, 10, 10, 10); // Add spacing

        JLabel totalEmployeesLabel = new JLabel("Total Employees:", JLabel.LEFT);
        totalEmployeesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = 0;
        summaryPanel.add(totalEmployeesLabel, constraints);

        totalEmployeesValue = new JLabel(EmployeeCount, JLabel.RIGHT); // Updated here
        totalEmployeesValue.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 1;
        constraints.gridy = 0;
        summaryPanel.add(totalEmployeesValue, constraints);

        JLabel totalInventoryLabel = new JLabel("Total Inventory Items:", JLabel.LEFT);
        totalInventoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = 1;
        summaryPanel.add(totalInventoryLabel, constraints);

        totalInventoryValue = new JLabel(ItemCount, JLabel.RIGHT); // Updated here
        totalInventoryValue.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 1;
        constraints.gridy = 1;
        summaryPanel.add(totalInventoryValue, constraints);

        JLabel recentActivitiesLabel = new JLabel("Recent Activities:", JLabel.LEFT);
        recentActivitiesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2; // Span two columns
        summaryPanel.add(recentActivitiesLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH; // Allow text area to expand
        constraints.weighty = 1.0; // Give remaining vertical space to text area
        summaryPanel.add(createRecentActivity(), constraints);

        return summaryPanel;
    }

    private void refreshData() {
        setVisible(false);
        new AdminHomePage();
        JOptionPane.showMessageDialog(this, "Data Refreshed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logoutFunction() {
        setVisible(false);
        try {
            First frame = new First();
            frame.setUndecorated(true);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Logout Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
                taskListArea.setText(tasksText.toString());
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createAnnouncementPanel() {
        JPanel announcementPanel = new JPanel(new BorderLayout());
        announcementPanel.setBackground(new Color(173, 216, 230));
        announcementsArea.setEditable(false);
        announcementsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane announcementScrollPane = new JScrollPane(announcementsArea);
        announcementPanel.add(announcementScrollPane, BorderLayout.CENTER);

        announcementPanel.setPreferredSize(new Dimension(200, 300));

        taskListArea.setEditable(false);
        JScrollPane taskListScrollPane = new JScrollPane(taskListArea);
        announcementPanel.add(taskListScrollPane, BorderLayout.CENTER);

        announcementPanel.setBorder(BorderFactory.createTitledBorder("Announcements"));
        announcementPanel.setFont(new Font("Arial", Font.BOLD, 14)); // Add titled border

        return announcementPanel;
    }

    private JPanel createRecentActivity() {
        JPanel announcementPanel = new JPanel(new BorderLayout());

        JPanel employeeActivityPanel = new JPanel(new BorderLayout());
        JLabel employeeActivityLabel = new JLabel("Employee Activity:", JLabel.LEFT);
        employeeActivityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        employeeActivityPanel.add(employeeActivityLabel, BorderLayout.NORTH);

        JTextArea employeeActivityArea = new JTextArea();
        employeeActivityArea.setEditable(false);
        employeeActivityArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane employeeScrollPane = new JScrollPane(employeeActivityArea);
        employeeActivityPanel.add(employeeScrollPane, BorderLayout.CENTER);

        JPanel inventoryActivityPanel = new JPanel(new BorderLayout());
        JLabel inventoryActivityLabel = new JLabel("Inventory Activity:", JLabel.LEFT);
        inventoryActivityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inventoryActivityPanel.add(inventoryActivityLabel, BorderLayout.NORTH);

        JTextArea inventoryActivityArea = new JTextArea();
        inventoryActivityArea.setEditable(false);
        inventoryActivityArea.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane inventoryScrollPane = new JScrollPane(inventoryActivityArea);
        inventoryActivityPanel.add(inventoryScrollPane, BorderLayout.CENTER);

        JPanel activityPanel = new JPanel(new GridLayout(2, 1));
        activityPanel.add(employeeActivityPanel);
        activityPanel.add(inventoryActivityPanel);

        announcementPanel.add(activityPanel, BorderLayout.CENTER);

        retrieveEmployeeActivities(employeeActivityArea);
        retrieveInventoryActivities(inventoryActivityArea);

        announcementPanel.setBorder(BorderFactory.createTitledBorder("Recent Activities"));

        return announcementPanel;
    }

    private void retrieveEmployeeActivities(JTextArea employeeActivityArea) {
        try {
            String query = "SELECT operation, time FROM log WHERE table_name = 'Employee' ORDER BY time DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String operation = resultSet.getString("operation");
                Timestamp time = resultSet.getTimestamp("time");
                String activityText = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time) + "] " + operation + " on Employee\n";
                employeeActivityArea.append(activityText);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving employee activities.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retrieveInventoryActivities(JTextArea inventoryActivityArea) {
        try {
            String query = "SELECT operation, time FROM log WHERE table_name = 'Product' ORDER BY time DESC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String operation = resultSet.getString("operation");
                Timestamp time = resultSet.getTimestamp("time");
                String activityText = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time) + "] " + operation + " on Product\n";
                inventoryActivityArea.append(activityText);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving inventory activities.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton createNavigationButton(String text, String actionCommand, String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(text, icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.BLACK); // Set text color
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (actionCommand) {
                    case "addEmployee":
                        new addEmployee();
                        break;
                    case "viewEmployee":
                        new EmployeeSearchScreen();
                        break;
                    case "addInventory":
                        new addInventory();
                        break;
                    case "viewInventory":
                        new InventorySearchPage();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Something went wrong");
                        break;
                }
            }
        });
        return button;
    }

    private void connectToDatabase() {
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

    private void retrieveCountsFromDatabase() {
        try {
            String employeesQuery = "SELECT COUNT(*) FROM Employee";
            Statement employeesStatement = connection.createStatement();
            ResultSet employeesResultSet = employeesStatement.executeQuery(employeesQuery);
            if (employeesResultSet.next()) {
                int totalEmployees = employeesResultSet.getInt(1);
                System.out.println(totalEmployees);
                EmployeeCount = Integer.toString(totalEmployees);
            }

            String inventoryQuery = "SELECT COUNT(*) FROM Product";
            Statement inventoryStatement = connection.createStatement();
            ResultSet inventoryResultSet = inventoryStatement.executeQuery(inventoryQuery);
            if (inventoryResultSet.next()) {
                int totalInventoryItems = inventoryResultSet.getInt(1);
                ItemCount = Integer.toString(totalInventoryItems);
            }

            employeesStatement.close();
            inventoryStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving counts from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminHomePage::new);
    }
}

class RoundBtn implements Border {
    private int r;
    RoundBtn(int r) {
        this.r = r;
    }
    public Insets getBorderInsets(Component c) {
        return new Insets(this.r + 1, this.r + 1, this.r + 2, this.r);
    }
    public boolean isBorderOpaque() {
        return true;
    }
    public void paintBorder(Component c, Graphics g, int x, int y,
                            int width, int height) {
        g.drawRoundRect(x, y, width - 1, height - 1, r, r);
    }
}
