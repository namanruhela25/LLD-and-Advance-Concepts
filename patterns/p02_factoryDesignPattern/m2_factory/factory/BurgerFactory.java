package patterns.p2_factoryDesignPattern.m2_factory.factory;

import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public interface BurgerFactory {
    Burger createBurger(String type);
}
