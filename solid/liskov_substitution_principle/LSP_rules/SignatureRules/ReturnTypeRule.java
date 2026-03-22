package solid.liskov_substitution_principle.LSP_rules.SignatureRules;

import java.lang.ref.Cleaner.Cleanable;

class Organism {

}

class Animal extends Organism {

}

class Dog extends Animal {

}

class Parent {
    public Organism getAnimal() {
        System.out.println("Parent class : Animal instance returned");
        return new Animal();
    }
}


class Child extends Parent {
    @Override
    public Dog getAnimal() {
        System.out.println("Child class : Dog instance returned");
        return new Dog();
    }
}

class Client {
    Parent p;

    Client (Parent p) {
        this.p = p;
    }

    public void takeAnimal() {
        p.getAnimal();
    }
}


public class ReturnTypeRule {
    public static void main(String[] args) {

        Parent parent = new Parent();
        Child child = new Child();

        Client client1 = new Client(parent);

        Client client2 = new Client(child);

        client1.takeAnimal();

        client2.takeAnimal();


    }
}
