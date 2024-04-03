package javaSwing;

import javax.swing.*;
import java.awt.*;

public class addInventory extends JFrame {

    public addInventory() {
        setTitle("Add Inventory");
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Disposes only the current window when closed
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Form components
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns
        JLabel nameLabel = new JLabel("Name of Item:");
        JTextField nameField = new JTextField();
        JLabel prizeLabel = new JLabel("Prize:");
        JTextField prizeField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel itemIdLabel = new JLabel("Item ID:");
        JTextField itemIdField = new JTextField();

        // Add components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(prizeLabel);
        formPanel.add(prizeField);
        formPanel.add(quantityLabel);
        formPanel.add(quantityField);
        formPanel.add(itemIdLabel);
        formPanel.add(itemIdField);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonsPanel.add(addButton);
        buttonsPanel.add(cancelButton);

        // Add components to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(addInventory::new);
    }
}
