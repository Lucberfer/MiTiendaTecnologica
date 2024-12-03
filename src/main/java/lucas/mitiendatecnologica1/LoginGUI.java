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
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

class LoginFrame extends JFrame {

    private JTextField nameField;

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

        JPanel panel = createMainPanel();
        add(panel);

        JLabel logoLabel = createLogoLabel();
        panel.add(logoLabel);

        JLabel nameLabel = createNameLabel();
        panel.add(nameLabel);

        nameField = createNameField();
        panel.add(nameField);

        JButton loginButtom = createLoginButton();
        panel.add(loginButtom);
    }

    private JPanel createMainPanel() {

        JPanel panel = new JPanel();
        panel.setBounds(0,150,400,250);
        panel.setLayout(null);
        panel.setBackground(new Color(82,89,103));
        return  panel;
    }

    private JLabel createLogoLabel() {

        URL logoURL = getClass().getClassLoader().getResource("FigaroTech.png");
        if (logoURL != null) {
            ImageIcon originalLogoIcon = new ImageIcon(logoURL);
            Image scaledImage = originalLogoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setBounds((400 - 100) / 2, 20, 100, 100); // Center the logo horizontally at the top of the frame
            return logoLabel;
        } else {
            JLabel logoLabel = new JLabel("Logo no encontrado", SwingConstants.CENTER);
            logoLabel.setBounds((400 - 140) / 2, 20, 140, 50); // Center the text horizontally at the top of the frame
            logoLabel.setForeground(Color.WHITE);
            return logoLabel;
        }
    }

    private JLabel createNameLabel() {

        JLabel nameLabel = new JLabel("NOMBRE: ");
        nameLabel.setBounds(40,80,80,25);
        nameLabel.setForeground(Color.WHITE);
        return nameLabel;
    }

    private JTextField createNameField() {

        JTextField nameField = new JTextField();
        nameField.setBounds(120,80,150,25);
        return nameField;
    }

    private JButton createLoginButton() {

        JButton loginButton = new JButton("INICIO DE SESIÓN");
        loginButton.setBounds(125,130,140,30);
        loginButton.setBackground(new Color(35,44,55));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new LoginButtonActionListener());
        return loginButton;
    }

    private void openNewGUI() {

        IntroFrame introFrame = new IntroFrame();
        introFrame.setVisible(true);
    }

    private void addNameDB(String name) {

        try (Connection connection= DatabaseConn.connect()) {
            String query = "INSERT INTO user (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class LoginButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String name = nameField.getText();

            if (!name.isEmpty()) {

                addNameDB(name);
                openNewGUI();
            } else {

                JOptionPane.showMessageDialog(LoginFrame.this, " Ingrese su nombre.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

class IntroFrame extends JFrame {

    public IntroFrame() {

        setTitle("Bienvenido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,200);
        setLayout(new FlowLayout());
        getContentPane().setBackground(new Color(35,44,55));
        setLocationRelativeTo(null);

        JLabel introlabel = new JLabel("Bienvenido a FÍGARO TECH");
        introlabel.setForeground(Color.WHITE);
        add(introlabel);
    }
}
