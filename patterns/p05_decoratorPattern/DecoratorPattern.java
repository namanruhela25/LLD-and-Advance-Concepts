package patterns.p5_decoratorPattern;

import patterns.p5_decoratorPattern.characters.Mario;
import patterns.p5_decoratorPattern.decorators.FlyUp;
import patterns.p5_decoratorPattern.decorators.HeightUp;
import patterns.p5_decoratorPattern.interfaces.Character;


public class DecoratorPattern {
    public static void main(String[] args) {
        Character mario = new Mario();

        System.out.println("Power : " + mario.getAbilities());
        
        mario = new HeightUp(mario);

        System.out.println("Mario with Height Up : " + mario.getAbilities());

        mario = new FlyUp(new HeightUp(mario));

        System.out.println("Mario with Height Up and Fly Up : " + mario.getAbilities());

        System.out.println("--- Creating new Mario with only Fly Up : ----");

        Character mario2 = new Mario();

        mario2 = new FlyUp(mario2);

        System.out.println("Mario with Fly Up : " + mario2.getAbilities());

    }
}
