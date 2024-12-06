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

public class LoginGUI {
    // Empty main method removed to avoid redundant code
}

class LoginFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;

    // Constructor to initialize the LoginFrame
    public LoginFrame() {
        setTitle("Fígaro Tech - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);
        getContentPane().setBackground(new Color(35, 44, 55));
        setLocationRelativeTo(null);

        initComponents(); // Initialize the UI components
    }

    // Method to initialize components
    private void initComponents() {
        JLabel logoLabel = createLogoLabel();
        add(logoLabel);

        JPanel panel = createMainPanel();
        add(panel);

        JLabel nameLabel = createNameLabel();
        panel.add(nameLabel);

        nameField = createNameField();
        panel.add(nameField);

        JLabel emailLabel = createEmailLabel();
        panel.add(emailLabel);

        emailField = createEmailField();
        panel.add(emailField);

        JButton loginButton = createLoginButton();
        panel.add(loginButton);
    }

    // Method to create the main panel
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 150, 400, 250);
        panel.setLayout(null);
        panel.setBackground(new Color(82, 89, 103));
        return panel;
    }

    // Method to create the logo label
    private JLabel createLogoLabel() {
        URL logoURL = getClass().getClassLoader().getResource("FigaroTech.png");
        if (logoURL != null) {
            ImageIcon originalLogoIcon = new ImageIcon(logoURL);
            Image scaledImage = originalLogoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setBounds((400 - 100) / 2, 20, 100, 100);
            return logoLabel;
        } else {
            JLabel logoLabel = new JLabel("Logo no encontrado", SwingConstants.CENTER);
            logoLabel.setBounds((400 - 140) / 2, 20, 140, 50);
            logoLabel.setForeground(Color.WHITE);
            return logoLabel;
        }
    }

    // Method to create name label
    private JLabel createNameLabel() {
        JLabel nameLabel = new JLabel("NOMBRE: ");
        nameLabel.setBounds(40, 20, 80, 25);
        nameLabel.setForeground(Color.WHITE);
        return nameLabel;
    }

    // Method to create name field
    private JTextField createNameField() {
        JTextField nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 25);
        return nameField;
    }

    // Method to create email label
    private JLabel createEmailLabel() {
        JLabel emailLabel = new JLabel("EMAIL: ");
        emailLabel.setBounds(40, 60, 80, 25);
        emailLabel.setForeground(Color.WHITE);
        return emailLabel;
    }

    // Method to create email field
    private JTextField createEmailField() {
        JTextField emailField = new JTextField();
        emailField.setBounds(120, 60, 150, 25);
        return emailField;
    }

    // Method to create login button
    private JButton createLoginButton() {
        JButton loginButton = new JButton("INICIO DE SESIÓN");
        loginButton.setBounds(125, 120, 140, 30);
        loginButton.setBackground(new Color(35, 44, 55));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginButtonActionListener());
        return loginButton;
    }

    // Method to validate user credentials and handle login process
    private void validateAndLogin(String name, String email) {
        try (Connection connection = DatabaseConn.connect()) {
            String checkUserQuery = "SELECT COUNT(*) FROM user WHERE email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Bienvenido de nuevo, " + name + ".", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                addUserToDB(name, email);
                JOptionPane.showMessageDialog(this, "Registro exitoso. Bienvenido, " + name + ".", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            }

            openMainGUI();
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add new user to the database
    private void addUserToDB(String name, String email) {
        try (Connection connection = DatabaseConn.connect()) {
            String query = "INSERT INTO user (name, email, address) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, "No address provided");
            statement.executeUpdate();
            System.out.println("Usuario agregado a la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Listener class to handle the login button action
    private class LoginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String email = emailField.getText();

            if (!name.isEmpty() && !email.isEmpty()) {
                validateAndLogin(name, email);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Por favor, ingrese su nombre y email.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to open the main GUI after login
    private void openMainGUI() {
        MiTiendaTecnologica1.openMainGUI();
    }
}
