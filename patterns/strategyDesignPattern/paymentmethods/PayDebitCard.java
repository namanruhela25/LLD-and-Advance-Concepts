package patterns.strategyDesignPattern.paymentmethods;

import patterns.strategyDesignPattern.interfaces.PaymentMethod;

public class PayDebitCard implements PaymentMethod {
    @Override
    public void pay() {
        System.out.println("Paying via debit card");
    }
}
