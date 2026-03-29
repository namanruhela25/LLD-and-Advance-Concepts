package patterns.p2_factoryDesignPattern.m2_factory.concretefactory;

import patterns.p2_factoryDesignPattern.m2_factory.factory.BurgerFactory;
import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;
import patterns.p2_factoryDesignPattern.m2_factory.items.BasicWheatBurger;
import patterns.p2_factoryDesignPattern.m2_factory.items.PremiumWheatBurger;
import patterns.p2_factoryDesignPattern.m2_factory.items.StandardWheatBurger;

public class SinghBurger implements BurgerFactory {
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

}
