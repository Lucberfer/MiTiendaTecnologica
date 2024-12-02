package lucas.mitiendatecnologica1;

import java.util.List;

/**
 *
 * @author lucas
 */
public class MiTiendaTecnologica1 {

    public static void main(String[] args) {

        // Create tables in the database if they do not exist
        DatabaseConn.createTables();

        // Dump the data from the JSON file into the database
        DatabaseConn.insertDataFromJson("tech.json");

        // Verify the data inserted into the database
        System.out.println("Usuarios en la base de datos:");
        List<User> users = DatabaseConn.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
        // shiiit
        System.out.println("\nPrograma ejecutado correctamente.");
    }
}
