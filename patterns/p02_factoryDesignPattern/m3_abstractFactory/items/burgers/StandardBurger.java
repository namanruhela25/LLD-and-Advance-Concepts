package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.burgers;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;

public class StandardBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Standard Burger with bun, patty, cheese, and lettuce!");
    }
}
