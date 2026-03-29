package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;

public class BasicWheatGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Wheat Garlic Bread with butter and garlic!");
    }
}
