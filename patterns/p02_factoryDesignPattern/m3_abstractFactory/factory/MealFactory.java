package patterns.p2_factoryDesignPattern.m3_abstractFactory.factory;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;

public interface MealFactory {
    Burger createBurger(String type);
    GarlicBread createGarlicBread(String type);
}
