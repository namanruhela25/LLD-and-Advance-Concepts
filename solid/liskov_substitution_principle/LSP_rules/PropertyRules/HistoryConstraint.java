package solid.liskov_substitution_principle.LSP_rules.PropertyRules;

/*
Subclass should not narrow down the functionality
*/

class BankAccount {
    protected double balance;

    public BankAccount(double b) {
        if (b < 0) throw new IllegalArgumentException("Balance can't be negative");
        this.balance = b;
    }

    // History Constraint: withdraw should be allowed
    public void withdraw(double amount) {
        if (balance - amount < 0) throw new RuntimeException("Insufficient funds");
        balance -= amount;
        System.out.println("Amount withdrawn. Remaining balance is " + balance);
    }
}

class FixedDepositAccount extends BankAccount {
    public FixedDepositAccount(double b) {
        super(b);
    }

    // LSP break! History constraint broken!
    // Parent class behavior changed: Now withdraw is not allowed.
    // This class will break client code that relies on withdraw.
    @Override
    public void withdraw(double amount) {
        throw new RuntimeException("Withdraw not allowed in Fixed Deposit");
    }
}


public class HistoryConstraint {
    public static void main(String[] args) {
        BankAccount acc1 = new BankAccount(5000);
        acc1.withdraw(2000);

        BankAccount acc2 = new FixedDepositAccount(5000);
        
        acc2.withdraw(2000); // LSP break! Throws exception instead of withdrawing.
    }
}
