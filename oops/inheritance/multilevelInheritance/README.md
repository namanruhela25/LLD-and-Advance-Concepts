# Multilevel Inheritance in Java

This folder demonstrates **Multilevel Inheritance** in Java, where a class inherits from a parent class which itself inherits from another parent class (forming a chain).

## What is Multilevel Inheritance?

Multilevel inheritance is a form of inheritance where there is a chain of inheritance. A class inherits from another class, which in turn inherits from another class. This creates a hierarchy of classes.

In Java, there is no limit to the depth of the inheritance chain.

## Implementation Overview

### Grandparent Class: `Animal`
- **Fields**: `name`, `age`
- **Methods**:
  - `eat()`: Displays eating behavior
  - `sleep()`: Displays sleeping behavior
  - `displayInfo()`: Displays animal information

### Parent Class: `Mammal` (extends Animal)
- **Inherits**: All methods and fields from `Animal`
- **Methods**:
  - `giveBirth()`: Mammal-specific behavior

### Child Class: `Dog` (extends Mammal)
- **Inherits**: All methods and fields from `Mammal` and `Animal`
- **Additional Field**: `breed`
- **Methods**:
  - `bark()`: Dog-specific behavior
  - `eat()`: Overridden method
  - `displayInfo()`: Overridden method

## Key Features Demonstrated

1. **Multilevel Chain**: `Dog` -> `Mammal` -> `Animal` (inheritance chain)
2. **Constructor Chaining**: Constructor calls chain up through `super()`
3. **Method Inheritance**: `Dog` inherits methods from both `Mammal` and `Animal`
4. **Method Overriding**: Methods can be overridden at any level in the chain
5. **Access to All Ancestors**: Child class can access methods from all ancestors in the chain

## How to Run

```bash
cd multilevel_inheritance
javac MultilevelInheritance.java
java oops.inheritance.multilevelInheritance.MultilevelInheritance
```

## Expected Output

```
--- Multilevel Inheritance Example ---
Name: Max, Age: 3
Breed: Labrador

Max is eating dog food...
Max is a mammal and gives birth to live young...
Max is barking: Woof! Woof!
Max is sleeping...
```

This implementation demonstrates how multilevel inheritance creates a hierarchy of classes where a child class can inherit from an intermediate class that itself inherits from another class.
