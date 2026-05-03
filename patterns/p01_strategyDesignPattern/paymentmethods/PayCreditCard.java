package patterns.p1_strategyDesignPattern.paymentmethods;
import patterns.p1_strategyDesignPattern.interfaces.PaymentMethod;

public class PayCreditCard implements PaymentMethod {
    @Override
    public void pay() {
        System.out.println("Payment done using Credit Card");
    }
}
