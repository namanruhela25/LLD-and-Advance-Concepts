package patterns.p5_decoratorPattern.decorators.baseDecorator;

import patterns.p5_decoratorPattern.interfaces.Character;

public abstract class Decorator implements Character {
    protected Character character;

    public Decorator(Character character) {
        this.character = character;
    }
}
