package lucas.mitiendatecnologica1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lucas
 */
public class DatabaseConn {

    private static final String URL = "jdbc:sqlite:tech.db";

    // Connect to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos");
            e.printStackTrace();
            return null;
        }
    }

    // Create tables
    public static void createTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

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
                        prize REAL NOT NULL,
                        description VARCHAR(1000),
                        stock INTEGER NOT NULL,
                        FOREIGN KEY (idCategory) REFERENCES category(idCategory)
                    );
                    """;

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

            stmt.execute(createUsersTable);
            stmt.execute(createCategoriesTable);
            stmt.execute(createProductsTable);
            stmt.execute(createRecordTable);

            System.out.println("Tablas creadas correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


