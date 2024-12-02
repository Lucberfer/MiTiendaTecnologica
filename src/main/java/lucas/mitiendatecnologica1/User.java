package lucas.mitiendatecnologica1;

public class User {

    private int id;
    private String name;
    private String email;
    private String address;

    public User(int id, String name, String email, String address) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", address='" + address + '\'' + '}';
    }
}
