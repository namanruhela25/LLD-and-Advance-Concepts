package patterns.p5_decoratorPattern.decorators;

import patterns.p5_decoratorPattern.decorators.baseDecorator.Decorator;
import patterns.p5_decoratorPattern.interfaces.Character;

public class FlyUp extends Decorator {

    public FlyUp(Character c) {
        super(c);
    }

    @Override
    public String getAbilities() {
        return character.getAbilities() + " + Fly Up";
    }
    
}
