package lucas.mitiendatecnologica1;

public class User {

    private int id; // User ID
    private String name; // User name
    private String email; // User email
    private String address; // User address

    // Constructor to initialize User attributes
    public User(int id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    // Getters and setters for the attributes
    public int getId() {
        return id; // Get user ID
    }

    public void setId(int id) {
        this.id = id; // Set user ID
    }

    public String getName() {
        return name; // Get user name
    }

    public void setName(String name) {
        this.name = name; // Set user name
    }

    public String getEmail() {
        return email; // Get user email
    }

    public void setEmail(String email) {
        this.email = email; // Set user email
    }

    public String getAddress() {
        return address; // Get user address
    }

    public void setAddress(String address) {
        this.address = address; // Set user address
    }
}
