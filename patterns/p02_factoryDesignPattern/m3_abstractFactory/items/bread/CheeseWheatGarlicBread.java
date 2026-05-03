package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;

public class CheeseWheatGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("Preparing Cheese Wheat Garlic Bread with extra cheese and butter!");
    }
}
