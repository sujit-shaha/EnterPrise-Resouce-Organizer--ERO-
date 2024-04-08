package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Map;

public class InventoryManagementPage extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Connection connection;

    public InventoryManagementPage() {
        setTitle("Inventory Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel leftNavigationPanel = createLeftNavigationPanel();
        JPanel rightNavigationPanel = createRightNavigationPanel();

        mainPanel.add(leftNavigationPanel, BorderLayout.WEST);
        mainPanel.add(rightNavigationPanel, BorderLayout.EAST);

        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        
        mainPanel.setBackground(new Color(173, 216, 230));
        leftNavigationPanel.setBackground(new Color(173, 216, 230));
        rightNavigationPanel.setBackground(new Color(173, 216, 230));
        searchPanel.setBackground(new Color(173, 216, 230));
        cardsPanel.setBackground(new Color(173, 216, 230));
        scrollPane.setBackground(new Color(173, 216, 230));
        

        connectToDatabase();
        fetchAndDisplayInventoryItems();

        setVisible(true);
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

    private void fetchAndDisplayInventoryItems() {
        try {
            String query = "SELECT product_name, quantity, supplier_id FROM Product";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String itemName = resultSet.getString("product_name");
                String quantity = resultSet.getString("quantity");
                int supplierId = resultSet.getInt("supplier_id");
                String supplierName = getSupplierName(supplierId);

                JPanel cardPanel = createCardPanel(itemName, quantity, supplierName);
                cardsPanel.add(cardPanel);
            }

            statement.close();
            cardsPanel.revalidate();
            cardsPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching inventory items.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSupplierName(int supplierId) throws SQLException {
        String query = "SELECT supplier_name FROM Supplier WHERE supplier_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, supplierId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("supplier_name");
        }
        return "";
    }

    private JPanel createCardPanel(String itemName, String quantity, String supplierName) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + itemName);
        JLabel quantityLabel = new JLabel("Quantity: " + quantity);
        JLabel supplierLabel = new JLabel("Supplier: " + supplierName);

        cardPanel.add(nameLabel);
        cardPanel.add(quantityLabel);
        cardPanel.add(supplierLabel);

        return cardPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Inventory"));
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInventory(searchField.getText());
            }
        });
        searchPanel.add(searchField);
        return searchPanel;
    }

    private void searchInventory(String keyword) {
        cardsPanel.removeAll();
        try {
            String query = "SELECT product_name, quantity, supplier_id FROM Product WHERE LOWER(product_name) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + keyword.toLowerCase() + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String itemName = resultSet.getString("product_name");
                String quantity = resultSet.getString("quantity");
                int supplierId = resultSet.getInt("supplier_id");
                String supplierName = getSupplierName(supplierId);

                JPanel cardPanel = createCardPanel(itemName, quantity, supplierName);
                cardsPanel.add(cardPanel);
            }

            statement.close();
            resultSet.close();
            cardsPanel.revalidate();
            cardsPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching inventory items.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLeftNavigationPanel() {
        JPanel navigationPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton updateButton = createIconButton("Update Item", "C:\\Users\\Dell\\Downloads\\update_item_icon.png");
        JButton addButton = createIconButton("Add Item", "C:\\Users\\Dell\\Downloads\\add_inventory_icon.png");
        JButton removeButton = createIconButton("Remove Item", "C:\\Users\\Dell\\Downloads\\remove_item_icon.jpg");

        navigationPanel.add(updateButton);
        navigationPanel.add(addButton);
        navigationPanel.add(removeButton);

        return navigationPanel;
    }

    private JPanel createRightNavigationPanel() {
        JPanel navigationPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JButton supplierButton = createIconButton("Supplier Contact", "C:\\Users\\Dell\\Downloads\\supplier_contact_icon.jpg");
        JButton historyButton = createIconButton("Show History", "C:\\Users\\Dell\\Downloads\\show_history_icon.png");
        JButton requestButton = createIconButton("Request Item", "C:\\Users\\Dell\\Downloads\\request_item_icon.jpg");

        navigationPanel.add(supplierButton);
        navigationPanel.add(historyButton);
        navigationPanel.add(requestButton);

        return navigationPanel;
    }

    private JButton createIconButton(String text, String iconFileName) {
        JButton button = new JButton(text, new ImageIcon(iconFileName));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.BLACK); // Set text color
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement action for the button
                switch (text) {
                    case "Update Item":
                    	new UpdateItemPage();
                        break;
                    case "Add Item":
                        new addInventory();
                        break;
                    case "Remove Item":
                        new RemoveItemPage();
                        break;
                    case "Supplier Contact":
                        new SupplierContactPage();
                        break;
                    case "Show History":
                        new ProductLogPage();
                        break;
                    case "Request Item":
                        new SendRequestPage();
                        break;
                    default:
                        break;
                }
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryManagementPage::new);
    }
}
