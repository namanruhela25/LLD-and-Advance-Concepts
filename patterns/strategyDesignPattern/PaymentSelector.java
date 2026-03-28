package patterns.strategyDesignPattern;

import patterns.strategyDesignPattern.interfaces.PaymentMethod;

public class PaymentSelector {
    PaymentMethod payCreditCard;
    PaymentMethod payDebitCard;
    PaymentMethod payUPI;
    PaymentMethod payWallet;
    
    public PaymentSelector(PaymentMethod payCreditCard, PaymentMethod payDebitCard, PaymentMethod payUPI, PaymentMethod payWallet) {
        this.payCreditCard = payCreditCard;
        this.payDebitCard = payDebitCard;
        this.payUPI = payUPI;
        this.payWallet = payWallet;
    }

    public void payNow(String method) {
        if (method.equals("CreditCard")) {
            payCreditCard.pay();
        } else if (method.equals("DebitCard")) {
            payDebitCard.pay();
        } else if (method.equals("UPI")) {
            payUPI.pay();
        } else if (method.equals("Wallet")) {
            payWallet.pay();
        }
    }
}
