package lucas.mitiendatecnologica1;

// Class representing a product in the store
public class Product {

    private int id; // ID of the product
    private String name; // Name of the product
    private double price; // Price of the product
    private int inventario; // Inventory count for the product

    // Constructor to initialize Product
    public Product(int id, String name, double price, int inventario) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inventario = inventario;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for inventory
    public int getInventario() {
        return inventario;
    }

    // Setter for inventory
    public void setInventario(int inventario) {
        this.inventario = inventario;
    }
}
