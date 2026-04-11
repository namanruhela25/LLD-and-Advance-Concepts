package patterns.p5_decoratorPattern.decorators;

import patterns.p5_decoratorPattern.decorators.baseDecorator.Decorator;
import patterns.p5_decoratorPattern.interfaces.Character;

public class StarPower extends Decorator {
    public StarPower(Character c) {
        super(c);
    }

    @Override
    public String getAbilities() {
        return character.getAbilities() + " + Star Power";
    }
    
    
}
