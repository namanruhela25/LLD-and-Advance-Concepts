package patterns.p13_BridgePattern;

interface Engine {
    void start();
}

class PetrolEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Petrol Engine Starting..");
    }
}


class DieselEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Diesel Engine Starting..");
    }
}

class ElectricEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Electric Engine Starting..");
    }
}


abstract class Car {
    Engine engine;

    public Car(Engine e) {
        this.engine = e;
    }

    public abstract void drive();
}


class Sedan extends Car {
    
    public Sedan(Engine e) {
        super(e);
    }

    @Override
    public void drive() {
        engine.start();
        System.out.println("Driving Sedan car");
    }
    
}



class SUV extends Car {

    public SUV(Engine e) {
        super(e);
    }

    @Override
    public void drive() {
        engine.start();
        System.out.println("Driving SUV Car");
    }
    
}

public class BridegPattern {
    public static void main(String[] args) {
        Engine petrol = new PetrolEngine();
        Engine diesel = new DieselEngine();
        Engine electric = new ElectricEngine();

        Car sedan = new Sedan(electric);
        sedan.drive();

        Car suv = new SUV(petrol);
        suv.drive();


        Car suv1 = new SUV(diesel);
        suv1.drive();
    }    
}
