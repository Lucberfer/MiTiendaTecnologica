package lucas.mitiendatecnologica1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginGUI {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // Launch the LoginFrame graphical interface
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

// Class representing the login window
class LoginFrame extends JFrame {

    private JTextField nameField; // Text field where the user will enter their name

    public LoginFrame() {

        // Basic configuration for the login window
        setTitle("Fígaro Tech - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);
        getContentPane().setBackground(new Color(35, 44, 55)); // Dark background color
        setLocationRelativeTo(null); // Center the window on the screen

        initComponents(); // Initialize components
    }

    private void initComponents() {

        // Create and add the main panel
        JPanel panel = createMainPanel();
        add(panel);

        // Create and add the logo to the main panel
        JLabel logoLabel = createLogoLabel();
        panel.add(logoLabel);

        // Create and add the name label and text field
        JLabel nameLabel = createNameLabel();
        panel.add(nameLabel);

        nameField = createNameField();
        panel.add(nameField);

        // Create and add the login button
        JButton loginButton = createLoginButton();
        panel.add(loginButton);
    }

    private JPanel createMainPanel() {

        // Create the main panel for the interface
        JPanel panel = new JPanel();
        panel.setBounds(0, 150, 400, 250); // Position and size of the panel
        panel.setLayout(null);
        panel.setBackground(new Color(82, 89, 103)); // Background color of the panel
        return panel;
    }

    private JLabel createLogoLabel() {

        // Create the logo to display in the interface
        URL logoURL = getClass().getClassLoader().getResource("FigaroTech.png"); // Search for the image resource

        if (logoURL != null) {

            // If the logo is found, load and scale it to fit the desired size
            ImageIcon originalLogoIcon = new ImageIcon(logoURL);
            Image scaledImage = originalLogoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setBounds((400 - 100) / 2, 20, 100, 100); // Center the logo at the top of the panel
            return logoLabel;
        } else {

            // If the image is not found, display a message indicating that the logo is not available
            JLabel logoLabel = new JLabel("Logo no encontrado", SwingConstants.CENTER);
            logoLabel.setBounds((400 - 140) / 2, 20, 140, 50); // Center the text in the panel
            logoLabel.setForeground(Color.WHITE);
            return logoLabel;
        }
    }

    private JLabel createNameLabel() {

        // Create and configure the label for the name field
        JLabel nameLabel = new JLabel("NOMBRE ");
        nameLabel.setBounds(40, 80, 80, 25); // Position and size of the label
        nameLabel.setForeground(Color.WHITE); // Text color
        return nameLabel;
    }

    private JTextField createNameField() {

        // Create and configure the text field for entering the name
        JTextField nameField = new JTextField();
        nameField.setBounds(120, 80, 150, 25); // Position and size of the text field
        return nameField;
    }

    private JButton createLoginButton() {

        // Create and configure the login button
        JButton loginButton = new JButton("INICIAR SESIÓN");
        loginButton.setBounds(125, 130, 140, 30); // Position and size of the button
        loginButton.setBackground(new Color(35, 44, 55)); // Background color of the button
        loginButton.setForeground(Color.WHITE); // Text color of the button
        loginButton.addActionListener(new LoginButtonActionListener()); // Add an action event to the button
        return loginButton;
    }

    private void openNewGUI() {

        // Method to open a new window after successful login
        IntroFrame introFrame = new IntroFrame();
        introFrame.setVisible(true);
    }

    private void addNameDB(String name) {

        // Method to add the entered name to the database
        try (Connection connection = DatabaseConn.connect()) {

            String query = "INSERT INTO user (name) VALUES (?)"; // SQL query to insert the name
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate(); // Execute the query to insert the name into the database

        } catch (SQLException e) {

            // Error handling in case of failure in the connection or insertion
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error conectando a la base de dato.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class LoginButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Action to perform when the login button is pressed
            String name = nameField.getText(); // Get the name from the text field

            if (!name.isEmpty()) {

                // If the name is not empty, add the name to the database and open the new interface
                addNameDB(name);
                openNewGUI();
            } else {
                // Display an error message if the name field is empty
                JOptionPane.showMessageDialog(LoginFrame.this, "Introduzca tu nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// Class for the welcome window that appears after successful login
class IntroFrame extends JFrame {

    public IntroFrame() {

        // Basic configuration for the welcome window
        setTitle("Bienvenido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new FlowLayout());
        getContentPane().setBackground(new Color(35, 44, 55)); // Background color
        setLocationRelativeTo(null); // Center the window on the screen

        // Create and add the welcome label
        JLabel introLabel = new JLabel("Bienbenido FÍGARO TECH");
        introLabel.setForeground(Color.WHITE); // Text color
        add(introLabel);
    }
}

