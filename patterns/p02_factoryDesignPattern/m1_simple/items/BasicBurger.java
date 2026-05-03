package patterns.p2_factoryDesignPattern.m1_simple.items;

import patterns.p2_factoryDesignPattern.m1_simple.interfaces.Burger;

public class BasicBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Burger with bun, patty, and ketchup!");
    }
}
