public class Account {
    private int id;
    private int customerId;
    private double balance;
    private double interestRate;

    public Account(int id, int customerId, double balance, double interestRate) {
        this.id = id;
        this.customerId = customerId;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getBalance() {
        return balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", customer ID: " + customerId +
                ", balance: " + balance +
                ", interest rate: " + interestRate;
    }
}
