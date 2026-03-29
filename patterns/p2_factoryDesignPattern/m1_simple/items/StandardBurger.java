package patterns.p2_factoryDesignPattern.m1_simple.items;

import patterns.p2_factoryDesignPattern.m1_simple.interfaces.Burger;

public class StandardBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Burger with bun, patty, and mustard!");
    }
}
