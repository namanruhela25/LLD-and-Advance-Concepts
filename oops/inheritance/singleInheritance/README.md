# Single Inheritance in Java

This folder demonstrates **Single Inheritance** in Java, where a class inherits from only one parent class.

## What is Single Inheritance?

Single inheritance is the simplest form of inheritance where a child class extends only one parent class. The child class inherits all the properties and methods of the parent class and can also have its own properties and methods.

## Implementation Overview

### Parent Class: `Animal`
- **Fields**: `name`, `age`
- **Methods**:
  - `eat()`: Displays eating behavior
  - `sleep()`: Displays sleeping behavior
  - `displayInfo()`: Displays animal information

### Child Class: `Dog` (extends Animal)
- **Additional Field**: `breed`
- **Constructor**: Calls parent constructor with `super()`
- **Methods**:
  - `bark()`: Dog-specific behavior
  - `eat()`: Overridden method with dog-specific implementation
  - `displayInfo()`: Overridden method that calls parent method and adds breed info

## Key Features Demonstrated

1. **Inheritance**: `Dog` class inherits from `Animal` class using `extends` keyword
2. **Constructor Chaining**: Child class calls parent constructor using `super()`
3. **Method Overriding**: `eat()` and `displayInfo()` methods are overridden in the child class
4. **Access to Parent Methods**: Child class can call parent methods using `super` keyword
5. **Protected Access**: Parent class uses `protected` modifier for fields, allowing access in child class

## How to Run

```bash
cd single_inheritance
javac SingleInheritance.java
java oops.inheritance.singleInheritance.SingleInheritance
```

## Expected Output

```
--- Single Inheritance Example ---
Name: Buddy, Age: 5
Breed: Golden Retriever

Buddy is eating dog food...
Buddy is barking: Woof! Woof!
Buddy is sleeping...
```

This implementation demonstrates how single inheritance allows a child class to inherit and extend the functionality of a parent class.
