package patterns.p2_factoryDesignPattern.m2_factory.items;

import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public class BasicWheatBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Wheat Burger with bun, patty, and ketchup!");
    }
}
