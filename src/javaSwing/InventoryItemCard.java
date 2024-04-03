package javaSwing;

import javax.swing.*;
import java.awt.*;

public class InventoryItemCard extends JPanel {

    public InventoryItemCard(String label, String value) {
        setLayout(new GridLayout(2, 1)); // 2 rows, 1 column

        JLabel labelLabel = new JLabel(label + ":");
        JLabel valueLabel = new JLabel(value);

        add(labelLabel);
        add(valueLabel);
    }

    public static void main(String[] args) {
        // Example usage:
        JFrame frame = new JFrame("Inventory Items");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JPanel cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Use a vertical layout for cards

        // Example data for testing
        String[][] inventoryItems = {
                {"Laptop", "$1000", "10", "ITM001"},
                {"Smartphone", "$800", "20", "ITM002"},
                {"Tablet", "$500", "15", "ITM003"}
        };

        for (String[] item : inventoryItems) {
            JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Use a vertical layout for item card
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border for visual separation

            for (int i = 0; i < item.length; i++) {
                InventoryItemCard card = new InventoryItemCard(getLabel(i), item[i]);
                cardPanel.add(card);
            }

            cardsPanel.add(cardPanel);
        }

        JScrollPane scrollPane = new JScrollPane(cardsPanel); // Add scrolling functionality
        contentPane.add(scrollPane, BorderLayout.CENTER);

        frame.setContentPane(contentPane);
        frame.setVisible(true);
    }

    private static String getLabel(int index) {
        switch (index) {
            case 0:
                return "Name of Item";
            case 1:
                return "Price";
            case 2:
                return "Quantity Available";
            case 3:
                return "Item ID";
            default:
                return "";
        }
    }
}
