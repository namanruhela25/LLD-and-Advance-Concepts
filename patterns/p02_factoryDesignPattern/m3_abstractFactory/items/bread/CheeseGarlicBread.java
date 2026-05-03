package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;

public class CheeseGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("Preparing Cheese Garlic Bread with extra cheese and butter!");
    }
}
