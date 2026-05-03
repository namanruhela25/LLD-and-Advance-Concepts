package patterns.p2_factoryDesignPattern.m1_simple.factory;

import patterns.p2_factoryDesignPattern.m1_simple.interfaces.Burger;
import patterns.p2_factoryDesignPattern.m1_simple.items.BasicBurger;
import patterns.p2_factoryDesignPattern.m1_simple.items.PremiumBurger;
import patterns.p2_factoryDesignPattern.m1_simple.items.StandardBurger;


public class BurgerFactory {
    public Burger createBurger(String type) {
        switch (type.toLowerCase()) {
            case "basic":
                return new BasicBurger();
            case "standard":
                return new StandardBurger();
            case "premium":
                return new PremiumBurger();
            default:
                throw new IllegalArgumentException("Unknown burger type: " + type);
        }
    }
}
