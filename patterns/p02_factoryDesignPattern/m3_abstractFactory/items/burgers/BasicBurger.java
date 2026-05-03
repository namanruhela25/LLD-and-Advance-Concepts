package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;

public class BasicBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Burger with bun, patty, and ketchup!");
    }
}
