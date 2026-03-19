package oop.inheritance.hierarchicalInheritance;

// Parent class (Superclass)
class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " is eating...");
    }

    public void sleep() {
        System.out.println(name + " is sleeping...");
    }

    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

// First child class - inherits from Animal
class Dog extends Animal {
    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    public void bark() {
        System.out.println(name + " is barking: Woof! Woof!");
    }

    @Override
    public void eat() {
        System.out.println(name + " is eating dog food...");
    }
}

// Second child class - inherits from Animal
class Cat extends Animal {
    private String color;

    public Cat(String name, int age, String color) {
        super(name, age);
        this.color = color;
    }

    public void meow() {
        System.out.println(name + " is meowing: Meow! Meow!");
    }

    @Override
    public void eat() {
        System.out.println(name + " is eating cat food...");
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Color: " + color);
    }
}

// Third child class - inherits from Animal
class Bird extends Animal {
    private String wingSpan;

    public Bird(String name, int age, String wingSpan) {
        super(name, age);
        this.wingSpan = wingSpan;
    }

    public void fly() {
        System.out.println(name + " is flying with wing span: " + wingSpan);
    }

    @Override
    public void eat() {
        System.out.println(name + " is eating seeds and insects...");
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Wing Span: " + wingSpan);
    }
}

// Main class
public class HierarchicalInheritance {
    public static void main(String[] args) {
        System.out.println("--- Hierarchical Inheritance Example ---\n");

        Dog myDog = new Dog("Buddy", 5, "Golden Retriever");
        System.out.println("Dog Information:");
        myDog.displayInfo();
        myDog.eat();
        myDog.bark();
        myDog.sleep();

        System.out.println("\n");

        Cat myCat = new Cat("Whiskers", 3, "Orange");
        System.out.println("Cat Information:");
        myCat.displayInfo();
        myCat.eat();
        myCat.meow();
        myCat.sleep();

        System.out.println("\n");

        Bird myBird = new Bird("Tweety", 2, "1.5 feet");
        System.out.println("Bird Information:");
        myBird.displayInfo();
        myBird.eat();
        myBird.fly();
        myBird.sleep();
    }
}
