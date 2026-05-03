package patterns.p2_factoryDesignPattern.m2_factory.items;

import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public class PremiumBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Premium Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}
