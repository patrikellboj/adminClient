public class Customer {
    int id;
    String name;
    String ssnumber;
    String password;

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
        return "User{" +
                "name='" + name + '\'' +
                ", ssnumber='" + ssnumber + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}

