# Abstraction in Java

This package demonstrates the concept of **Abstraction** in Java using both an **interface** and an **abstract class**, the two primary ways to achieve abstraction in Java.

## What is Abstraction?

Abstraction is the process of hiding the implementation details and showing only the essential features of an object to the user. It helps in reducing complexity and allows the user to focus on what an object does rather than how it does it.

In Java, abstraction can be achieved through:
1. **Abstract Classes**: Classes that cannot be instantiated and may contain abstract methods (methods without implementation)
2. **Interfaces**: Similar to abstract classes but can only contain abstract methods and constants


## Implementation Overview

### 1. Interface: `Vehicle`
- Declared as `interface Vehicle`
- Contains abstract methods `start()`, `stop()`, and `fuelUp()` that must be implemented by any class that implements the interface

#### Concrete Classes
- **`Car`**: Implements `Vehicle` and provides specific implementation for car operations
- **`Bike`**: Implements `Vehicle` and provides specific implementation for bike operations

### 2. Abstract Class: `Machine`
- Declared as `abstract class Machine`
- Contains abstract methods `powerOn()` and `powerOff()` that must be implemented by subclasses
- Contains a concrete method `service()` that provides common functionality

#### Concrete Class
- **`WashingMachine`**: Extends `Machine` and provides specific implementation for machine operations

### Main Class: `Abstraction`
- Demonstrates abstraction by creating objects of type `Vehicle` (interface) and `Machine` (abstract class)
- Calls methods without knowing the internal implementation details
- Shows polymorphism - same method calls behave differently for different objects

## Key Features Demonstrated

1. **Interface Methods**: All methods in the interface are abstract by default and must be implemented by concrete classes
2. **Abstract Class Methods**: Abstract methods in the abstract class must be implemented by subclasses, while concrete methods provide shared functionality
3. **Method Overriding**: Concrete classes provide their own implementation of interface or abstract class methods
4. **Polymorphism**: Reference of type `Vehicle` or `Machine` can hold objects of their respective concrete classes
5. **Code Reusability**: Common method signatures or functionality are shared across subclasses

## How to Run

Compile and run the `Abstraction.java` file:

```bash
javac Abstraction.java
java Abstraction
```

## Expected Output

```
Demonstrating Abstraction:
Car operations:
Car engine is starting with a key turn...
Car is being fueled up...
Car is braking and stopping...

Bike operations:
Bike engine is starting with a kick or button...
Bike is being fueled up...
Bike is applying brakes and stopping...

---

Demonstrating Abstraction with Abstract Class:
Washing Machine is powering on...
Machine is being serviced...
Washing Machine is powering off...
```

This implementation shows how abstraction allows us to work with different types of objects through both interfaces and abstract classes, hiding the specific implementation details of each type.