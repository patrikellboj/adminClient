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
        int output;
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
                    // Uppdaterar namn
                    System.out.println("Enter value for new name: ");
                    String newName = scan.nextLine().trim();
                    output = repository.updateCustomerDetails("name", newName, customer.getId());
                    if(output != 0) {
                        System.out.println("Customer updatet to:\n"
                                + repository.getSpecificCustomer(ssnumber).toString());
                    } else {
                        System.out.println("Unexpected error occurred when updating name");
                    }
                    break;

                case "2":
                    // Uppdaterar personnummer
                    System.out.println("Enter value for new social security number: ");
                    String newSsnumber = scan.nextLine().trim();
                    output = repository.updateCustomerDetails("ssnumber", newSsnumber, customer.getId());
                    if(output != 0) {
                        System.out.println("Customer updatet to:\n"
                                + repository.getSpecificCustomer(newSsnumber).toString());
                    } else {
                        System.out.println("Unexpected error occurred when updating social security number");
                    }
                    break;

                case "3":
                    // Uppdaterar lösenord
                    System.out.println("Enter value for new password: ");
                    String newPass = scan.nextLine().trim();
                    output = repository.updateCustomerDetails("password", newPass, customer.getId());
                    if(output != 0) {
                        System.out.println("Customer updatet to:\n"
                                + repository.getSpecificCustomer(ssnumber).toString());
                    } else {
                        System.out.println("Unexpected error occurred when updating password");
                    }
                    break;
                default:
                    System.out.println("Didn't catch that.");
                    break;
            }
        } else {
            System.out.println("Could not find a customer with social security number: " + ssnumber);
        }
    }

    public void addCustomerAccount(String ssnumber) {
        try {
            int output;
            double balance;
            double interestRate;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                System.out.println("Creating an account for the following customer: " + customer.toString());
                System.out.println("Enter initial balance for account:");
                String tempBalance = scan.nextLine().trim();
                balance = Double.parseDouble(tempBalance);
                System.out.println("Enter interest rate (in %) for account:");
                String tempInterestRate = scan.nextLine().trim();
                interestRate = Double.parseDouble(tempInterestRate);

                output = repository.addCustomerAccount(customer.getId(), balance, interestRate);

                if(output != 0) {
                    System.out.println("An account with a balance of: " + balance +
                            " and an interest rate of: " + interestRate + "% was created for " + customer.getName());
                } else {
                    System.out.println("Unexpected error occurred when trying to create an account for " +
                            customer.getName());
                }
            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error occurred. Input value must be numeric values");
        }
    }

    public void deleteCustomer(String ssnumber) {
        Customer customer = repository.getSpecificCustomer(ssnumber);
        if(customer != null) {
            // todo kalla på metod som raderar kunddata i db'n
        } else {
            System.out.println("Could not find a customer with social security number: " + ssnumber);
        }

    }


}
