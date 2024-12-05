package lucas.mitiendatecnologica1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JsonToDatabaseLoader {

    public static void loadJsonData() {
        Connection connection = null;

        try (InputStream is = JsonToDatabaseLoader.class.getClassLoader().getResourceAsStream("tech.json")) {
            if (is == null) {
                throw new RuntimeException("El archivo JSON 'tech.json' no fue encontrado. Asegúrate de que esté en la carpeta src/main/resources.");
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(is));

            // Verificar si el objeto JSON no es null
            if (jsonObject == null) {
                throw new RuntimeException("El JSON parseado es null, revisa la estructura del archivo 'tech.json'.");
            }

            // Obtener el objeto 'tienda'
            JSONObject store = (JSONObject) jsonObject.get("tienda");
            if (store == null) {
                throw new RuntimeException("No se encontró el objeto 'tienda' en el archivo JSON. Verifica la estructura del archivo 'tech.json'.");
            }

            // Obtener las categorías y los usuarios
            JSONArray categories = (JSONArray) store.get("categorias");
            if (categories == null) {
                throw new RuntimeException("No se encontró el array 'categorias' en el objeto 'tienda' del archivo JSON.");
            }

            JSONArray users = (JSONArray) store.get("usuarios");
            if (users == null) {
                throw new RuntimeException("No se encontró el array 'usuarios' en el objeto 'tienda' del archivo JSON.");
            }

            // Abrir conexión a la base de datos
            connection = DatabaseConn.connect();
            connection.setAutoCommit(false);

            // Insertar Categorías y Productos
            for (Object categoryObj : categories) {
                JSONObject category = (JSONObject) categoryObj;
                String categoryName = (String) category.get("nombre");

                // Insertar Categoría
                String insertCategoryQuery = "INSERT INTO category (name) VALUES (?)";
                try (PreparedStatement categoryStmt = connection.prepareStatement(insertCategoryQuery)) {
                    categoryStmt.setString(1, categoryName);
                    categoryStmt.executeUpdate();
                }

                // Insertar Productos
                JSONArray products = (JSONArray) category.get("productos");
                if (products != null) {
                    for (Object productObj : products) {
                        JSONObject product = (JSONObject) productObj;
                        String productName = (String) product.get("nombre");
                        double price = (double) product.get("precio");
                        String description = (String) product.get("descripcion");

                        // Obtener inventario y manejar el valor nulo
                        Long inventarioValue = (Long) product.get("inventario");
                        int inventario = (inventarioValue != null) ? inventarioValue.intValue() : 0;

                        String insertProductQuery = "INSERT INTO product (idCategory, name, price, description, inventary) VALUES ((SELECT idCategory FROM category WHERE name = ?), ?, ?, ?, ?)";
                        try (PreparedStatement productStmt = connection.prepareStatement(insertProductQuery)) {
                            productStmt.setString(1, categoryName);
                            productStmt.setString(2, productName);
                            productStmt.setDouble(3, price);
                            productStmt.setString(4, description);
                            productStmt.setInt(5, inventario);
                            productStmt.executeUpdate();
                        }
                    }
                } else {
                    System.err.println("No se encontraron productos para la categoría: " + categoryName);
                }
            }

            // Insertar Usuarios y el Historial de Compras
            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;
                String userName = (String) user.get("nombre");
                String email = (String) user.get("email");

                JSONObject addressObj = (JSONObject) user.get("direccion");
                String address = (addressObj != null) ? addressObj.get("calle") + " " + addressObj.get("numero") + ", " + addressObj.get("ciudad") + ", " + addressObj.get("pais") : "Sin dirección";

                String insertUserQuery = "INSERT INTO user (name, email, address) VALUES (?, ?, ?)";
                try (PreparedStatement userStmt = connection.prepareStatement(insertUserQuery)) {
                    userStmt.setString(1, userName);
                    userStmt.setString(2, email);
                    userStmt.setString(3, address);
                    userStmt.executeUpdate();
                }

                // Insertar Historial de Compras
                JSONArray purchaseHistory = (JSONArray) user.get("historialCompras");
                if (purchaseHistory != null) {
                    for (Object purchaseObj : purchaseHistory) {
                        JSONObject purchase = (JSONObject) purchaseObj;
                        String insertRecordQuery = "INSERT INTO record (idUser, idProduct, quantity, date) VALUES ((SELECT idUser FROM user WHERE name = ?), (SELECT idProduct FROM product WHERE idProduct = ?), ?, ?)";
                        try (PreparedStatement recordStmt = connection.prepareStatement(insertRecordQuery)) {
                            recordStmt.setString(1, userName);
                            recordStmt.setInt(2, ((Long) purchase.get("productoId")).intValue());
                            recordStmt.setInt(3, ((Long) purchase.get("cantidad")).intValue());
                            recordStmt.setString(4, (String) purchase.get("fecha"));
                            recordStmt.executeUpdate();
                        }
                    }
                } else {
                    System.err.println("No se encontró historial de compras para el usuario: " + userName);
                }
            }

            // Commit de la transacción
            connection.commit();
            System.out.println("Datos iniciales cargados exitosamente.");

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Transacción revertida debido a un error.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error al parsear el archivo JSON 'tech.json'. Revisa el formato del archivo.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
