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

public class RemoveItemPage extends JFrame {

    private Connection connection;
    private JPanel productPanel;

    public RemoveItemPage() {
        setTitle("Remove Item - Admin Home");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Remove Items", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);

        // Product Panel
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        JScrollPane productScrollPane = new JScrollPane(productPanel);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton removeButton = new JButton("Remove Selected Items");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedItems();
            }
        });
        buttonPanel.add(removeButton);

        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(productScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        connectToDatabase();
        if (connection != null) {
            fetchProducts();
        }

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

    private void fetchProducts() {
        try {
            String query = "SELECT * FROM Product";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                addProductCard(productId, productName, quantity, description, price);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProductCard(int productId, String productName, int quantity, String description, double price) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createEtchedBorder());
        cardPanel.setMaximumSize(new Dimension(600, 100));

        JLabel nameLabel = new JLabel("Product Name: " + productName);
        JLabel quantityLabel = new JLabel("Quantity: " + quantity);
        JLabel descriptionLabel = new JLabel("Description: " + description);
        JLabel priceLabel = new JLabel("Price: $" + price);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setActionCommand(String.valueOf(productId)); // Set product ID as action command

        cardPanel.add(nameLabel, BorderLayout.NORTH);
        cardPanel.add(quantityLabel, BorderLayout.WEST);
        cardPanel.add(descriptionLabel, BorderLayout.CENTER);
        cardPanel.add(priceLabel, BorderLayout.EAST);
        cardPanel.add(checkBox, BorderLayout.SOUTH);

        productPanel.add(cardPanel);
        productPanel.revalidate();
        productPanel.repaint();
    }

    private void removeSelectedItems() {
        Component[] components = productPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel cardPanel = (JPanel) component;
                for (Component innerComponent : cardPanel.getComponents()) {
                    if (innerComponent instanceof JCheckBox) {
                        JCheckBox checkBox = (JCheckBox) innerComponent;
                        if (checkBox.isSelected()) {
                            int productId = Integer.parseInt(checkBox.getActionCommand());
                            if (deleteProduct(productId)) {
                                JOptionPane.showMessageDialog(this, "Product with ID " + productId + " deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                	setVisible(false);
                                	new InventoryManagementPage();// Refresh the product list after deletion
                            } else {
                                JOptionPane.showMessageDialog(this, "Failed to delete product with ID " + productId, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean deleteProduct(int productId) {
        try {
            String query = "DELETE FROM Product WHERE product_id = " + productId;
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(query);
            statement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RemoveItemPage::new);
    }
}
