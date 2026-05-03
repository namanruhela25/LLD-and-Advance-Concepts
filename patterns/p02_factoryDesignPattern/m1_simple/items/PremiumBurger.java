package patterns.p2_factoryDesignPattern.m1_simple.items;

import patterns.p2_factoryDesignPattern.m1_simple.interfaces.Burger;

public class PremiumBurger implements Burger {
    @Override
    public void prepare() {
        System.out.println("Preparing Premium Burger with sesame bun, double patty, cheese, lettuce, tomato, and special sauce!");
    }
    
}
