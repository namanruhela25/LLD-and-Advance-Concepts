package patterns.strategyDesignPattern;

import patterns.strategyDesignPattern.paymentmethods.PayCreditCard;
import patterns.strategyDesignPattern.paymentmethods.PayDebitCard;
import patterns.strategyDesignPattern.paymentmethods.PayUPI;
import patterns.strategyDesignPattern.paymentmethods.PayWallet;

public class Strategy {
    public static void main(String[] args) {
        PaymentSelector selector = new PaymentSelector(new PayCreditCard(), new PayDebitCard(), new PayUPI(), new PayWallet());
        selector.payNow("UPI");
        selector.payNow("DebitCard");
        selector.payNow("DebitCard");
        selector.payNow("CreditCard");
        selector.payNow("Wallet");
        selector.payNow("DebitCard");
        selector.payNow("Wallet");
        selector.payNow("CreditCard");
        selector.payNow("CreditCard");
        selector.payNow("UPI");
        selector.payNow("UPI");
        selector.payNow("Wallet");
        selector.payNow("UPI");
        selector.payNow("UPI");
    }
}
