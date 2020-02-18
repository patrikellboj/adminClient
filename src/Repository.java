import javax.swing.*;
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

    // Metod som returnerar en customer med hjälp av personnummer.
    public Customer getSpecificCustomer(String ssnumberParam) {
        Customer customer = null;
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            PreparedStatement pstmt = conn.prepareStatement(
               "SELECT id, name, ssnumber, password " +
                    "FROM inl3.customer " +
                    "WHERE ? = customer.ssnumber"
            );
            pstmt.setString(1, ssnumberParam);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String ssnumber = rs.getString("ssnumber");
                String password = rs.getString("password");
                customer = new Customer(id, name, ssnumber, password);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    // Metod som returnerar en lista med alla customers.
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomersList;
    }

    // Metod för att lägga till en kund. Returnerar id'et på den kund som läggs till.
    // Returnerar 0 om något går fel.
    public int addCustomer(String name, String ssnumber, String password) {
        int addedCustomerId = 0;
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO customer(name, ssnumber, password) " +
                         "VALUES(?, ?, ?)",
                          Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, ssnumber);
            pstmt.setString(3, password);
            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    addedCustomerId = rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addedCustomerId;
    }

}
