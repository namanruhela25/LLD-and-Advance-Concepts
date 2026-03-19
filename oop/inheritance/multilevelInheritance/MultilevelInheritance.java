package oop.inheritance.multilevelInheritance;

// Grandparent class (Superclass)
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

// Parent class (Intermediate class) - inherits from Animal
class Mammal extends Animal {
    public Mammal(String name, int age) {
        super(name, age);
    }

    public void giveBirth() {
        System.out.println(name + " is a mammal and gives birth to live young...");
    }
}

// Child class - inherits from Mammal (Multilevel Inheritance)
class Dog extends Mammal {
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

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Breed: " + breed);
    }
}

// Main class
public class MultilevelInheritance {
    public static void main(String[] args) {
        Dog myDog = new Dog("Max", 3, "Labrador");

        System.out.println("--- Multilevel Inheritance Example ---");
        myDog.displayInfo();
        System.out.println();
        myDog.eat();
        myDog.giveBirth();
        myDog.bark();
        myDog.sleep();
    }
}
