package lucas.mitiendatecnologica1;

public class Category {

    private int id; // Category ID
    private String name; // Category name

    // Constructor to initialize Category attributes
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters for the attributes
    public int getId() {
        return id; // Get category ID
    }

    public void setId(int id) {
        this.id = id; // Set category ID
    }

    public String getName() {
        return name; // Get category name
    }

    public void setName(String name) {
        this.name = name; // Set category name
    }
}
