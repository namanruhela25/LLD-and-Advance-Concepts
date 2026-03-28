package patterns.strategyDesignPattern.paymentmethods;
import patterns.strategyDesignPattern.interfaces.PaymentMethod;

public class PayCreditCard implements PaymentMethod {
    @Override
    public void pay() {
        System.out.println("Payment done using Credit Card");
    }
}
