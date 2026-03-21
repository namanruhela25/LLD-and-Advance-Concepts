package solid.liskov_substitution_principle;

import java.util.ArrayList;
import java.util.List;

// 1. DepositOnlyAccount interface: only allows deposits
interface DepositOnlyAccount {
    void deposit(double amount);
}

interface WithdrawableAccount extends DepositOnlyAccount {
    void withdraw(double amount);
}

class SavingsAccount implements WithdrawableAccount {

    private double balance;

    public SavingsAccount() {
        balance = 0;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount + " in Savings Account. New Balance: " + balance);
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + " from Savings Account. New Balance: " + balance);
        } else {
            System.out.println("Insufficient funds in Savings Account!");
        }
    }
}

class CurrentAccount implements WithdrawableAccount {
    private double balance;

    public CurrentAccount() {
        balance = 0;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount + " in Current Account. New Balance: " + balance);
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + " from Current Account. New Balance: " + balance);
        } else {
            System.out.println("Insufficient funds in Current Account!");
        }
    }
}

class FixedTermAccount implements DepositOnlyAccount {
    private double balance;

    public FixedTermAccount() {
        balance = 0;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposited: " + amount + " in Fixed Term Account. New Balance: " + balance);
    }
}


class BankClient {
    private List<DepositOnlyAccount> despositOnlyAccounts;
    private List<WithdrawableAccount> withdrawableAccounts;

    public BankClient(List<DepositOnlyAccount> despositOnlyAccounts,List<WithdrawableAccount> withdrawableAccounts ) {
        this.despositOnlyAccounts = despositOnlyAccounts;
        this.withdrawableAccounts = withdrawableAccounts;
    }

    public void performTransactions() {
        for (DepositOnlyAccount account : despositOnlyAccounts) {
            account.deposit(4000);
        }

        for (WithdrawableAccount account : withdrawableAccounts) {
            account.deposit(4000);
            account.withdraw(500);
        }
    }
}


public class LSP_Followed {
    public static void main(String[] args) {
        List<DepositOnlyAccount> depositOnlyAccounts = new ArrayList<>();
        List<WithdrawableAccount> withdrawableAccounts = new ArrayList<>();

        withdrawableAccounts.add(new CurrentAccount());
        withdrawableAccounts.add(new SavingsAccount());

        depositOnlyAccounts.add(new FixedTermAccount());

        BankClient client = new BankClient(depositOnlyAccounts, withdrawableAccounts);

        client.performTransactions();
        
    }
}
