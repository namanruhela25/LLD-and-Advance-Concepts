# Encapsulation in Java - SportsCar Example

This package demonstrates the concept of **Encapsulation** in Java using a `SportsCar` class, one of the four fundamental principles of Object-Oriented Programming (OOP).

## What is Encapsulation?

Encapsulation is the process of wrapping data (variables) and code (methods) together as a single unit. It restricts direct access to some of an object's components, which is a means of preventing unintended interference and misuse of the data.

In Java, encapsulation is achieved by:
- Declaring the fields of a class as `private` (data hiding)
- Providing public methods to access and update the values of the fields (controlled access)
- Implementing business logic within methods to ensure data integrity

## Implementation Overview

### Class: `SportsCar`
- **Private Fields**:
  - `brand`: The car's brand (e.g., "Ford")
  - `model`: The car's model (e.g., "Mustang")
  - `isEngineOn`: Boolean flag indicating if the engine is running
  - `currentSpeed`: Current speed of the car (in km/h)
  - `currentGear`: Current gear of the car
  - `tyreCompany`: The company that manufactures the tyres

- **Constructor**: Initializes the `brand` and `model`

- **Getter Methods**:
  - `getSpeed()`: Returns the current speed
  - `getTyreCompany()`: Returns the tyre company

- **Setter Methods**:
  - `setTyreCompany(String tyreCompany)`: Sets the tyre company

- **Behavioral Methods**:
  - `startEngine()`: Starts the engine and sets `isEngineOn` to true
  - `shiftGear(int gear)`: Changes the gear and updates `currentGear`
  - `accelerate()`: Increases speed by 20 km/h, but only if the engine is on
  - `brake()`: Decreases speed by 20 km/h, ensuring it doesn't go below 0
  - `stopEngine()`: Turns off the engine and resets gear and speed to 0

### Main Class: `Encapsulation`
- Creates a `SportsCar` object ("Ford Mustang")
- Demonstrates encapsulation by calling methods to control the car's state
- Shows that direct access to private fields is not allowed (commented-out line illustrates this)
- Uses getter method to safely retrieve the current speed

## Key Features Demonstrated

1. **Data Hiding**: All fields are private, preventing direct external access
2. **Controlled Access**: Public methods provide safe ways to interact with the object's state
3. **Data Integrity**: Methods like `accelerate()` and `brake()` include logic to maintain valid states
4. **Abstraction**: Users interact with the car through high-level methods without knowing internal details

## How to Run

Compile and run the `Encapsulation.java` file:

```bash
javac Encapsulation.java
java oops.encapsulation.Encapsulation
```

## Expected Output

```
Ford Mustang : Engine starts with a roar!
Ford Mustang : Shifted to gear 1
Ford Mustang : Accelerating to 20 km/h
Ford Mustang : Shifted to gear 2
Ford Mustang : Accelerating to 40 km/h
Ford Mustang : Braking! Speed is now 20 km/h
Ford Mustang : Engine turned off.
Current Speed of My Sports Car is 0
```

This implementation shows how encapsulation protects the internal state of objects and provides controlled, safe access to data and functionality.
