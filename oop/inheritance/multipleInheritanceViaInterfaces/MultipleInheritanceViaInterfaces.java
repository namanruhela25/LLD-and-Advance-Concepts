package oop.inheritance.multipleInheritanceViaInterfaces;

// First interface
interface Swimmer {
    void swim();
}

// Second interface
interface Flyer {
    void fly();
    // Note: In Java, Two Interface can have same method name but they can have different implementation in the classes that implement these interfaces. This is one of the advantages of using interfaces for multiple inheritance.
    void swim();
}

// Class implementing multiple interfaces (Multiple Inheritance via Interfaces)
class Duck implements Swimmer, Flyer {
    private String name;
    private int age;

    public Duck(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public void swim() {
        System.out.println(name + " is swimming in the water...");
    }

    @Override
    public void fly() {
        System.out.println(name + " is flying in the sky...");
    }

    public void eat() {
        System.out.println(name + " is eating...");
    }

    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

// Another interface
interface Runner {
    void run();
}

// Another class implementing multiple interfaces
class Dog implements Runner, Swimmer {
    private String name;
    private String breed;

    public Dog(String name, String breed) {
        this.name = name;
        this.breed = breed;
    }

    @Override
    public void run() {
        System.out.println(name + " is running fast...");
    }

    @Override
    public void swim() {
        System.out.println(name + " is swimming (dogs can swim)...");
    }

    public void bark() {
        System.out.println(name + " is barking: Woof! Woof!");
    }

    public void displayInfo() {
        System.out.println("Name: " + name + ", Breed: " + breed);
    }
}

// Main class
public class MultipleInheritanceViaInterfaces {
    public static void main(String[] args) {
        System.out.println("--- Multiple Inheritance via Interfaces Example ---\n");

        Duck myDuck = new Duck("Donald", 5);
        System.out.println("Duck Information:");
        myDuck.displayInfo();
        myDuck.swim();
        myDuck.fly();
        myDuck.eat();

        System.out.println("\n");

        Dog myDog = new Dog("Max", "Golden Retriever");
        System.out.println("Dog Information:");
        myDog.displayInfo();
        myDog.run();
        myDog.swim();
        myDog.bark();
    }
}
