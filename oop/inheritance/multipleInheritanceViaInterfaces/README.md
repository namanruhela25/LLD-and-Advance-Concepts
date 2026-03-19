# Multiple Inheritance via Interfaces in Java

This folder demonstrates **Multiple Inheritance via Interfaces** in Java, where a class can implement multiple interfaces to achieve multiple inheritance behavior.

## What is Multiple Inheritance via Interfaces?

Java doesn't support multiple inheritance with classes (a class cannot extend multiple classes), but it does support implementing multiple interfaces. An interface is a contract that defines what methods a class must implement.

By implementing multiple interfaces, a class can inherit behavior from multiple sources, achieving the benefits of multiple inheritance while avoiding the complexity and ambiguity of the diamond problem.

## Implementation Overview

### Interfaces

1. **`Swimmer` Interface**:
   - Method: `swim()`

2. **`Flyer` Interface**:
   - Method: `fly()`

3. **`Runner` Interface**:
   - Method: `run()`

### Concrete Classes

1. **`Duck` Class** (implements Swimmer, Flyer):
   - **Fields**: `name`, `age`
   - **Methods**:
     - `swim()`: Implements Swimmer interface
     - `fly()`: Implements Flyer interface
     - `eat()`: Additional method
     - `displayInfo()`: Display duck information

2. **`Dog` Class** (implements Runner, Swimmer):
   - **Fields**: `name`, `breed`
   - **Methods**:
     - `run()`: Implements Runner interface
     - `swim()`: Implements Swimmer interface
     - `bark()`: Additional method
     - `displayInfo()`: Display dog information

## Key Features Demonstrated

1. **Multiple Interface Implementation**: Classes implement multiple interfaces using comma-separated syntax
2. **Contract Fulfillment**: Each class implements all methods from all interfaces it implements
3. **Polymorphism**: Objects can be treated as instances of any interface they implement
4. **Avoids Diamond Problem**: Multiple interfaces don't cause the ambiguity of multiple class inheritance
5. **Behavior Composition**: Classes compose behaviors from multiple sources (interfaces)

## How to Run

```bash
cd multiple_inheritance_via_interfaces
javac MultipleInheritanceViaInterfaces.java
java oops.inheritance.multipleInheritanceViaInterfaces.MultipleInheritanceViaInterfaces
```

## Expected Output

```
--- Multiple Inheritance via Interfaces Example ---

Duck Information:
Name: Donald, Age: 5
Donald is swimming in the water...
Donald is flying in the sky...
Donald is eating...


Dog Information:
Name: Max, Breed: Golden Retriever
Max is running fast...
Max is swimming (dogs can swim)...
Max is barking: Woof! Woof!
```

## Why Interfaces for Multiple Inheritance?

1. **Clarity**: Makes it clear what behaviors a class provides
2. **Flexibility**: A class can implement multiple unrelated interfaces
3. **Decoupling**: Interfaces reduce dependency between classes
4. **Avoids Ambiguity**: Unlike classes, interfaces don't have the "diamond problem"

This implementation demonstrates how multiple interfaces allow a class to combine behaviors from different sources while maintaining clean code design.
