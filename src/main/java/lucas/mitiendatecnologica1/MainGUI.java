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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}

class MainFrame extends JFrame {

    private JComboBox<String> categoryComboBox;
    private JComboBox<String> productComboBox;
    private JTextField quantifyField;
    private JTextArea userInfoArea;
    private JTextArea purchaseRecordArea;

    public MainFrame() {
        setTitle("Figaro Tech - Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1000);
        setLayout(null);
        getContentPane().setBackground(new Color(35, 44, 55));
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JLabel logoLabel = createLogoLabel();
        add(logoLabel);

        JLabel titleLabel = new JLabel("FÍGARO TECH");
        titleLabel.setBounds(250, 30, 440, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        JPanel controlPanel = createControlPanel();
        add(controlPanel);

        userInfoArea = createUserInfoArea();
        add(userInfoArea);

        purchaseRecordArea = createPurchaseRecordArea();
        add(purchaseRecordArea);

        // Load data after components are fully initialized
        loadUserInfo();
        loadPurchaseRecord();
    }

    private JLabel createLogoLabel() {
        URL logoURL = getClass().getClassLoader().getResource("LogoMain.png");
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
        panel.setBounds(20, 130, 760, 150);
        panel.setLayout(null);
        panel.setBackground(new Color(82, 89, 103));

        JLabel categoryLabel = new JLabel("CATEGORÍA");
        categoryLabel.setBounds(20, 20, 100, 25);
        categoryLabel.setForeground(Color.WHITE);
        panel.add(categoryLabel);

        categoryComboBox = new JComboBox<>();
        categoryComboBox.setBounds(140, 20, 200, 25);
        populateCategories();
        categoryComboBox.addActionListener(e -> populateProducts());
        panel.add(categoryComboBox);

        JLabel productLabel = new JLabel("PRODUCTOS");
        productLabel.setBounds(20, 60, 100, 25);
        productLabel.setForeground(Color.WHITE);
        panel.add(productLabel);

        productComboBox = new JComboBox<>();
        productComboBox.setBounds(140, 60, 200, 25);
        productComboBox.addActionListener(e -> showProductImage());
        panel.add(productComboBox);

        JLabel quantifyLabel = new JLabel("CANTIDAD");
        quantifyLabel.setBounds(20, 100, 100, 25);
        quantifyLabel.setForeground(Color.WHITE);
        panel.add(quantifyLabel);

        quantifyField = new JTextField();
        quantifyField.setBounds(140, 100, 200, 25);
        panel.add(quantifyField);

        JButton buyButton = new JButton("COMPRAR");
        buyButton.setBounds(400, 60, 120, 30);
        buyButton.setBackground(new Color(35, 44, 55));
        buyButton.setForeground(Color.WHITE);
        buyButton.addActionListener(new BuyButtonActionListener());
        panel.add(buyButton);

        return panel;
    }

    private JTextArea createUserInfoArea() {
        JTextArea userInfoArea = new JTextArea();
        userInfoArea.setBounds(400, 300, 350, 100);
        userInfoArea.setBackground(new Color(200, 200, 200));
        userInfoArea.setEditable(false);
        userInfoArea.setBorder(BorderFactory.createTitledBorder("INFORMACIÓN DEL USUARIO"));
        return userInfoArea;
    }

    private JTextArea createPurchaseRecordArea() {
        JTextArea purchaseRecordArea = new JTextArea();
        purchaseRecordArea.setBounds(20, 420, 730, 200);
        purchaseRecordArea.setBackground(new Color(200, 200, 200));
        purchaseRecordArea.setEditable(false);
        purchaseRecordArea.setBorder(BorderFactory.createTitledBorder("HISTORIAL DE COMPRA"));
        return purchaseRecordArea;
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

    private void showProductImage() {
        String selectedProduct = (String) productComboBox.getSelectedItem();
        if (selectedProduct != null) {
            try (Connection connection = DatabaseConn.connect();
                 PreparedStatement statement = connection.prepareStatement("SELECT image FROM product WHERE name = ?")) {
                statement.setString(1, selectedProduct);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String imagePath = resultSet.getString("image");
                    ImageIcon productImageIcon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
                    JOptionPane.showMessageDialog(this, new JLabel(productImageIcon), "Imagen del producto", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserInfo() {
        try (Connection connection = DatabaseConn.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT name, email, address FROM user ORDER BY idUser DESC LIMIT 1")) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String userInfo = "NOMBRE: " + resultSet.getString("name") + "\nEMAIL: " + resultSet.getString("email") + "\nDIRECCIÓN: " + resultSet.getString("address");
                userInfoArea.setText(userInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPurchaseRecord() {
        try (Connection connection = DatabaseConn.connect();
             PreparedStatement statement = connection.prepareStatement("SELECT p.name, r.quantity, r.date FROM record r JOIN product p ON r.idProduct = p.idProduct WHERE r.idUser = (SELECT idUser FROM user ORDER BY idUser DESC LIMIT 1)")) {
            ResultSet resultSet = statement.executeQuery();
            StringBuilder history = new StringBuilder("Nombre\tCANTIDAD\tFECHA\n");
            while (resultSet.next()) {
                history.append(resultSet.getString("name")).append("\t").append(resultSet.getInt("quantity")).append("\t").append(resultSet.getString("date")).append("\n");
            }
            purchaseRecordArea.setText(history.toString());
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
                quantity = Integer.parseInt(quantifyField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Introduce una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedProduct != null && quantity > 0) {
                try (Connection connection = DatabaseConn.connect()) {
                    connection.setAutoCommit(false);

                    // Update inventory
                    PreparedStatement updateInventory = connection.prepareStatement("UPDATE product SET inventory = inventory - ? WHERE name = ? AND inventory >= ?");
                    updateInventory.setInt(1, quantity);
                    updateInventory.setString(2, selectedProduct);
                    updateInventory.setInt(3, quantity);
                    int rowsUpdated = updateInventory.executeUpdate();

                    if (rowsUpdated > 0) {
                        // Insert record
                        PreparedStatement insertRecord = connection.prepareStatement("INSERT INTO record (idUser, idProduct, quantity, date) VALUES ((SELECT idUser FROM user ORDER BY idUser DESC LIMIT 1), (SELECT idProduct FROM product WHERE name = ?), ?, DATE('now'))");
                        insertRecord.setString(1, selectedProduct);
                        insertRecord.setInt(2, quantity);
                        insertRecord.executeUpdate();

                        connection.commit();
                        JOptionPane.showMessageDialog(MainFrame.this, "Compra realizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadPurchaseRecord();
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "No hay suficientes artículos en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();// Esto es una prueba
                }
            }
        }
    }
}