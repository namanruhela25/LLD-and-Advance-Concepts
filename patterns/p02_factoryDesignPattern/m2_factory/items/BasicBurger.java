package patterns.p2_factoryDesignPattern.m2_factory.items;

import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public class BasicBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Burger with bun, patty, and ketchup!");
    }
}
