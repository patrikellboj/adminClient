public class Main {
    public static void main(String[] args) {
        Repository repository = new Repository();
        Menu menu = new Menu(repository);

        menu.enterUserAndPass();
        menu.mainMenu();
    }
}
