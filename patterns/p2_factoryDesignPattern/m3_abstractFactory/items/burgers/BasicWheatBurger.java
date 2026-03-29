package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;

public class BasicWheatBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Wheat Burger with bun, patty, and ketchup!");
    }
}
