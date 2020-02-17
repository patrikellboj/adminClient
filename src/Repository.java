import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


public class Repository {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    Properties prop = new Properties();

    public Repository() {
        try {
            prop.load(new FileInputStream("src\\config.properties"));
            Class.forName(JDBC_DRIVER);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> allCustomersList = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            Statement stmt = conn.createStatement();
            String sqlQuery = "SELECT id, name, ssnumber, password FROM inl3.customer";
            ResultSet rs = stmt.executeQuery(sqlQuery);
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String ssnumber = rs.getString("ssnumber");
                String password = rs.getString("password");
                allCustomersList.add(new Customer(id, name, ssnumber, password));
                System.out.println("id: " + id + ", name: " + name + ", ssnumber: " + ssnumber + ", password: " + password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allCustomersList;
    }
}
