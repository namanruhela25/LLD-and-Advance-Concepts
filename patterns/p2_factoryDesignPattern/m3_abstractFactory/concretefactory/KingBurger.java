package patterns.p2_factoryDesignPattern.m3_abstractFactory.concretefactory;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.factory.MealFactory;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread.BasicGarlicBread;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers.BasicBurger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers.PremiumBurger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers.StandardBurger;

public class KingBurger implements MealFactory {
    @Override
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

    @Override
    public GarlicBread createGarlicBread(String type) {
        switch (type.toLowerCase()) {
            case "basic":
                return new BasicGarlicBread();
            default:
                throw new IllegalArgumentException("Unknown garlic bread type: " + type);
        }
    }
}
