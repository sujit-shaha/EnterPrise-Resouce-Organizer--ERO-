package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class EmployeeSearchScreen extends JFrame {

    private JTextField searchField;
    private JPanel cardsPanel;
    private Map<String, String[]> dummyData;

    public EmployeeSearchScreen() {
        setTitle("Employee Search");
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

        initializeDummyData();
        searchEmployee("");

        setVisible(true);
    }

    public JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Employee"));
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchEmployee(searchField.getText());
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);
        return searchPanel;
    }

    private void initializeDummyData() {
        dummyData = new HashMap<>();
        dummyData.put("EMP001", new String[]{"John Doe", "1980-01-01", "$50000"});
        dummyData.put("EMP002", new String[]{"Jane Smith", "1985-05-15", "$60000"});
        dummyData.put("EMP003", new String[]{"Alice Johnson", "1990-10-20", "$55000"});
        dummyData.put("EMP004", new String[]{"Robert Brown", "1978-07-25", "$52000"});
        dummyData.put("EMP005", new String[]{"Emily Davis", "1982-12-12", "$58000"});
        dummyData.put("EMP006", new String[]{"Michael Wilson", "1975-09-30", "$62000"});
        dummyData.put("EMP007", new String[]{"Emma Miller", "1988-03-18", "$54000"});
        dummyData.put("EMP008", new String[]{"David Martinez", "1987-06-05", "$57000"});
        dummyData.put("EMP009", new String[]{"Sophia Anderson", "1995-11-08", "$59000"});
        dummyData.put("EMP010", new String[]{"Daniel Taylor", "1983-04-23", "$51000"});
    }

    private void searchEmployee(String keyword) {
        cardsPanel.removeAll();
        for (Map.Entry<String, String[]> entry : dummyData.entrySet()) {
            String[] employeeData = entry.getValue();
            if (matchesSearch(employeeData, keyword)) {
                JPanel cardPanel = createCardPanel(employeeData);
                cardsPanel.add(cardPanel);
            }
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private boolean matchesSearch(String[] employeeData, String keyword) {
        for (String info : employeeData) {
            if (info.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private JPanel createCardPanel(String[] employeeData) {
        JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        for (int i = 0; i < employeeData.length; i++) {
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(getLabel(i) + ": ");
            label.setFont(new Font("Arial", Font.BOLD, 14));
            JLabel value = new JLabel(employeeData[i]);
            infoPanel.add(label);
            infoPanel.add(value);
            cardPanel.add(infoPanel);
        }

        return cardPanel;
    }

    private static String getLabel(int index) {
        switch (index) {
            case 0:
                return "Name";
            case 1:
                return "DOB";
            case 2:
                return "Salary";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeSearchScreen::new);
    }
}
