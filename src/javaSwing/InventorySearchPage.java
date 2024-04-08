package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventorySearchPage extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;

    public InventorySearchPage() {
        setTitle("Inventory Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // Fetch and display inventory from database
        fetchAndDisplayInventory("");

        setVisible(true);
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Inventory"));
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                fetchAndDisplayInventory(searchField.getText());
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        return searchPanel;
    }

    private void fetchAndDisplayInventory(String keyword) {
        cardsPanel.removeAll();

        // Fetch data from database based on keyword
        List<String[]> inventoryData = fetchDataFromDatabase(keyword);

        // Create and add card panels for each product
        for (String[] itemData : inventoryData) {
            JPanel cardPanel = createCardPanel(itemData);
            cardsPanel.add(cardPanel);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private List<String[]> fetchDataFromDatabase(String keyword) {
        List<String[]> inventoryData = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish connection to your Oracle database
            String url = "jdbc:oracle:thin:@localhost:1521:xe";
            String username = "system";
            String password = "Sujit123";
            connection = DriverManager.getConnection(url, username, password);

            String query = "SELECT * FROM Product WHERE LOWER(product_name) LIKE ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword.toLowerCase() + "%");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String productName = resultSet.getString("product_name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");

                inventoryData.add(new String[]{productName, String.valueOf(price), String.valueOf(quantity)});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return inventoryData;
    }

    private JPanel createCardPanel(String[] itemData) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + itemData[0]);
        JLabel priceLabel = new JLabel("Price: $" + itemData[1]);
        JLabel quantityLabel = new JLabel("Quantity Available: " + itemData[2]);

        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        cardPanel.add(nameLabel);
        cardPanel.add(priceLabel);
        cardPanel.add(quantityLabel);

        return cardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventorySearchPage::new);
    }
}
