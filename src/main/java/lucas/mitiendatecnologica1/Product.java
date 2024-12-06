package lucas.mitiendatecnologica1;

public class Product {

    private int id; // Product ID
    private String name; // Product name
    private double price; // Product price
    private int inventario; // Product inventory

    // Constructor to initialize Product attributes
    public Product(int id, String name, double price, int inventario) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inventario = inventario;
    }

    // Getters and setters for the attributes
    public int getId() {
        return id; // Get product ID
    }

    public void setId(int id) {
        this.id = id; // Set product ID
    }

    public String getName() {
        return name; // Get product name
    }

    public void setName(String name) {
        this.name = name; // Set product name
    }

    public double getPrice() {
        return price; // Get product price
    }

    public void setPrice(double price) {
        this.price = price; // Set product price
    }

    public int getInventario() {
        return inventario; // Get product inventory
    }

    public void setInventario(int inventario) {
        this.inventario = inventario; // Set product inventory
    }
}
