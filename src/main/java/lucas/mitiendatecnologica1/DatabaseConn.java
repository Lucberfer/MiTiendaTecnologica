package lucas.mitiendatecnologica1;

import org.json.JSONArray;
import org.json.JSONObject;

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

    // Insert data from JSON file
    public static void insertDataFromJson(String jsonFilePath) {
        try (Connection conn = connect()) {
            // Load JSON from the resources folder
            InputStream inputStream = DatabaseConn.class.getClassLoader().getResourceAsStream(jsonFilePath);
            if (inputStream == null) {
                System.err.println("No se pudo encontrar el archivo JSON en la ruta: " + jsonFilePath);
                return;
            }

            // Read JSON file content
            String jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(jsonString);

            // Insert users
            JSONArray users = jsonObject.getJSONObject("tienda").getJSONArray("usuarios");
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                insertUser(
                        user.getInt("id"),
                        user.getString("nombre"),
                        user.getString("email"),
                        user.getJSONObject("direccion").getString("calle") + " " + user.getJSONObject("direccion").getInt("numero") + ", "
                                + user.getJSONObject("direccion").getString("ciudad") + ", " + user.getJSONObject("direccion").getString("pais")
                );
            }

            // Insert categories and products
            JSONArray categories = jsonObject.getJSONObject("tienda").getJSONArray("categorias");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject category = categories.getJSONObject(i);
                insertCategory(category.getInt("id"), category.getString("nombre"));

                JSONArray products = category.getJSONArray("productos");
                for (int j = 0; j < products.length(); j++) {
                    JSONObject product = products.getJSONObject(j);
                    insertProduct(
                            product.getInt("id"),
                            category.getInt("id"),
                            product.getString("nombre"),
                            product.getDouble("precio"),
                            product.getString("descripcion"),
                            product.getInt("inventario")
                    );
                }
            }

            System.out.println("Datos del JSON volcados correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Insert a user
    private static void insertUser(int idUser, String name, String email, String address) {
        String query = "INSERT OR IGNORE INTO user (idUser, name, email, address) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idUser);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, address);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a category
    private static void insertCategory(int idCategory, String name) {
        String query = "INSERT OR IGNORE INTO category (idCategory, name) VALUES (?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idCategory);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert a product
    private static void insertProduct(int idProduct, int idCategory, String name, double prize, String description, int stock) {
        String query = "INSERT OR IGNORE INTO product (idProduct, idCategory, name, prize, description, stock) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idProduct);
            pstmt.setInt(2, idCategory);
            pstmt.setString(3, name);
            pstmt.setDouble(4, prize);
            pstmt.setString(5, description);
            pstmt.setInt(6, stock);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
