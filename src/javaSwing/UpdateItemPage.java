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

public class UpdateItemPage extends JFrame {

    private Connection connection;
    private JPanel productPanel;

    public UpdateItemPage() {
        setTitle("Update Items - Admin Home");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Update Items", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);

        // Product Panel
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        JScrollPane productScrollPane = new JScrollPane(productPanel);

        // Button Panel
        


        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(productScrollPane, BorderLayout.CENTER);

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
                double price = resultSet.getDouble("price");

                addProductCard(productId, productName, quantity, price);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProductCard(int productId, String productName, int quantity, double price) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createEtchedBorder());
        cardPanel.setMaximumSize(new Dimension(600, 100));

        JLabel nameLabel = new JLabel("Product Name: " + productName);
        JLabel quantityLabel = new JLabel("Quantity: " + quantity);
        JLabel priceLabel = new JLabel("Price: $" + price);

        JButton updateQuantityButton = new JButton("Update Quantity");
        updateQuantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newQuantityString = JOptionPane.showInputDialog(null, "Enter new quantity for " + productName + ":");
                if (newQuantityString != null && !newQuantityString.isEmpty()) {
                    try {
                        int newQuantity = Integer.parseInt(newQuantityString);
                        if (newQuantity >= 0) {
                            if (updateQuantity(productId, newQuantity)) {
                                quantityLabel.setText("Quantity: " + newQuantity);
                                JOptionPane.showMessageDialog(null, "Quantity updated successfully for " + productName, "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to update quantity for " + productName, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Quantity must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPriceString = JOptionPane.showInputDialog(null, "Enter new price for " + productName + ":");
                if (newPriceString != null && !newPriceString.isEmpty()) {
                    try {
                        double newPrice = Double.parseDouble(newPriceString);
                        if (newPrice >= 0) {
                            if (updatePrice(productId, newPrice)) {
                                priceLabel.setText("Price: $" + newPrice);
                                JOptionPane.showMessageDialog(null, "Price updated successfully for " + productName, "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to update price for " + productName, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Price must be a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input for price.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateQuantityButton);
        buttonPanel.add(updatePriceButton);

        cardPanel.add(nameLabel, BorderLayout.NORTH);
        cardPanel.add(quantityLabel, BorderLayout.WEST);
        cardPanel.add(priceLabel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        productPanel.add(cardPanel);
        productPanel.revalidate();
        productPanel.repaint();
    }

    private boolean updateQuantity(int productId, int newQuantity) {
        try {
            String query = "UPDATE Product SET quantity = " + newQuantity + " WHERE product_id = " + productId;
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(query);
            statement.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updatePrice(int productId, double newPrice) {
        try {
            String query = "UPDATE Product SET price = " + newPrice + " WHERE product_id = " + productId;
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
        SwingUtilities.invokeLater(UpdateItemPage::new);
    }
}
