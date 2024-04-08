package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchScreen extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Connection connection;

    public EmployeeSearchScreen() {
        setTitle("Employee Search");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        connectToDatabase();
        searchEmployee("");

        setVisible(true);
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Employee"));
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchEmployee(searchField.getText());
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        return searchPanel;
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

    private void searchEmployee(String keyword) {
        cardsPanel.removeAll();
        List<String[]> employeeData = fetchEmployeeData(keyword);
        for (String[] data : employeeData) {
            JPanel cardPanel = createCardPanel(data);
            cardsPanel.add(cardPanel);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private List<String[]> fetchEmployeeData(String keyword) {
        List<String[]> result = new ArrayList<>();
        try {
            String query = "SELECT name, hire_date, salary FROM Employee WHERE LOWER(name) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String hireDate = resultSet.getString("hire_date");
                String salary = resultSet.getString("salary");
                result.add(new String[]{name, hireDate, salary});
            }

            statement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching employee data.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return result;
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
                return "Hire Date";
            case 2:
                return "Salary";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeSearchScreen::new);
    }
}
