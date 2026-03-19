package oop.encapsulation;

class SportsCar {
    private String brand;
    private String model;
    private boolean isEngineOn = false;
    private int currentSpeed = 0;
    private int currentGear = 0;
        
    //Introduce new variable to exaplain setters
    private String tyreCompany;

    public SportsCar(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public String getTyreCompany() {
        return tyreCompany;
    }

    public void setTyreCompany(String tyreCompany) {
        this.tyreCompany = tyreCompany;
    }

    public void startEngine() {
        isEngineOn = true;
        System.out.println(brand + " " + model + " : Engine starts with a roar!");
    }

    public void shiftGear(int gear) {
        this.currentGear = gear;
        System.out.println(brand + " " + model + " : Shifted to gear " + currentGear);
    }

    public void accelerate() {
        if (!isEngineOn) {
            System.out.println(brand + " " + model + " : Engine is off! Cannot accelerate.");
            return;
        }
        currentSpeed += 20;
        System.out.println(brand + " " + model + " : Accelerating to " + currentSpeed + " km/h");
    }
    
    public void brake() {
        currentSpeed -= 20;
        if (currentSpeed < 0) currentSpeed = 0;
        System.out.println(brand + " " + model + " : Braking! Speed is now " + currentSpeed + " km/h");
    }

    public void stopEngine() {
        isEngineOn = false;
        currentGear = 0;
        currentSpeed = 0;
        System.out.println(brand + " " + model + " : Engine turned off.");
    }
}

//Main Method
public class Encapsulation {
    public static void main(String[] args) {

        SportsCar mySportsCar = new SportsCar("Ford", "Mustang");

        mySportsCar.startEngine();
        mySportsCar.shiftGear(1);
        mySportsCar.accelerate();
        mySportsCar.shiftGear(2);
        mySportsCar.accelerate();
        mySportsCar.brake();
        mySportsCar.stopEngine();

        //Setting arbitrary value to speed.
        //mySportsCar.currentSpeed = 500;

       // System.out.println("Current Speed of My Sports Car is set to " + mySportsCar.currentSpeed);

       System.out.println("Current Speed of My Sports Car is " + mySportsCar.getSpeed());
    }
} 