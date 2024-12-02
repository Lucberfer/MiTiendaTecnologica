package lucas.mitiendatecnologica1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
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
        setSize(400, 300);
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
        panel.setBounds(50,30,280,200);
        panel.setLayout(null);
        panel.setBackground(new Color(82,89,103));
        return  panel;
    }

    private JLabel createLogoLabel() {

        ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("FigaroLogo.png"));
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(70, 10, 140, 50);
        return logoLabel;
    }


}
