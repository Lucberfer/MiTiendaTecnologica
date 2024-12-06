package lucas.mitiendatecnologica1;

import javax.swing.SwingUtilities;

public class MiTiendaTecnologica1 {

    // Main method to launch the application
    public static void main(String[] args) {

        // Load initial data into the database from the JSON file
        System.out.println("Cargando datos iniciales del JSON...");
        DatabaseConn.createOrUpdateTables(); // Ensure tables are created before loading data
        JsonToDatabaseLoader.loadJsonData();
        System.out.println("Datos iniciales cargados exitosamente.");

        // Start the Login GUI
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    // Method to open the main GUI after login
    public static void openMainGUI() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
