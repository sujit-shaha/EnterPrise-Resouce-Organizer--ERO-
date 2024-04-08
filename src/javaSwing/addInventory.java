package javaSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class addInventory extends JFrame {

    private Connection connection;

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

    public addInventory() {
    	
    	connectToDatabase();
    	 // Dropdown for Supplier ID
        JComboBox<String> supplierIdComboBox = new JComboBox<>();
        fetchSupplierIds(supplierIdComboBox);

        setTitle("Add Inventory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form components
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JLabel nameLabel = new JLabel("Name of Item:");
        JTextField nameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel supplierIdLabel = new JLabel("Supplier ID:");
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(quantityLabel);
        formPanel.add(quantityField);
        formPanel.add(supplierIdLabel);
        formPanel.add(supplierIdComboBox);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInventoryToDatabase(nameField.getText(), priceField.getText(), quantityField.getText(),
                        (String) supplierIdComboBox.getSelectedItem(), descriptionField.getText());
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonsPanel.add(addButton);
        buttonsPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);

        setVisible(true);
    }

    private void fetchSupplierIds(JComboBox<String> comboBox) {
        try {
            String query = "SELECT supplier_id FROM Supplier";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                comboBox.addItem(resultSet.getString("supplier_id"));
            }

            statement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching supplier IDs.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addInventoryToDatabase(String name, String price, String quantity, String supplierId, String description) {
        try {
            String query = "INSERT INTO Product (product_id, product_name,quantity, description, price, supplier_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, generateProductId());
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, Integer.parseInt(quantity));
            preparedStatement.setString(4, description);
            preparedStatement.setBigDecimal(5, new java.math.BigDecimal(price));
            preparedStatement.setInt(6, Integer.parseInt(supplierId));

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Inventory added successfully!");
                setVisible(false);
//                new InventoryManagementPage();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add inventory.");
            }

            preparedStatement.close();
        } catch (SQLException | NumberFormatException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Error: Failed to add inventory.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int generateProductId() {
        // Code to generate a unique product ID, you can implement your logic here
        return (int) (Math.random() * 1000) + 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(addInventory::new);
    }
}
