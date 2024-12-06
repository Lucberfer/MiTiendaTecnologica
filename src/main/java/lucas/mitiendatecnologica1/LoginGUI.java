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

}

class LoginFrame extends JFrame {

    private JTextField nameField; // Input field for user name
    private JTextField emailField; // Input field for user email

    // Constructor to set up the Login Frame properties
    public LoginFrame() {
        setTitle("Fígaro Tech - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);
        getContentPane().setBackground(new Color(35, 44, 55));
        setLocationRelativeTo(null);

        initComponents(); // Initialize components of the frame
    }

    // Initialize all components of the login window
    private void initComponents() {
        JLabel logoLabel = createLogoLabel(); // Create logo
        add(logoLabel);

        JPanel panel = createMainPanel(); // Create main panel
        add(panel);

        JLabel nameLabel = createNameLabel(); // Create name label
        panel.add(nameLabel);

        nameField = createNameField(); // Create name input field
        panel.add(nameField);

        JLabel emailLabel = createEmailLabel(); // Create email label
        panel.add(emailLabel);

        emailField = createEmailField(); // Create email input field
        panel.add(emailField);

        JButton loginButton = createLoginButton(); // Create login button
        panel.add(loginButton);
    }

    // Create main panel of the login form
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 150, 400, 250);
        panel.setLayout(null);
        panel.setBackground(new Color(82, 89, 103));
        return panel;
    }

    // Create the logo label for the login form
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

    // Create the name label
    private JLabel createNameLabel() {
        JLabel nameLabel = new JLabel("NOMBRE: ");
        nameLabel.setBounds(40, 20, 80, 25);
        nameLabel.setForeground(Color.WHITE);
        return nameLabel;
    }

    // Create the name input field
    private JTextField createNameField() {
        JTextField nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 25);
        return nameField;
    }

    // Create the email label
    private JLabel createEmailLabel() {
        JLabel emailLabel = new JLabel("EMAIL: ");
        emailLabel.setBounds(40, 60, 80, 25);
        emailLabel.setForeground(Color.WHITE);
        return emailLabel;
    }

    // Create the email input field
    private JTextField createEmailField() {
        JTextField emailField = new JTextField();
        emailField.setBounds(120, 60, 150, 25);
        return emailField;
    }

    // Create the login button and set the action listener for login logic
    private JButton createLoginButton() {
        JButton loginButton = new JButton("INICIO DE SESIÓN");
        loginButton.setBounds(125, 120, 140, 30);
        loginButton.setBackground(new Color(35, 44, 55));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginButtonActionListener()); // Attach action listener to the button
        return loginButton;
    }

    // Method to validate if the user exists and proceed to login or register a new user
    private void validateAndLogin(String name, String email) {
        try (Connection connection = DatabaseConn.connect()) {
            // Verificar si el usuario ya existe
            String checkUserQuery = "SELECT COUNT(*) FROM user WHERE email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Si el usuario ya existe, permitir el acceso
                JOptionPane.showMessageDialog(this, "Bienvenido de nuevo, " + name + ".", "Inicio de Sesión Exitoso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Si el usuario no existe, agregarlo a la base de datos
                addUserToDB(name, email);
                JOptionPane.showMessageDialog(this, "Registro exitoso. Bienvenido, " + name + ".", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            }

            // Iniciar la interfaz principal
            openMainGUI();
            dispose(); // Close login window

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new user to the database
    private void addUserToDB(String name, String email) {
        try (Connection connection = DatabaseConn.connect()) {
            String query = "INSERT INTO user (name, email, address) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, "No address provided"); // Set a default address if not provided
            statement.executeUpdate();
            System.out.println("Usuario agregado a la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Action listener for the login button to validate user data and proceed with login or registration
    private class LoginButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameField.getText();
            String email = emailField.getText();

            if (!name.isEmpty() && !email.isEmpty()) {
                validateAndLogin(name, email); // Validate and proceed with login
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Por favor, ingrese su nombre y email.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to open the main GUI after a successful login
    private void openMainGUI() {
        MiTiendaTecnologica1.openMainGUI();
    }
}
