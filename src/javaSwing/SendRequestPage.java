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

public class SendRequestPage extends JFrame {

    private Connection connection;
    private JPanel productPanel;

    public SendRequestPage() {
        setTitle("Send Request - Admin Home");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Send Request", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel);

        // Product Panel
        productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        JScrollPane productScrollPane = new JScrollPane(productPanel);

        


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

        JButton sendRequestButton = new JButton("Send Request");
        sendRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRequestDialog(productId, productName, quantity);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(sendRequestButton);

        cardPanel.add(nameLabel, BorderLayout.NORTH);
        cardPanel.add(quantityLabel, BorderLayout.WEST);
        cardPanel.add(priceLabel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        productPanel.add(cardPanel);
        productPanel.revalidate();
        productPanel.repaint();
    }

    private void sendRequestDialog(int productId, String productName, int availableQuantity) {
        JTextField quantityField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter Quantity to Send for " + productName + ":"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Send Request",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String quantityStr = quantityField.getText();
            if (!quantityStr.isEmpty()) {
                try {
                    int requestedQuantity = Integer.parseInt(quantityStr);
                    if (requestedQuantity > 0 && requestedQuantity <= availableQuantity) {
                        int confirm = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to send " + requestedQuantity + " units of " + productName + "?",
                                "Confirm Send Request", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (updateQuantity(productId, availableQuantity - requestedQuantity)) {
                                JOptionPane.showMessageDialog(null, "Items Sent Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                productPanel.removeAll();
                                fetchProducts(); // Refresh the product list after sending request
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to update quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid quantity or insufficient quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    private void sendRequest() {
        // Implement if needed
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SendRequestPage::new);
    }
}
