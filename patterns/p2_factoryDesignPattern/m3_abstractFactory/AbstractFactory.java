package patterns.p2_factoryDesignPattern.m3_abstractFactory;

import patterns.p2_factoryDesignPattern.m3_abstractFactory.concretefactory.KingBurger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.concretefactory.SinghBurger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.factory.MealFactory;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.Burger;
import patterns.p2_factoryDesignPattern.m3_abstractFactory.interfaces.GarlicBread;

public class AbstractFactory {
    public static void main(String[] args) {

        String burgerType = "premium";
        String garlicBreadType = "basicwheat";


        MealFactory kingFactory = new KingBurger();
        MealFactory singhFactory = new SinghBurger();

        System.out.println("Ordering from King Burger:");

        Burger burger = kingFactory.createBurger(burgerType);
        
        if (burger!=null) {
            burger.prepare();
        }

        System.out.println("Ordering from Singh Burger: garlic bread");

        GarlicBread bread = singhFactory.createGarlicBread(garlicBreadType);

        if(bread!=null) {
            bread.prepare();
        }

    }
}
