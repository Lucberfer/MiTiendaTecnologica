package lucas.mitiendatecnologica1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConn {

    private static final String URL = "jdbc:sqlite:tech.db";

    // Connect to the SQLite database
    public static Connection connect() {

        try {
            // Establish the connection
            Connection conn = DriverManager.getConnection(URL);
            if (conn != null) {
                // Ensure tables are created when connected
                createTables(conn);
            }

            return conn;

        } catch (SQLException e) {

            System.err.println("Error conectando con la base de datos");
            e.printStackTrace();
            return null;
        }
    }

    // Create the tables if they do not exist
    private static void createTables(Connection conn) {

        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS user (
                    idUser INTEGER PRIMARY KEY AUTOINCREMENT,
                    name VARCHAR(60) NOT NULL,
                    email VARCHAR(60) NOT NULL UNIQUE,
                    address VARCHAR(100) NOT NULL
                );
                """;

        String createCategoriesTable = """
                CREATE TABLE IF NOT EXISTS category (
                    idCategory INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
                """;

        String createProductsTable = """
                CREATE TABLE IF NOT EXISTS product (
                    idProduct INTEGER PRIMARY KEY AUTOINCREMENT,
                    idCategory INTEGER NOT NULL,
                    name VARCHAR(60) NOT NULL,
                    price REAL NOT NULL,
                    description VARCHAR(1000),
                    inventory INTEGER NOT NULL,
                    FOREIGN KEY (idCategory) REFERENCES category(idCategory)
                );
                """;

        String createRecordsTable = """
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

        try (Statement stmt = conn.createStatement()) {

            // Execute the creation statements
            stmt.execute(createUsersTable);
            stmt.execute(createCategoriesTable);
            stmt.execute(createProductsTable);
            stmt.execute(createRecordsTable);
            System.out.println("Tuplas creadas y verificadas.");

        } catch (SQLException e) {

            System.err.println("Error creando tuplas");
            e.printStackTrace();
        }
    }
}

