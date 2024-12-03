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

    public static void main (String[] args) {

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
    }
}