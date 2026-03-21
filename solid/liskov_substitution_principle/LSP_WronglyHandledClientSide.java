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
            System.out.println("Amount left in Saving account : " + balance);
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
            System.out.println("Amount left in Current account : " + balance);
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
            account.deposit(4000);
            if (account instanceof FixedDepositAccount) {
                System.out.println("Skip the withdraw Operation for FixedDepositAccount");
            } else{
                try {
                    account.withdraw(1000);   
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }
            }
        }
    }
}

public class LSP_WronglyHandledClientSide {
    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new SavingsAccount());
        accounts.add(new CurrentAccount());
        accounts.add(new FixedDepositAccount());

        BankClient client = new BankClient(accounts);
        client.performTransactions();
    }
}
