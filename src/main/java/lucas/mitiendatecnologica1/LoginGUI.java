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
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            LoginFrame loginFrame = new LoginFrame();
//            loginFrame.setVisible(true);
//        });
//    }
}

class LoginFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;

    public LoginFrame() {
        setTitle("Fígaro Tech - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(null);
        getContentPane().setBackground(new Color(35, 44, 55));
        setLocationRelativeTo(null);

        initComponents();
    }

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

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 150, 400, 250);
        panel.setLayout(null);
        panel.setBackground(new Color(82, 89, 103));
        return panel;
    }

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

    private JLabel createNameLabel() {
        JLabel nameLabel = new JLabel("NOMBRE: ");
        nameLabel.setBounds(40, 20, 80, 25);
        nameLabel.setForeground(Color.WHITE);
        return nameLabel;
    }

    private JTextField createNameField() {
        JTextField nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 25);
        return nameField;
    }

    private JLabel createEmailLabel() {
        JLabel emailLabel = new JLabel("EMAIL: ");
        emailLabel.setBounds(40, 60, 80, 25);
        emailLabel.setForeground(Color.WHITE);
        return emailLabel;
    }

    private JTextField createEmailField() {
        JTextField emailField = new JTextField();
        emailField.setBounds(120, 60, 150, 25);
        return emailField;
    }

    private JButton createLoginButton() {
        JButton loginButton = new JButton("INICIO DE SESIÓN");
        loginButton.setBounds(125, 120, 140, 30);
        loginButton.setBackground(new Color(35, 44, 55));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginButtonActionListener());
        return loginButton;
    }

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
            dispose(); // Cerrar la ventana de inicio de sesión

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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


    private void openMainGUI() {
        MiTiendaTecnologica1.openMainGUI();
    }
}
