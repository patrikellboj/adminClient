public class Customer {
    private int id;
    private String name;
    private String ssnumber;
    private String password;

    Customer (int id, String name, String ssnumber, String password) {
        this.id = id;
        this.name = name;
        this.ssnumber = ssnumber;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSsnumber() {
        return ssnumber;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", SS number: " + ssnumber +
                ", Password: " + password;
    }
}

