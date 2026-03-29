package patterns.p2_factoryDesignPattern.m3_abstractFactory.concretefactory;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.factory.MealFactory;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread.BasicWheatGarlicBread;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread.CheeseWheatGarlicBread;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers.BasicWheatBurger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers.PremiumWheatBurger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers.StandardWheatBurger;

public class SinghBurger implements MealFactory {
    @Override
        public Burger createBurger(String type) {
        switch (type.toLowerCase()) {
            case "basicwheat":
                return new BasicWheatBurger();
            case "standardwheat":
                return new StandardWheatBurger();
            case "premiumwheat":
                return new PremiumWheatBurger();
            default:
                throw new IllegalArgumentException("Unknown burger type: " + type);
        }
    }

    @Override
        public GarlicBread createGarlicBread(String type) {
        switch (type.toLowerCase()) {
            case "basicwheat":
                return new BasicWheatGarlicBread();
            case "cheesewheat":
                return new CheeseWheatGarlicBread();
            default:
                throw new IllegalArgumentException("Unknown garlic bread type: " + type);
        }
    }
}
