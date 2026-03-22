package solid.liskov_substitution_principle.LSP_rules.MethodRules;


class Car {
    protected int speed;

    public Car() {
        speed = 0;
    }

    public void accelerate() {
        System.out.println("Accelerating");
        speed += 20;
    }

    // PostCondition: Speed must reduce after brake
    public void brake() {
        System.out.println("Applying brakes");
        speed -= 20;
    }
}

// Subclass can strengthen postcondition - Does not violate LSP
class HybridCar extends Car {
    private int charge;

    public HybridCar() {
        super();
        charge = 0;
    }

    // PostCondition: Speed must reduce after brake
    // PostCondition: Charge must increase.
    @Override
    public void brake() {
        System.out.println("Applying brakes");
        speed -= 20;
        charge += 10;
    }

    public void chargeBattery() {
        System.out.println("Charging battery");
        charge += 20;
    }

    // Getter method to retrieve the current charge
    public int getCharge() {
        return charge;
    }
}

public class PostCondition {
    public static void main(String[] args) {
        Car hybridCar = new HybridCar();
        hybridCar.accelerate();
        System.out.println("Current speed: " + hybridCar.speed);

        System.out.println("Current charge: " + ((HybridCar) hybridCar).getCharge());
        
        hybridCar.brake();
        System.out.println("Current speed: " + hybridCar.speed);

        System.out.println("Current charge: " + ((HybridCar) hybridCar).getCharge());
    }
}
