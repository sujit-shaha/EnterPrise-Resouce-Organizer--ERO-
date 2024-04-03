package javaSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminHomePage extends JFrame {
	
	private Connection connection;
	private JTextArea announcementsArea = new JTextArea();
	  // Summary Panel
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

	    JLabel totalEmployeesValue = new JLabel("100", JLabel.RIGHT);
	    totalEmployeesValue.setFont(new Font("Arial", Font.BOLD, 14));
	    constraints.gridx = 1;
	    constraints.gridy = 0;
	    summaryPanel.add(totalEmployeesValue, constraints);

	    JLabel totalInventoryLabel = new JLabel("Total Inventory Items:", JLabel.LEFT);
	    totalInventoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
	    constraints.gridx = 0;
	    constraints.gridy = 1;
	    summaryPanel.add(totalInventoryLabel, constraints);

	    JLabel totalInventoryValue = new JLabel("500", JLabel.RIGHT);
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

	    JTextArea recentActivitiesArea = new JTextArea("- 3 New Employees Added\n- Low Stock Alert: Item X");
	    recentActivitiesArea.setEditable(false);
	    recentActivitiesArea.setFont(new Font("Arial", Font.PLAIN, 12));
	    constraints.gridx = 0;
	    constraints.gridy = 3;
	    constraints.gridwidth = 2;
	    constraints.fill = GridBagConstraints.BOTH; // Allow text area to expand
	    constraints.weighty = 1.0; // Give remaining vertical space to text area
	    summaryPanel.add(recentActivitiesArea, constraints);

	    return summaryPanel;
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
	            while (resultSet.next()) {
	                String taskName = resultSet.getString(1);
	                System.out.println(taskName);
	                addTaskToTextArea(taskName);
	            }

	            statement.close();
	        } catch (SQLException e) {
	            System.out.println("Error retrieving tasks from database.");
	            e.printStackTrace();
	        }
	    }
	    
	    private void addTaskToTextArea(String taskName) {
	    	 if (announcementsArea == null) {
	             System.out.println("Task list area is not initialized.");
	             return;
	         }
	         announcementsArea.append(taskName + "\n");
	    }

	  // Announcement Panel
	  private JPanel createAnnouncementPanel() {
	    JPanel announcementPanel = new JPanel(new BorderLayout());

//	    JLabel announcementsLabel = new JLabel("Announcements:");
//	    announcementsLabel.setFont(new Font("Arial", Font.BOLD, 14));
//	    announcementPanel.add(announcementsLabel, BorderLayout.NORTH);
	    
//	    JTextArea announcementsArea = new JTextArea(); // Initialize the announcementsArea variable
//        this.announcementsArea = announcementsArea;
//        retrieveTasksFromDatabase();
	    
	    announcementsArea.setEditable(false);
	    announcementsArea.setFont(new Font("Arial", Font.PLAIN, 12));
	    announcementPanel.add(announcementsArea, BorderLayout.CENTER);
	    
	    announcementPanel.setPreferredSize(new Dimension(200,300));

	    announcementPanel.setBorder(BorderFactory.createTitledBorder("Announcements"));
	    announcementPanel.setFont(new Font("Arial", Font.BOLD, 14)); // Add titled border

	    return announcementPanel;
	  }

    public AdminHomePage() {

		 connectToDatabase();
		 if(connection != null) {

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
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Header Panel
        JLabel welcomeLabel = new JLabel("Hello, Admin!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(welcomeLabel);

        // Dashboard Panel
        JButton employeeManagementDashboardButton = new JButton("Employee Management");
        employeeManagementDashboardButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new EmployeeManagementScreen();
				
			}
		});
        JButton inventoryManagementDashboardButton = new JButton("Inventory Management");
        inventoryManagementDashboardButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new InventoryManagementPage();
				
			}
		});
        JButton logoutDashboardButton = new JButton("Logout");
        dashboardPanel.add(employeeManagementDashboardButton);
        dashboardPanel.add(inventoryManagementDashboardButton);
        dashboardPanel.add(logoutDashboardButton);

        // Navigation Panel with Icons
        Icon addEmployeeIcon = new ImageIcon("C:\\Users\\Dell\\Downloads\\add_employee_icon.png");
        Icon viewEmployeeIcon = new ImageIcon("C:\\Users\\Dell\\Downloads\\view_employee_icon.jpg");
        
        JButton addEmployeeButton = new JButton("Add Employee", addEmployeeIcon);
        JButton viewEmployeeListButton = new JButton("View Employees", viewEmployeeIcon);
        JButton addInventoryItemButton = new JButton("Add Item", new ImageIcon("C:\\Users\\Dell\\Downloads\\add_inventory_icon.png"));
        JButton viewInventoryStatusButton = new JButton("View Items", new ImageIcon("C:\\Users\\Dell\\Downloads\\view_inventory_icon.jpg"));
        
        setButtonStyle(addEmployeeButton,"addEmployee");
        setButtonStyle(viewEmployeeListButton,"viewEmployee");
        setButtonStyle(addInventoryItemButton,"addInventory");
        setButtonStyle(viewInventoryStatusButton,"viewInventory");
        
        navigationPanel.add(addEmployeeButton);
        navigationPanel.add(viewEmployeeListButton);
        navigationPanel.add(addInventoryItemButton);
        navigationPanel.add(viewInventoryStatusButton);

        // Quick Access Panel with Icons
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing
        quickAccessPanel.add(new JLabel()); // Empty cell for spacing



        // Search Panel
        JButton searchEmployeesButton = new JButton("Search Employees", new ImageIcon("search_employee_icon.png"));
        JButton searchInventoryButton = new JButton("Search Inventory", new ImageIcon("search_inventory_icon.png"));
        searchPanel.add(searchEmployeesButton);
        searchPanel.add(searchInventoryButton);

        // Add panels to main frame
        getContentPane().setLayout(new BorderLayout(10, 10)); // Added spacing between components
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(navigationPanel, BorderLayout.WEST);
        getContentPane().add(searchPanel, BorderLayout.SOUTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(summaryPanel, BorderLayout.CENTER);
        getContentPane().add(centerPanel, BorderLayout.CENTER);
        getContentPane().add(announcementPanel, BorderLayout.EAST);

        headerPanel.add(dashboardPanel); // Added dashboard panel to the headerPanel

        // Make the frame visible
        setVisible(true);
    }
    
    private void setButtonStyle(JButton button,String nameButton) {
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.BLACK); // Set text color
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				switch (nameButton) {
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
					JOptionPane.showMessageDialog(null,"Something went wrong");
					break;
				}
				
			}
		});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminHomePage::new);
    }
}
