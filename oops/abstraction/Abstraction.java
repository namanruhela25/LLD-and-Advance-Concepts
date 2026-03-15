package oops.abstraction;

// Abstract class demonstrating Abstraction
abstract class Machine {
    abstract void powerOn();
    abstract void powerOff();
    void service() {
        System.out.println("Machine is being serviced...");
    }
}

// Concrete class implementing abstract class
class WashingMachine extends Machine {
    @Override
    void powerOn() {
        System.out.println("Washing Machine is powering on...");
    }
    @Override
    void powerOff() {
        System.out.println("Washing Machine is powering off...");
    }
}

// Interface demonstrating Abstraction
interface Vehicle {
    // Abstract methods - implementation hidden from user
    void start();
    void stop();
    void fuelUp();
}


// Concrete class Car implementing Vehicle interface
class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("Car engine is starting with a key turn...");
    }

    @Override
    public void stop() {
        System.out.println("Car is braking and stopping...");
    }

    @Override
    public void fuelUp() {
        System.out.println("Car is being fueled up...");
    }
}


// Concrete class Bike implementing Vehicle interface
class Bike implements Vehicle {
    @Override
    public void start() {
        System.out.println("Bike engine is starting with a kick or button...");
    }

    @Override
    public void stop() {
        System.out.println("Bike is applying brakes and stopping...");
    }

    @Override
    public void fuelUp() {
        System.out.println("Bike is being fueled up...");
    }
}

public class Abstraction {
    public static void main(String[] args) {
        // Using abstraction - we don't know the internal implementation
        Vehicle myCar = new Car();
        Vehicle myBike = new Bike();

        System.out.println("Demonstrating Abstraction:");
        System.out.println("Car operations:");
        myCar.start();
        myCar.fuelUp();
        myCar.stop();

        System.out.println("\nBike operations:");
        myBike.start();
        myBike.fuelUp();
        myBike.stop();

        // Demonstrating abstraction using abstract class
        System.out.println("\n---\n");
        System.out.println("Demonstrating Abstraction with Abstract Class:");
        Machine wm = new WashingMachine();
        wm.powerOn();
        wm.service();
        wm.powerOff();
    }
}
