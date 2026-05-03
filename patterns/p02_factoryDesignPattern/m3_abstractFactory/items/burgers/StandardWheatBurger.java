package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;

public class StandardWheatBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Wheat Burger with bun, patty, cheese, and lettuce!");
    }
}
