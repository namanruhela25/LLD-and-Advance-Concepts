package patterns.p1_strategyDesignPattern.paymentmethods;

import patterns.p1_strategyDesignPattern.interfaces.PaymentMethod;

public class PayWallet implements PaymentMethod {
    public void pay() {
        System.out.println("Payment done via Wallet");
    }
}
