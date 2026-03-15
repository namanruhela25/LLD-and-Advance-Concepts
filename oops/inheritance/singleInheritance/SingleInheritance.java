package oops.inheritance.singleInheritance;

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

// Child class (Subclass) - Single Inheritance
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

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Breed: " + breed);
    }
}

// Main class
public class SingleInheritance {
    public static void main(String[] args) {
        /*
            Note : If we create reference of Animal then we can only access the methods of Animal class but not the methods of Dog class.
            myDog.bark(); --> This will give compile-time error because bark() method is not defined in Animal class. To access the bark() method, we need to create reference of Dog class.
        */
        Dog myDog = new Dog("Buddy", 5, "Golden Retriever");

        System.out.println("--- Single Inheritance Example ---");
        myDog.displayInfo();
        System.out.println();
        myDog.eat();
        myDog.bark();
        myDog.sleep();
    }
}
