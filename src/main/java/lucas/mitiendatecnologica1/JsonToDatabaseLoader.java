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

            JSONObject store = (JSONObject) jsonObject.get("tienda");
            JSONArray categories = (JSONArray) store.get("categorias");
            JSONArray users = (JSONArray) store.get("usuarios");

            connection = DatabaseConn.connect();
            connection.setAutoCommit(false);

            for (Object categoryObj : categories) {
                JSONObject category = (JSONObject) categoryObj;
                String categoryName = (String) category.get("nombre");

                String insertCategoryQuery = "INSERT INTO category (name) VALUES (?)";
                PreparedStatement categoryStmt = connection.prepareStatement(insertCategoryQuery);
                categoryStmt.setString(1, categoryName);
                categoryStmt.executeUpdate();

                JSONArray products = (JSONArray) category.get("productos");
                if (products != null) {
                    for (Object productObj : products) {
                        JSONObject product = (JSONObject) productObj;
                        String insertProductQuery = "INSERT INTO product (idCategory, name, price, description, inventario) VALUES ((SELECT idCategory FROM category WHERE name = ?), ?, ?, ?, ?)";
                        PreparedStatement productStmt = connection.prepareStatement(insertProductQuery);
                        productStmt.setString(1, categoryName);
                        productStmt.setString(2, (String) product.get("nombre"));
                        productStmt.setDouble(3, (Double) product.get("precio"));
                        productStmt.setString(4, (String) product.get("descripcion"));
                        productStmt.setInt(5, ((Long) product.get("inventario")).intValue());
                        productStmt.executeUpdate();
                    }
                }
            }

            for (Object userObj : users) {
                JSONObject user = (JSONObject) userObj;
                String insertUserQuery = "INSERT INTO user (name, email, address) VALUES (?, ?, ?)";
                PreparedStatement userStmt = connection.prepareStatement(insertUserQuery);
                userStmt.setString(1, (String) user.get("nombre"));
                userStmt.setString(2, (String) user.get("email"));
                JSONObject addressObj = (JSONObject) user.get("direccion");
                String address = addressObj.get("calle") + " " + addressObj.get("numero") + ", " + addressObj.get("ciudad") + ", " + addressObj.get("pais");
                userStmt.setString(3, address);
                userStmt.executeUpdate();

                JSONArray purchaseHistory = (JSONArray) user.get("historialCompras");
                if (purchaseHistory != null) {
                    for (Object purchaseObj : purchaseHistory) {
                        JSONObject purchase = (JSONObject) purchaseObj;
                        String insertRecordQuery = "INSERT INTO record (idUser, idProduct, quantity, date) VALUES ((SELECT idUser FROM user WHERE email = ?), (SELECT idProduct FROM product WHERE idProduct = ?), ?, ?)";
                        PreparedStatement recordStmt = connection.prepareStatement(insertRecordQuery);
                        recordStmt.setString(1, (String) user.get("email"));
                        recordStmt.setInt(2, ((Long) purchase.get("productoId")).intValue());
                        recordStmt.setInt(3, ((Long) purchase.get("cantidad")).intValue());
                        recordStmt.setString(4, (String) purchase.get("fecha"));
                        recordStmt.executeUpdate();
                    }
                }
            }

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
