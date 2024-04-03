package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class InventorySearchPage extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Map<String, String[]> dummyInventoryData;

    public InventorySearchPage() {
        setTitle("Inventory Search");
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

        initializeDummyInventoryData();
        searchInventory("");

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
                searchInventory(searchField.getText());
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        return searchPanel;
    }

    private void initializeDummyInventoryData() {
        dummyInventoryData = new HashMap<>();
        dummyInventoryData.put("001", new String[]{"Laptop", "$1000", "10"});
        dummyInventoryData.put("002", new String[]{"Printer", "$300", "5"});
        dummyInventoryData.put("003", new String[]{"Desk Chair", "$150", "8"});
        dummyInventoryData.put("004", new String[]{"Monitor", "$200", "12"});
        dummyInventoryData.put("005", new String[]{"Keyboard", "$50", "15"});
    }

    private void searchInventory(String keyword) {
        cardsPanel.removeAll();
        for (Map.Entry<String, String[]> entry : dummyInventoryData.entrySet()) {
            String[] itemData = entry.getValue();
            if (matchesSearch(itemData, keyword)) {
                JPanel cardPanel = createCardPanel(itemData);
                cardsPanel.add(cardPanel);
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private boolean matchesSearch(String[] itemData, String keyword) {
        for (String info : itemData) {
            if (info.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private JPanel createCardPanel(String[] itemData) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + itemData[0]);
        JLabel priceLabel = new JLabel("Price: " + itemData[1]);
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
