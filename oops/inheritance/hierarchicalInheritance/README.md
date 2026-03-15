# Hierarchical Inheritance in Java

This folder demonstrates **Hierarchical Inheritance** in Java, where multiple classes inherit from the same parent class.

## What is Hierarchical Inheritance?

Hierarchical inheritance is a form of inheritance where multiple child classes inherit from a single parent class. This creates a hierarchy where one parent serves as the base for multiple children.

## Implementation Overview

### Parent Class: `Animal`
- **Fields**: `name`, `age`
- **Methods**:
  - `eat()`: Displays eating behavior
  - `sleep()`: Displays sleeping behavior
  - `displayInfo()`: Displays animal information

### First Child Class: `Dog` (extends Animal)
- **Additional Field**: `breed`
- **Methods**:
  - `bark()`: Dog-specific behavior
  - `eat()`: Overridden method with dog-specific implementation

### Second Child Class: `Cat` (extends Animal)
- **Additional Field**: `color`
- **Methods**:
  - `meow()`: Cat-specific behavior
  - `eat()`: Overridden method with cat-specific implementation
  - `displayInfo()`: Overridden method

### Third Child Class: `Bird` (extends Animal)
- **Additional Field**: `wingSpan`
- **Methods**:
  - `fly()`: Bird-specific behavior
  - `eat()`: Overridden method with bird-specific implementation
  - `displayInfo()`: Overridden method

## Key Features Demonstrated

1. **Multiple Children from One Parent**: All three classes (`Dog`, `Cat`, `Bird`) inherit from the same `Animal` class
2. **Polymorphism**: Each child class overrides the `eat()` method differently
3. **Shared Behavior**: All child classes inherit `sleep()` and other base methods
4. **Unique Behaviors**: Each child class has its own unique methods (`bark()`, `meow()`, `fly()`)
5. **Method Overriding**: Child classes can override parent methods with their own implementation

## How to Run

```bash
cd hierarchical_inheritance
javac HierarchicalInheritance.java
java oops.inheritance.hierarchicalInheritance.HierarchicalInheritance
```

## Expected Output

```
--- Hierarchical Inheritance Example ---

Dog Information:
Name: Buddy, Age: 5
Buddy is eating dog food...
Buddy is barking: Woof! Woof!
Buddy is sleeping...


Cat Information:
Name: Whiskers, Age: 3
Color: Orange
Whiskers is eating cat food...
Whiskers is meowing: Meow! Meow!
Whiskers is sleeping...


Bird Information:
Name: Tweety, Age: 2
Wing Span: 1.5 feet
Tweety is eating seeds and insects...
Tweety is flying with wing span: 1.5 feet
Tweety is sleeping...
```

This implementation demonstrates how multiple child classes can inherit from a single parent class while maintaining their own unique characteristics and behaviors.
