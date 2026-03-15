# Inheritance in Java

This folder demonstrates all types of **Inheritance** supported in Java, one of the four fundamental principles of Object-Oriented Programming (OOP).

## What is Inheritance?

Inheritance is a mechanism where a new class is created based on an existing class. The new class (child/subclass) inherits properties and methods from the existing class (parent/superclass). Inheritance is used to achieve code reusability and establishes a relationship between classes.

## Types of Inheritance in Java

Java supports four types of inheritance:

### 1. **Single Inheritance**
A class inherits from only one parent class.

**Folder**: `single_inheritance/`
**Example**: `Dog` inherits from `Animal`

```
    Animal
      |
    Dog
```

[See Single Inheritance Example](single_inheritance/README.md)

---

### 2. **Multilevel Inheritance**
A class inherits from another class which itself inherits from another class, forming a chain.

**Folder**: `multilevel_inheritance/`
**Example**: `Dog` inherits from `Mammal` which inherits from `Animal`

```
    Animal
      |
   Mammal
      |
    Dog
```

[See Multilevel Inheritance Example](multilevel_inheritance/README.md)

---

### 3. **Hierarchical Inheritance**
Multiple classes inherit from the same parent class.

**Folder**: `hierarchical_inheritance/`
**Example**: `Dog`, `Cat`, and `Bird` all inherit from `Animal`

```
      Animal
      /  |  \
    Dog Cat Bird
```

[See Hierarchical Inheritance Example](hierarchical_inheritance/README.md)

---

### 4. **Multiple Inheritance via Interfaces**
Java doesn't support multiple inheritance with classes, but a class can implement multiple interfaces.

**Folder**: `multiple_inheritance_via_interfaces/`
**Example**: `Duck` implements `Swimmer` and `Flyer` interfaces

```
  Swimmer    Flyer
      \      /
      Duck
```

[See Multiple Inheritance via Interfaces Example](multiple_inheritance_via_interfaces/README.md)

---

## Key Concepts in Inheritance

1. **Parent Class (Superclass)**: The class being inherited from
2. **Child Class (Subclass)**: The class that inherits from the parent class
3. **extends Keyword**: Used to inherit from a single class
4. **implements Keyword**: Used to implement one or more interfaces
5. **super Keyword**: Used to call parent class methods and constructors
6. **Method Overriding**: Child class can provide its own implementation of parent class methods
7. **Polymorphism**: Objects can be treated as instances of their parent type

## Benefits of Inheritance

- **Code Reusability**: Existing code can be reused in new classes
- **Method Overriding**: Runtime polymorphism through method overriding
- **Establishing Relationships**: Creates a clear hierarchy and relationship between classes
- **Maintainability**: Changes in parent class automatically affect child classes

## Running the Examples

Navigate to each folder and compile/run the Java files:

```bash
# Single Inheritance
cd single_inheritance
javac SingleInheritance.java
java oops.inheritance.singleInheritance.SingleInheritance

# Multilevel Inheritance
cd ../multilevel_inheritance
javac MultilevelInheritance.java
java oops.inheritance.multilevelInheritance.MultilevelInheritance

# Hierarchical Inheritance
cd ../hierarchical_inheritance
javac HierarchicalInheritance.java
java oops.inheritance.hierarchicalInheritance.HierarchicalInheritance

# Multiple Inheritance via Interfaces
cd ../multiple_inheritance_via_interfaces
javac MultipleInheritanceViaInterfaces.java
java oops.inheritance.multipleInheritanceViaInterfaces.MultipleInheritanceViaInterfaces
```

## Summary

This package covers all major types of inheritance supported in Java:
- **Single Inheritance**: Simple parent-child relationship
- **Multilevel Inheritance**: Chain of inheritance
- **Hierarchical Inheritance**: Multiple children from one parent
- **Multiple Inheritance via Interfaces**: Multiple interfaces for composition

Each type has its own use case and benefits. Understanding these inheritance types is crucial for designing robust object-oriented applications in Java.
