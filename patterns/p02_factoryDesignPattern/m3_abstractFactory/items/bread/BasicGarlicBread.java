package patterns.p2_factoryDesignPattern.m3_abstractFactory.items.bread;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;

public class BasicGarlicBread implements GarlicBread {
    @Override
    public void prepare() {
        System.out.println("Preparing Basic Garlic Bread with butter and garlic!");
    }
}
