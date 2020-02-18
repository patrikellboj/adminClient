public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        Menu menu = new Menu(repository);
//        menu.getCustomers();
        menu.addCustomer("Janne", "671201", "123");
    }
}
