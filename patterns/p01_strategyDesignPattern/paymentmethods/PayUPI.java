package patterns.p1_strategyDesignPattern.paymentmethods;

import patterns.p1_strategyDesignPattern.interfaces.PaymentMethod;

public class PayUPI implements PaymentMethod {
    @Override
    public void pay() {
        System.out.println("Payment done via UPI");
    }
}
