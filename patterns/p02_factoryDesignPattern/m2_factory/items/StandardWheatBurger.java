package patterns.p2_factoryDesignPattern.m2_factory.items;

import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public class StandardWheatBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Wheat Burger with bun, patty, cheese, and lettuce!");
    }
}
