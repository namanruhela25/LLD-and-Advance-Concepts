package patterns.p5_decoratorPattern.decorators;

import patterns.p5_decoratorPattern.decorators.baseDecorator.Decorator;
import patterns.p5_decoratorPattern.interfaces.Character;

public class HeightUp extends Decorator {
    public HeightUp(Character c) {
        super(c);
    }

    @Override
    public String getAbilities() {
        return character.getAbilities() + " + Height Up";
    }
}
