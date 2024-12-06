package lucas.mitiendatecnologica1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConn {

    private static final String URL = "jdbc:sqlite:tech.db"; // Database URL

    // Method to connect to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL); // Return connection to the database
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return null;
        }
    }

    // Method to create or update the tables in the database
    public static void createOrUpdateTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // SQL to create Users Table
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS user (
                        idUser INTEGER PRIMARY KEY AUTOINCREMENT,
                        name VARCHAR(60) NOT NULL,
                        email VARCHAR(60) NOT NULL UNIQUE,
                        address VARCHAR(100) NOT NULL
                    );
                    """;

            // SQL to create Categories Table
            String createCategoriesTable = """
                    CREATE TABLE IF NOT EXISTS category (
                        idCategory INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    );
                    """;

            // SQL to create Products Table
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

            // SQL to create Record Table
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

            // Execute SQL commands to create tables
            stmt.execute(createUsersTable);
            stmt.execute(createCategoriesTable);
            stmt.execute(createProductsTable);
            stmt.execute(createRecordTable);

            // Attempt to add 'inventario' column to 'product' table if it does not exist
            try {
                String addInventoryColumn = "ALTER TABLE product ADD COLUMN inventario INTEGER DEFAULT 0";
                stmt.execute(addInventoryColumn);
                System.out.println("Columna 'inventario' añadida a la tabla 'product'.");
            } catch (SQLException e) {
                System.out.println("La columna 'inventario' ya existe en la tabla 'product' o no se pudo añadir.");
            }

            System.out.println("Tablas creadas o modificadas correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
