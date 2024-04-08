package javaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierContactPage extends JFrame {

    private Connection connection;
    private JPanel cardsPanel;

    public SupplierContactPage() {
        setTitle("Supplier Contacts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        cardsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        connectToDatabase();
        fetchAndDisplaySupplierContacts();

        setVisible(true);
    }

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

    private void fetchAndDisplaySupplierContacts() {
        try {
            String query = "SELECT * FROM Supplier";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String supplierName = resultSet.getString("supplier_name");
                String contactPerson = resultSet.getString("contact_person");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                JPanel cardPanel = createSupplierCard(supplierName, contactPerson, phone, email, address);
                cardsPanel.add(cardPanel);
            }

            statement.close();
            cardsPanel.revalidate();
            cardsPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching supplier contacts.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createSupplierCard(String supplierName, String contactPerson, String phone, String email, String address) {
        JPanel cardPanel = new JPanel(new GridLayout(5, 1));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Supplier Name: " + supplierName);
        JLabel contactLabel = new JLabel("Contact Person: " + contactPerson);
        JLabel phoneLabel = new JLabel("Phone: " + phone);
        JLabel emailLabel = new JLabel("Email: " + email);
        JLabel addressLabel = new JLabel("Address: " + address);

        cardPanel.add(nameLabel);
        cardPanel.add(contactLabel);
        cardPanel.add(phoneLabel);
        cardPanel.add(emailLabel);
        cardPanel.add(addressLabel);

        return cardPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SupplierContactPage::new);
    }
}
