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
                    "INSERT INTO inl3.customer(name, ssnumber, password) " +
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

    // Metod som uppdaterar en customer beroende på vad för värden som skickas in.
    // Returnerar 1 om en rad i databasen blev påverkad, annars 0.
    public int updateCustomerDetails(String column, String setValue, int customerId) {
        int rowAffected = 0;
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE customer " +
                         "SET " + column + " = ? " +
                         "WHERE id = ?");
            pstmt.setString(1, setValue);
            pstmt.setInt(2, customerId);
            rowAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowAffected;
    }

    // Metod för att lägga till en ett konto för en kund. Returnerar id'et på det konto som läggs till.
    // Returnerar 0 om något går fel.
    public int addCustomerAccount(int customerId, double balance, double interestRate) {
        int addedAccountId = 0;
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO inl3.account(customer_id, balance, interest_rate) " +
                         "VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, balance);
            pstmt.setDouble(3, interestRate);
            int rowAffected = pstmt.executeUpdate();
            if(rowAffected == 1) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    addedAccountId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addedAccountId;
    }

    // Metod som returnerar en kunds alla lån i form av en lista
    public ArrayList<Loan> getCustomerLoans(int customerIdParam) {
        ArrayList<Loan> customerLoans = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * " +
                         "FROM inl3.loan " +
                         "WHERE customer_id = ?");
            pstmt.setInt(1, customerIdParam);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                double amount = rs.getDouble("amount");
                double interestRate = rs.getDouble("interest_rate");
                int staffApprovedId = rs.getInt("staff_approved_id");
                customerLoans.add(new Loan(id, customerId, amount, interestRate, staffApprovedId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerLoans;
    }

    // Metod som returnerar en kunds alla konton i form av en lista
    public ArrayList<Account> getCustomerAccounts(int customerIdParam) {
        ArrayList<Account> customerAccounts = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * " +
                         "FROM inl3.account " +
                         "WHERE customer_id = ?");
            pstmt.setInt(1, customerIdParam);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                double balance = rs.getDouble("balance");
                double interestRate = rs.getDouble("interest_rate");
                customerAccounts.add(new Account(id, customerId, balance, interestRate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerAccounts;
    }

    // Metod som kallar på en stored procedure som raderar en kund och dess konton och lån.
    // Raderar allt, oavsett vad som finns på kontona eller lånen
    public void deleteCustomer(int customerId) {
        try(Connection conn = DriverManager.getConnection(prop.getProperty("DB_URL"), prop.getProperty("USER"), prop.getProperty("PASS"))) {
        CallableStatement cstmt = conn.prepareCall("CALL delete_customer(?);");
        cstmt.setInt(1, customerId);
        cstmt.execute();
        } catch (SQLException e) {
            System.out.println("Unexpected Error occurred when trying to delete customer");
            e.printStackTrace();
        }
    }

}
