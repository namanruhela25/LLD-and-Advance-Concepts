package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;

public class PremiumWheatBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Premium Wheat Burger with gourmet bun, premium patty, cheese, lettuce, and secret sauce!");
    }
}
