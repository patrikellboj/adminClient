import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private static final Scanner scan = new Scanner(System.in);
    Repository repository;

    public Menu(Repository repository) {
        this.repository = repository;
    }

    public void addCustomer(String name, String ssnumber, String password) {
        // Kollar om en kund med parametervärdet ssnumber redan finns i databasen.
        ArrayList<Customer> allCustomersList = repository.getCustomers();
        for (Customer customer : allCustomersList) {
            if(ssnumber.equals(customer.getSsnumber())) {
                System.out.println(
                        "A customer with the provided social security number already exists in the database"
                );
                return;
            }
        }
        int addedCustomerId;
        addedCustomerId =  repository.addCustomer(name, ssnumber, password);
        if(addedCustomerId != 0) {
            System.out.println("Customer added to database with ID: " + addedCustomerId);
        } else {
            System.out.println("Something went wrong when trying to add a new customer");
        }
    }

    public void updateCustomerDetails(String ssnumber) {
        Customer customer = repository.getSpecificCustomer(ssnumber);
        if(customer != null) {
            System.out.println("Found the following customer: " + customer.toString());
            System.out.println("What would you like to update? \n" +
                    "1. for Name\n" +
                    "2. for SS Number\n" +
                    "3. for Password");
            String option = scan.nextLine();

            switch(option) {
                case "1":
                    // Uppdatera namn

                    break;
                case "2":
                    // Uppdatera Password

                    break;
                case "3":
                    // Uppdatera password
                    break;
                default:
                    // gör något vid default
//                    System.out.println("");
            }

        } else {
            System.out.println("Could not find a customer with social security number: " + ssnumber);
        }
    }


}
