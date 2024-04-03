package javaSwing;

import javax.swing.*;
import java.awt.*;

public class EmployeeInfoCard extends JPanel {

    public EmployeeInfoCard(String label, String value) {
        setLayout(new GridLayout(2, 1)); // 2 rows, 1 column

        JLabel labelLabel = new JLabel(label + ":");
        JLabel valueLabel = new JLabel(value);

        add(labelLabel);
        add(valueLabel);
    }

    public static void main(String[] args) {
        // Example usage:
        JFrame frame = new JFrame("Employee Information");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JPanel cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Use a vertical layout for cards

        // Example data for testing
        String[][] employees = {
                {"John Doe", "1990-01-01", "$50000", "EMP001"},
                {"Alice Smith", "1985-05-15", "$60000", "EMP002"},
                {"Bob Johnson", "1978-12-30", "$75000", "EMP003"}
        };

        for (String[] employee : employees) {
            JPanel cardPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Use a vertical layout for employee card
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border for visual separation

            for (int i = 0; i < employee.length; i++) {
                EmployeeInfoCard card = new EmployeeInfoCard(getLabel(i), employee[i]);
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
                return "Name";
            case 1:
                return "DOB";
            case 2:
                return "Salary";
            case 3:
                return "Employee ID";
            default:
                return "";
        }
    }
}
