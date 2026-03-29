package patterns.p1_strategyDesignPattern;

import patterns.p1_strategyDesignPattern.paymentmethods.PayCreditCard;
import patterns.p1_strategyDesignPattern.paymentmethods.PayDebitCard;
import patterns.p1_strategyDesignPattern.paymentmethods.PayUPI;
import patterns.p1_strategyDesignPattern.paymentmethods.PayWallet;

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
