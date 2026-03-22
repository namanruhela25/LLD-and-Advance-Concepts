package solid.liskov_substitution_principle.LSP_rules.PropertyRules;


class BankAccount {
    protected double balance;

    public BankAccount(double b) {
        if (b < 0) throw new IllegalArgumentException("Balance can't be negative");
        balance = b;
    }

    public void withdraw(double amount) {
        if (balance - amount < 0) throw new RuntimeException("Insufficient funds");
        balance -= amount;
        System.out.println("Amount withdrawn. Remaining balance is " + balance);
    }
}

// Breaks invariant: Should not be allowed.
class CheatAccount extends BankAccount {
    public CheatAccount(double b) {
        super(b);
    }

    @Override
    public void withdraw(double amount) {
        balance -= amount; // LSP break! Negative balance allowed
        System.out.println("Amount withdrawn. Remaining balance is " + balance);
    }
}

public class ClassInvariant {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount(5000);
        // acc.withdraw(6000); // throws exception 
        
        BankAccount acc1 = new CheatAccount(5000);

        acc1.withdraw(7000);
        // Output - Amount withdrawn. Remaining balance is -2000.0
    }
}
