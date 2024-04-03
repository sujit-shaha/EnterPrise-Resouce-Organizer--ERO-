package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class InventoryManagementPage extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Map<String, String[]> inventoryItems;

    public InventoryManagementPage() {
        setTitle("Inventory Management");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel leftNavigationPanel = createLeftNavigationPanel();
        JPanel rightNavigationPanel = createRightNavigationPanel();
//        JPanel navigationPanel = new JPanel(new GridLayout(1, 2, 10, 10));
//        navigationPanel.add(leftNavigationPanel);
//        navigationPanel.add(rightNavigationPanel);

        mainPanel.add(leftNavigationPanel, BorderLayout.WEST);
        mainPanel.add(rightNavigationPanel,BorderLayout.EAST);

        JPanel searchPanel = createSearchPanel();
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        setVisible(true);

        // Initialize inventory items
        initializeInventoryItems();
        displayInventoryItems();
    }

    private void initializeInventoryItems() {
        inventoryItems = new HashMap<>();
        inventoryItems.put("Item 1", new String[]{"Item 1", "100", "Supplier A"});
        inventoryItems.put("Item 2", new String[]{"Item 2", "50", "Supplier B"});
        inventoryItems.put("Item 3", new String[]{"Item 3", "75", "Supplier C"});
        inventoryItems.put("Item 4", new String[]{"Item 4", "200", "Supplier D"});
        // Add more inventory items if needed
    }

    private void displayInventoryItems() {
        cardsPanel.removeAll();
        for (Map.Entry<String, String[]> entry : inventoryItems.entrySet()) {
            String itemName = entry.getKey();
            String[] itemData = entry.getValue();
            JPanel cardPanel = createCardPanel(itemName, itemData);
            cardsPanel.add(cardPanel);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCardPanel(String itemName, String[] itemData) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + itemData[0]);
        JLabel quantityLabel = new JLabel("Quantity: " + itemData[1]);
        JLabel supplierLabel = new JLabel("Supplier: " + itemData[2]);

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
        for (Map.Entry<String, String[]> entry : inventoryItems.entrySet()) {
            String itemName = entry.getKey();
            String[] itemData = entry.getValue();
            if (itemName.toLowerCase().contains(keyword.toLowerCase())) {
                JPanel cardPanel = createCardPanel(itemName, itemData);
                cardsPanel.add(cardPanel);
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
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
                JOptionPane.showMessageDialog(null, "Action performed: " + text);
            }
        });
        return button;
    }

}
