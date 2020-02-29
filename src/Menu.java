import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private static final Scanner scan = new Scanner(System.in);
    Repository repository;
    Admin adminLoggedIn = null;

    public Menu(Repository repository) {
        this.repository = repository;
    }

    public void enterUserAndPass() {
        try {
            while(adminLoggedIn == null) {
                System.out.println("Enter your username:");
                String username = scan.nextLine().trim();
                System.out.println("Enter your user password:");
                String password = scan.nextLine().trim();
                this.adminLoggedIn = checkValidUser(username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Admin checkValidUser(String name, String password) {
        for (Admin admin : repository.getAdmins()) {
            if(name.equalsIgnoreCase(admin.getName())) {
                if(password.equals(admin.getPassword())) {
                    System.out.println("Welcome " +
                            admin.getName() + ".");
                    return admin;
                } else {
                    System.out.println("Wrong password. Try again..");
                    return null;
                }
            }
        }
        System.out.println("Wrong username. Try again..");
        return null;
    }

    public void mainMenu() {
        while (true) {
            String ssnumber;
            System.out.println("\nMain menu\nChoose an option for what you would like to do");
            System.out.println(
                    "1 - to add a new customer.\n" +
                    "2 - to update customer details.\n" +
                    "3 - to delete a customer.\n" +
                    "4 - to add a customer account.\n" +
                    "5 - to close a customers account.\n" +
                    "6 - to deposit money to a customers account.\n" +
                    "7 - to withdraw money from a customers account.\n" +
                    "8 - to change interest rate for a customers account.\n" +
                    "9 - to approve a customers loan.\n" +
                    "10 - to change interest rate for a customers loan\n" +
                    "11 - to display the payment plan for a customers loan\n" +
                    "12 - to change the payment plan for a customers loan\n" +
                    "13 - to exit program");
            String option = scan.nextLine().trim();
            ssnumber = enterCustomerSsnumber();
            switch (option) {
                case "1":
                    String customerName;
                    String customerPassword;
                    System.out.println("Enter customers name:");
                    customerName = scan.nextLine().trim();
                    System.out.println("Enter customers desired password:");
                    customerPassword = scan.nextLine().trim();
                    addCustomer(customerName, ssnumber, customerPassword);
                    break;
                case "2":
                    updateCustomerDetails(ssnumber);
                    break;
                case "3":
                    deleteCustomer(ssnumber);
                    break;
                case "4":
                    addCustomerAccount(ssnumber);
                    break;
                case "5":
                    deleteAccount(ssnumber);
                    break;
                case "6":
                    depositMoney(ssnumber);
                    break;
                case "7":
                    withdrawMoney(ssnumber);
                    break;
                case "8":
                    updateAccountInterest(ssnumber);
                    break;
                case "9":
                    approveLoan(ssnumber);
                    break;
                case "10":
                    updateLoanInterest(ssnumber);
                    break;
                case "11":
                    getPaymentPlan(ssnumber);
                    break;
                case "12":
                    updatePaymentPlan(ssnumber);
                    break;
                case "13":
                    System.exit(0);
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        } // while loop end
    }

    public String enterCustomerSsnumber() {
        String ssnumber;
        System.out.println("Enter customers social security number:");
        ssnumber = scan.nextLine().trim();
        return ssnumber;
    }


    public void addCustomer(String name, String ssnumber, String password) {
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
                    "1 - for Name\n" +
                    "2 - for SS Number\n" +
                    "3 - for Password");
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
        ArrayList<Account> accounts;
        ArrayList<Loan> loans;
        Customer customer = repository.getSpecificCustomer(ssnumber);
        if(customer != null) {
            accounts = repository.getCustomerAccounts(customer.getId());
            loans = repository.getCustomerLoans(customer.getId());
            System.out.println("Are you sure you want to delete customer: " + customer.toString() + "\n" +
                    accounts.size() + " accounts and " + loans.size() + " loans belonging " + customer.getName() +
                    " will also be permanently deletet");
            System.out.println("1 - for Yes \n" +
                               "2 - for No");
            String answer = scan.nextLine().trim();
            if (answer.equals("1")) {
                repository.deleteCustomer(customer.getId());
                System.out.println("Customer was successfully deleted");
            } else {
                System.out.println("Customer was not deleted");
            }
        } else {
            System.out.println("Could not find a customer with social security number: " + ssnumber);
        }
    }

    public void deleteAccount(String ssnumber) {
        int output;
        ArrayList<Account> accounts;
        Customer customer = repository.getSpecificCustomer(ssnumber);
        if(customer != null) {
            accounts = repository.getCustomerAccounts(customer.getId());

            System.out.println(customer.getName()+ " has " + accounts.size() + " accounts");
            if(accounts.size() < 1) {
                System.out.println("Customer must have an account in order to delete one");
                return;
            }
            accounts.forEach((e) -> System.out.println(e.toString()));
            System.out.println("Enter the ID for the account you want to delete:");

            String accountIdTemp = scan.nextLine().trim();
            int accountId = Integer.parseInt(accountIdTemp);
            output = repository.deleteAccount(accountId);

            if(output != 0) {
                System.out.println("Account deleted");
            } else {
                System.out.println("Unexpected error occurred when trying to delete an account");
            }
        } else {
            System.out.println("Could not find a customer with social security number: " + ssnumber);
        }
    }

    public void depositMoney(String ssnumber) {
        try {
            String output;
            int accountId;
            double moneyToDeposit;
            ArrayList<Account> accounts;
            Customer customer = repository.getSpecificCustomer(ssnumber);

            if (customer != null) {
                accounts = repository.getCustomerAccounts(customer.getId());
                System.out.println(customer.getName()+ " has " + accounts.size() + " accounts");

                if(accounts.size() < 1) {
                    System.out.println("In order to deposit money the customer must first create an account");
                    return;
                }
                accounts.forEach((e) -> System.out.println(e.toString()));
                System.out.println("Enter the ID for the account you want to deposit money into:");

                String accountIdTemp = scan.nextLine().trim();
                accountId = Integer.parseInt(accountIdTemp);

                System.out.println("Enter how much do you want to deposit:");
                String moneyToDepositTemp = scan.nextLine().trim();
                moneyToDeposit = Double.parseDouble(moneyToDepositTemp);
                if(moneyToDeposit <= 0) {
                    System.out.println("Value to deposit must be positive");
                    return;
                }
                output = repository.depositMoney(customer.getId(), accountId, moneyToDeposit);
                System.out.println(output);
                accounts = repository.getCustomerAccounts(customer.getId());
                System.out.print("New balance for account: ");
                // Skriver ut balance på det konto som ändrades
                accounts.forEach(e->{
                    if(accountId == e.getId()){
                        System.out.print(e.getBalance());
                    }
                });
            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error occurred. Input value must be numeric values");
        }
    }

    public void withdrawMoney(String ssnumber) {
        try {
            String output;
            int accountId;
            double moneyToWithdraw;
            ArrayList<Account> accounts;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                accounts = repository.getCustomerAccounts(customer.getId());
                System.out.println(customer.getName() + " has " + accounts.size() + " accounts");

                if (accounts.size() < 1) {
                    System.out.println("In order to withdraw money the customer must first create an account");
                    return;
                }
                accounts.forEach((e) -> System.out.println(e.toString()));
                System.out.println("Enter the ID for the account you want to withdraw money from:");

                String accountIdTemp = scan.nextLine().trim();
                accountId = Integer.parseInt(accountIdTemp);

                System.out.println("Enter how much do you want to withdraw:");
                String moneyToWithdrawTemp = scan.nextLine().trim();
                moneyToWithdraw = Double.parseDouble(moneyToWithdrawTemp);
                if(moneyToWithdraw <= 0) {
                    System.out.println("Value to withdraw must be positive");
                    return;
                }
                output = repository.withdrawMoney(customer.getId(), accountId, moneyToWithdraw);
                System.out.println(output);
                accounts = repository.getCustomerAccounts(customer.getId());
                System.out.print("New balance for account: ");
                // Skriver ut balance på det konto som ändrades
                accounts.forEach(e->{
                    if(accountId == e.getId()){
                        System.out.print(e.getBalance());
                    }
                });
            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error occurred. Input value must be numeric values");
        }
    }

    public void updateAccountInterest(String ssnumber) {
        try {
            int output;
            int accountId;
            double newInterestRate;
            ArrayList<Account> accounts;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                accounts = repository.getCustomerAccounts(customer.getId());
                System.out.println(customer.getName() + " has " + accounts.size() + " accounts");

                if (accounts.size() < 1) {
                    System.out.println("In order to change interest rate the customer must first create an account");
                    return;
                }
                accounts.forEach((e) -> System.out.println(e.toString()));
                System.out.println("Enter the ID for the account you want to change interest rate on:");

                String accountIdTemp = scan.nextLine().trim();
                accountId = Integer.parseInt(accountIdTemp);

                System.out.println("Enter new interest rate");
                String newInterestRateTemp = scan.nextLine().trim();
                newInterestRate = Double.parseDouble(newInterestRateTemp);
//                if(newInterestRate <= 0) {
//                    System.out.println("Value interest rate must be positive");
//                    return;
//                }
                output = repository.updateAccountInterest(newInterestRate, accountId, customer.getId());
                if(output != 0) {
                    System.out.println("Interest rate changed!");
                    accounts = repository.getCustomerAccounts(customer.getId());
                    System.out.print("New interest rate for account: ");
                    // Skriver ut balance på det konto som ändrades
                    accounts.forEach(e->{
                        if(accountId == e.getId()){
                            System.out.print(e.getInterestRate());
                        }
                    });
                } else {
                    System.out.println("Unexpected error occurred when trying to change interest rate");
                }
            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error occurred. Input value must be numeric values");
        }
    }

    public void updateLoanInterest(String ssnumber) {
        try {
            int output;
            int loanId;
            double newInterestRate;
            ArrayList<Loan> loans;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                loans = repository.getCustomerLoans(customer.getId());
                System.out.println(customer.getName() + " has " + loans.size() + " loans");

                if (loans.size() < 1) {
                    System.out.println("In order to change interest rate the customer must first have a loan");
                    return;
                }
                loans.forEach((e) -> System.out.println(e.toString()));
                System.out.println("Enter the ID for the loan you want to change interest rate on:");

                String loanIdTemp = scan.nextLine().trim();
                loanId = Integer.parseInt(loanIdTemp);

                System.out.println("Enter new interest rate");
                String newInterestRateTemp = scan.nextLine().trim();
                newInterestRate = Double.parseDouble(newInterestRateTemp);
                if(newInterestRate <= 0) {
                    System.out.println("Value interest rate must be positive");
                    return;
                }
                output = repository.updateLoanInterest(newInterestRate, loanId, customer.getId());
                if(output != 0) {
                    System.out.println("Interest rate changed!");
                    loans = repository.getCustomerLoans(customer.getId());
                    System.out.print("New interest rate for loan: ");
                    loans.forEach(e->{
                        if(loanId == e.getId()){
                            System.out.print(e.getInterestRate());
                        }
                    });
                } else {
                    System.out.println("Unexpected error occurred when trying to change interest rate");
                }
            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error occurred. Input value must be numeric values");
        }
    }

    public void approveLoan(String ssnumber) {
        try {
            int output;
            int loanId;
            ArrayList<Loan> loans;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                loans = repository.getCustomerLoans(customer.getId());
                loans.removeIf(e -> e.getStaffApprovedId() != 0);
                if(loans.size() < 1) {
                    System.out.println("The customer did not have any loans that needed approval");
                    return;
                }
                System.out.println("The following loan/loans needs approval");
                loans.forEach((e) -> System.out.println(e.toString()));

                System.out.println("Enter the ID for the loan you want to approve:");
                String loanIdTemp = scan.nextLine().trim();
                loanId = Integer.parseInt(loanIdTemp);

                output = repository.approveLoan(adminLoggedIn.getId(), loanId, customer.getId());
                if(output != 0) {
                    System.out.println("Loan aproved by " + adminLoggedIn.getName() + ".");
                } else {
                    System.out.println("Unexpected error occurred when trying to approve loan");
                }
            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error occurred. Input value must be numeric values");
        }
    }

    public void getPaymentPlan(String ssnumber) {
        try {
            double years;
            double totalAmount;
            double interestRate;
            double interestAmount;
            int loanId;
            ArrayList<Loan> loans;
            Loan customerLoan = null;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                loans = repository.getCustomerLoans(customer.getId());
                if (loans.size() < 1) {
                    System.out.println("The customer did not have any loans");
                    return;
                }
                System.out.println("Customer has the following loans:");
                loans.forEach((e) -> System.out.println(e.toString()));
                System.out.println("\nEnter the ID for the loan you want to see the payment plan for:");
                String loanIdTemp = scan.nextLine().trim();
                loanId = Integer.parseInt(loanIdTemp);
                for (Loan loan : loans) {
                    if(loan.getId() == loanId) {
                        customerLoan = loan;
                    }
                }
                if(customerLoan == null) {
                    System.out.println("Unexpected error");
                    return;
                }

                years = repository.getPaymentPlan(loanId);
                 if(years == 0) {
                     System.out.println("Your loan does not seem to have a set time period");
                     return;
                 }

                interestRate = customerLoan.getInterestRate() * 0.01;
                interestAmount = (customerLoan.getAmount() * interestRate) * years;
                totalAmount = customerLoan.getAmount() + interestAmount;

                System.out.println("Payment plan:");
                System.out.println("Initial loan amount: " + customerLoan.getAmount() + "\n" +
                        "Interest rate: " + customerLoan.getInterestRate() + "%\n" +
                        "Loan runs for " + years + " years\n" +
                        "After " + years + " years a total amount of " + totalAmount + " needs to be paid back");

            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void updatePaymentPlan(String ssnumber) {
        try {
            double years;
            int output;
            int loanId;
            ArrayList<Loan> loans;
            Loan customerLoan = null;
            Customer customer = repository.getSpecificCustomer(ssnumber);
            if (customer != null) {
                loans = repository.getCustomerLoans(customer.getId());
                if (loans.size() < 1) {
                    System.out.println("The customer did not have any loans. Can not update update any payment plan");
                    return;
                }
                System.out.println("Customer has the following loans:");
                loans.forEach((e) -> System.out.println(e.toString()));
                System.out.println("\nEnter the ID for the loan you want to change the payment plan for:");
                String loanIdTemp = scan.nextLine().trim();
                loanId = Integer.parseInt(loanIdTemp);
                for (Loan loan : loans) {
                    if(loan.getId() == loanId) {
                        customerLoan = loan;
                    }
                }
                if(customerLoan == null) {
                    System.out.println("Unexpected error");
                    return;
                }

                years = repository.getPaymentPlan(loanId);
                System.out.println("Current payment plan is set for " + years + " years" +
                        "\nDo you want to change that?" +
                        "\n1 - for Yes" +
                        "\n2 - for No");

                String option = scan.nextLine().trim();

                switch(option) {
                    case "1":
                        System.out.println("Enter the amount of years you want to set the payment plan for");
                        String yearsToChangeToTemp = scan.nextLine().trim();
                        double yearsToChangeTo = Double.parseDouble(yearsToChangeToTemp);
                        output = repository.updatePaymentPlan(yearsToChangeTo, loanId);
                        if (output != 0) {
                            System.out.println("Payment plan updated to: " + yearsToChangeTo + " years");
                        } else {
                            System.out.println("Unexpected error occurred when updating payment plan");
                        }
                        break;
                    case "2":
                        break;
                    default:
                        System.out.println("Didn't catch that.");
                        break;
                }

            } else {
                System.out.println("Could not find a customer with social security number: " + ssnumber);
            }
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}