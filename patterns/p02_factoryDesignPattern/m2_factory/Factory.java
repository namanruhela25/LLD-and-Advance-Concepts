package patterns.p2_factoryDesignPattern.m2_factory;

import patterns.p2_factoryDesignPattern.m2_factory.concretefactory.KingBurger;
import patterns.p2_factoryDesignPattern.m2_factory.concretefactory.SinghBurger;
import patterns.p2_factoryDesignPattern.m2_factory.factory.BurgerFactory;
import patterns.p2_factoryDesignPattern.m2_factory.interfaces.Burger;

public class Factory {
    public static void main(String[] args) {
        String burgerType = "premium";
        String wheatBurgerType = "standardwheat";

        BurgerFactory kingFactory = new KingBurger();
        BurgerFactory singhFactory = new SinghBurger();

        System.out.println("Ordering from King Burger:");

        Burger burger = kingFactory.createBurger(burgerType);

        burger.prepare();

        System.out.println("Ordering from Singh Burger:");

        // Direct method call without storing in extra variables
        singhFactory.createBurger(wheatBurgerType).prepare();

    }
}
