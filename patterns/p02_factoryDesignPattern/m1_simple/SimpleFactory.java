package patterns.p2_factoryDesignPattern.m1_simple;

import patterns.p2_factoryDesignPattern.m1_simple.factory.BurgerFactory;
import patterns.p2_factoryDesignPattern.m1_simple.interfaces.Burger;

public class SimpleFactory {
    public static void main(String[] args) {
        BurgerFactory basicBurger = new BurgerFactory();
        Burger burger = basicBurger.createBurger("premium");

        burger.prepare();

    }
}
