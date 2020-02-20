public class Loan {
    private int id;
    private int customerId;
    private double amount;
    private double interestRate;
    private int staffApprovedId;

    public Loan(int id, int customerId, double amount, double interestRate, int staffApprovedId) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.interestRate = interestRate;
        this.staffApprovedId = staffApprovedId;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getAmount() {
        return amount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public int getStaffApprovedId() {
        return staffApprovedId;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", customerId: " + customerId +
                ", amount: " + amount +
                ", interest rate: " + interestRate +
                ", staffApprovedId: " + staffApprovedId;
    }

}
