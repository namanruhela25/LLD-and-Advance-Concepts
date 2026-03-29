package patterns.p2_factoryDesignPattern.m2_factory.items;

import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public class StandardBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Burger with bun, patty, cheese, and lettuce!");
    }
}
