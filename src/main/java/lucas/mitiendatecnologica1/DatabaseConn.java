package lucas.mitiendatecnologica1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Utility class to manage database connection and setup
public class DatabaseConn {

    private static final String URL = "jdbc:sqlite:tech.db"; // Database URL

    // Method to establish a connection to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return null;
        }
    }

    // Method to create or update database tables
    public static void createOrUpdateTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // SQL statement to create the Users table if it doesn't exist
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS user (
                        idUser INTEGER PRIMARY KEY AUTOINCREMENT,
                        name VARCHAR(60) NOT NULL,
                        email VARCHAR(60) NOT NULL UNIQUE,
                        address VARCHAR(100) NOT NULL
                    );
                    """;

            // SQL statement to create the Categories table if it doesn't exist
            String createCategoriesTable = """
                    CREATE TABLE IF NOT EXISTS category (
                        idCategory INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    );
                    """;

            // SQL statement to create the Products table if it doesn't exist
            String createProductsTable = """
                    CREATE TABLE IF NOT EXISTS product (
                        idProduct INTEGER PRIMARY KEY AUTOINCREMENT,
                        idCategory INTEGER NOT NULL,
                        name VARCHAR(60) NOT NULL,
                        price REAL NOT NULL,
                        description VARCHAR(1000),
                        inventario INTEGER NOT NULL,
                        FOREIGN KEY (idCategory) REFERENCES category(idCategory)
                    );
                    """;

            // SQL statement to create the Record table if it doesn't exist
            String createRecordTable = """
                    CREATE TABLE IF NOT EXISTS record (
                        idRecord INTEGER PRIMARY KEY AUTOINCREMENT,
                        idUser INTEGER NOT NULL,
                        idProduct INTEGER NOT NULL,
                        quantity INTEGER NOT NULL,
                        date TEXT NOT NULL,
                        FOREIGN KEY (idUser) REFERENCES user(idUser),
                        FOREIGN KEY (idProduct) REFERENCES product(idProduct)
                    );
                    """;

            // Execute the table creation commands
            stmt.execute(createUsersTable);
            stmt.execute(createCategoriesTable);
            stmt.execute(createProductsTable);
            stmt.execute(createRecordTable);

            System.out.println("Tablas creadas o modificadas correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
