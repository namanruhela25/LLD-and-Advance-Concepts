package patterns.p1_strategyDesignPattern.paymentmethods;

import patterns.p1_strategyDesignPattern.interfaces.PaymentMethod;

public class PayDebitCard implements PaymentMethod {
    @Override
    public void pay() {
        System.out.println("Paying via debit card");
    }
}
