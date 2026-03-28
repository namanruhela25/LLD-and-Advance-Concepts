package patterns.strategyDesignPattern.paymentmethods;

import patterns.strategyDesignPattern.interfaces.PaymentMethod;

public class PayWallet implements PaymentMethod {
    public void pay() {
        System.out.println("Payment done via Wallet");
    }
}
