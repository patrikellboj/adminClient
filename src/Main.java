public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        Menu menu = new Menu(repository);
//        menu.getCustomers();
//        menu.addCustomer("Örjan", "880000", "123");
//        menu.updateCustomerDetails("881212");
//        menu.addCustomerAccount("880000");
//        menu.deleteCustomer("761201");
        menu.deleteAccount("761201");
    }
}
