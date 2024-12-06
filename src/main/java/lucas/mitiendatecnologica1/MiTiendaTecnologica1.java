package lucas.mitiendatecnologica1;

import javax.swing.SwingUtilities;

public class MiTiendaTecnologica1 {

    public static void main(String[] args) {
        // Load initial data into the database from the JSON file
        System.out.println("Cargando datos iniciales del JSON...");
        JsonToDatabaseLoader.loadJsonData(); // Load data from JSON into the database
        System.out.println("Datos iniciales cargados exitosamente.");

        // Start the Login GUI
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(); // Create login frame
            loginFrame.setVisible(true); // Show login frame
        });
    }

    // Method to open the main GUI window
    public static void openMainGUI() {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(); // Create main frame
            mainFrame.setVisible(true); // Show main frame
        });
    }
}
