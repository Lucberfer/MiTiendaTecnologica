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

        // Use JSONParser to parse the JSON file
        JSONParser parser = new JSONParser();

        try (InputStream is = JsonToDatabaseLoader.class.getClassLoader().getResourceAsStream("tech.json")) {
            if (is == null) {
                throw new RuntimeException("JSON no encontrado.");
            }

            // Parse JSON file into a JSONObject
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(is));

            // Extract categories and users arrays
            JSONObject storeObject = (JSONObject) jsonObject.get("store");
            JSONArray categories = (JSONArray) storeObject.get("categories");
            JSONArray users = (JSONArray) storeObject.get("users");

            // Establish connection to the database
            try (Connection connection = DatabaseConn.connect()) {
                connection.setAutoCommit(false);

                // Insert Categories and Products
                for (Object categoryObj : categories) {
                    JSONObject category = (JSONObject) categoryObj;
                    String categoryName = (String) category.get("name");

                    // Insert Category
                    String insertCategoryQuery = "INSERT INTO category (name) VALUES (?)";
                    PreparedStatement categoryStmt = connection.prepareStatement(insertCategoryQuery);
                    categoryStmt.setString(1, categoryName);
                    categoryStmt.executeUpdate();

                    // Insert Products
                    JSONArray products = (JSONArray) category.get("products");
                    for (Object productObj : products) {
                        JSONObject product = (JSONObject) productObj;
                        String productName = (String) product.get("name");
                        double price = ((Number) product.get("price")).doubleValue();
                        String description = (String) product.get("description");
                        int inventory = ((Number) product.get("inventory")).intValue();

                        String insertProductQuery = "INSERT INTO product (idCategory, name, price, description, inventory) VALUES ((SELECT idCategory FROM category WHERE name = ?), ?, ?, ?, ?)";
                        PreparedStatement productStmt = connection.prepareStatement(insertProductQuery);
                        productStmt.setString(1, categoryName);
                        productStmt.setString(2, productName);
                        productStmt.setDouble(3, price);
                        productStmt.setString(4, description);
                        productStmt.setInt(5, inventory);
                        productStmt.executeUpdate();
                    }
                }

                // Insert Users and Purchase History
                for (Object userObj : users) {
                    JSONObject user = (JSONObject) userObj;
                    String userName = (String) user.get("name");
                    String email = (String) user.get("email");
                    String address = user.get("address").toString();

                    String insertUserQuery = "INSERT INTO user (name, email, address) VALUES (?, ?, ?)";
                    PreparedStatement userStmt = connection.prepareStatement(insertUserQuery);
                    userStmt.setString(1, userName);
                    userStmt.setString(2, email);
                    userStmt.setString(3, address);
                    userStmt.executeUpdate();

                    // Insert Purchase History
                    JSONArray purchaseHistory = (JSONArray) user.get("purchaseHistory");
                    for (Object purchaseObj : purchaseHistory) {
                        JSONObject purchase = (JSONObject) purchaseObj;
                        int productId = ((Number) purchase.get("productId")).intValue();
                        int quantity = ((Number) purchase.get("quantity")).intValue();
                        String date = (String) purchase.get("date");

                        String insertRecordQuery = "INSERT INTO record (idUser, idProduct, quantity, date) VALUES ((SELECT idUser FROM user WHERE name = ?), (SELECT idProduct FROM product WHERE idProduct = ?), ?, ?)";
                        PreparedStatement recordStmt = connection.prepareStatement(insertRecordQuery);
                        recordStmt.setString(1, userName);
                        recordStmt.setInt(2, productId);
                        recordStmt.setInt(3, quantity);
                        recordStmt.setString(4, date);
                        recordStmt.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            System.err.println("Error parseando el json.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
