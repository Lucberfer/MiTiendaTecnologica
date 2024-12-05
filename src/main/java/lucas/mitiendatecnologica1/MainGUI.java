package lucas.mitiendatecnologica1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainGUI {

}

class MainFrame extends JFrame {

    private JComboBox<String> categoryComboBox;
    private JComboBox<String> productComboBox;
    private JTextField quantityField;

    public MainFrame() {
        setTitle("Figaro Tech - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);
        getContentPane().setBackground(new Color(35, 44, 55));
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JLabel logoLabel = createLogoLabel();
        add(logoLabel);

        JLabel titleLabel = new JLabel("FÍGARO TECH");
        titleLabel.setBounds(200, 20, 400, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        JPanel controlPanel = createControlPanel();
        add(controlPanel);
    }

    private JLabel createLogoLabel() {
        URL logoURL = getClass().getClassLoader().getResource("FigaroTech.png");
        if (logoURL != null) {
            ImageIcon originalLogoIcon = new ImageIcon(logoURL);
            Image scaledImage = originalLogoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon);
            logoLabel.setBounds(20, 10, 100, 100);
            return logoLabel;
        } else {
            JLabel logoLabel = new JLabel("Logo not found", SwingConstants.CENTER);
            logoLabel.setBounds(20, 10, 100, 100);
            logoLabel.setForeground(Color.WHITE);
            return logoLabel;
        }
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(20, 130, 760, 300);
        panel.setLayout(null);
        panel.setBackground(new Color(82, 89, 103));

        JLabel categoryLabel = new JLabel("Categoría:");
        categoryLabel.setBounds(20, 20, 100, 25);
        categoryLabel.setForeground(Color.WHITE);
        panel.add(categoryLabel);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.setBounds(140, 20, 200, 25);
        populateCategories();
        categoryComboBox.addActionListener(e -> populateProducts());
        panel.add(categoryComboBox);

        JLabel productLabel = new JLabel("Producto:");
        productLabel.setBounds(20, 60, 100, 25);
        productLabel.setForeground(Color.WHITE);
        panel.add(productLabel);

        productComboBox = new JComboBox<>();
        productComboBox.setBounds(140, 60, 200, 25);
        panel.add(productComboBox);

        JLabel quantityLabel = new JLabel("Cantidad:");
        quantityLabel.setBounds(20, 100, 100, 25);
        quantityLabel.setForeground(Color.WHITE);
        panel.add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(140, 100, 200, 25);
        panel.add(quantityField);

        JButton buyButton = new JButton("Comprar");
        buyButton.setBounds(400, 60, 120, 30);
        buyButton.setBackground(new Color(35, 44, 55));
        buyButton.setForeground(Color.WHITE);
        buyButton.addActionListener(new BuyButtonActionListener());
        panel.add(buyButton);

        return panel;
    }

    private void populateCategories() {
        try (Connection connection = DatabaseConn.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM category")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                categoryComboBox.addItem(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateProducts() {
        productComboBox.removeAllItems();
        String selectedCategory = (String) categoryComboBox.getSelectedItem();

        try (Connection connection = DatabaseConn.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT name FROM product WHERE idCategory = (SELECT idCategory FROM category WHERE name = ?)")) {
            statement.setString(1, selectedCategory);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                productComboBox.addItem(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class BuyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedProduct = (String) productComboBox.getSelectedItem();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Por favor, ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedProduct != null && quantity > 0) {
                try (Connection connection = DatabaseConn.connect()) {
                    connection.setAutoCommit(false);

                    PreparedStatement updateInventory = connection.prepareStatement("UPDATE product SET inventario = inventario - ? WHERE name = ? AND inventario >= ?");
                    updateInventory.setInt(1, quantity);
                    updateInventory.setString(2, selectedProduct);
                    updateInventory.setInt(3, quantity);
                    int rowsUpdated = updateInventory.executeUpdate();

                    if (rowsUpdated > 0) {
                        PreparedStatement insertRecord = connection.prepareStatement("INSERT INTO record (idUser, idProduct, quantity, date) VALUES ((SELECT idUser FROM user ORDER BY idUser DESC LIMIT 1), (SELECT idProduct FROM product WHERE name = ?), ?, DATE('now'))");
                        insertRecord.setString(1, selectedProduct);
                        insertRecord.setInt(2, quantity);
                        insertRecord.executeUpdate();

                        connection.commit();
                        JOptionPane.showMessageDialog(MainFrame.this, "Compra realizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "No hay suficientes artículos en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
