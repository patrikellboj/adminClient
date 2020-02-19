public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        Menu menu = new Menu(repository);
//        menu.getCustomers();
//        menu.addCustomer("Erik", "761201", "123");
//        menu.updateCustomerDetails("881212");
//        menu.addCustomerAccount("761201");
        menu.deleteCustomer("761201");
    }
}
