package solid.liskov_substitution_principle;

import java.util.ArrayList;
import java.util.List;

interface Account {
    void deposit(double amount);
    void withdraw(double amount);
}

class SavingsAccount implements Account {
    private double balance;

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}

class CurrentAccount implements Account {
    private double balance;

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}

class FixedDepositAccount implements Account {
    private double balance;

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public void withdraw(double amount) {
        throw new UnsupportedOperationException("Withdrawals are not allowed on a fixed deposit account");
    }
}

class BankClient {
    private List<Account> accounts;

    public BankClient(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void performTransactions() {
        for (Account account : accounts) {
            account.deposit(100);
            account.withdraw(50); // This will throw an exception for FixedDepositAccount
        }
    }

}

public class LSP_Violated {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new SavingsAccount());
        accounts.add(new CurrentAccount());
        accounts.add(new FixedDepositAccount());

        BankClient client = new BankClient(accounts);
        client.performTransactions(); // This will cause an exception due to the FixedDepositAccount
        
    }
}
