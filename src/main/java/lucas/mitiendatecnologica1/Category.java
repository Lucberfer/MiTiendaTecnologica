package lucas.mitiendatecnologica1;

// Class representing a category in the store
public class Category {

    private int id; // ID of the category
    private String name; // Name of the category

    // Constructor to initialize Category
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
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
}
