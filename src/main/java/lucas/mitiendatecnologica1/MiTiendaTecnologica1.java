package lucas.mitiendatecnologica1;

import javax.swing.SwingUtilities;

public class MiTiendaTecnologica1 {

    public static void main(String[] args) {

        // Load initial data into the database from the JSON file
        System.out.println("Cargando datos iniciales del JSON...");
        JsonToDatabaseLoader.loadJsonData();
        System.out.println("Datos iniciales cargados exitosamente.");

        // Start the Login GUI
        SwingUtilities.invokeLater(() -> {

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    public static void openMainGUI() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
