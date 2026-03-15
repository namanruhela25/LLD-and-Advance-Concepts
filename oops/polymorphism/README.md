# Polymorphism in Java 

This folder demonstrates **Polymorphism**, one of the four fundamental principles of Object-Oriented Programming (OOP), with comprehensive details for interview preparation.

## What is Polymorphism?

**Polymorphism** means "many forms". It is the ability of an object to take on multiple forms or the ability of a method to behave differently based on the context.

In Java, polymorphism is achieved through:
1. **Method Overloading** (Compile-time/Static Polymorphism)
2. **Method Overriding** (Runtime/Dynamic Polymorphism)

## Types of Polymorphism in Java

### 1. **Compile-time Polymorphism (Static Polymorphism) - Method Overloading**

**What**: A class has multiple methods with the same name but different parameters.

**When**: Resolved at **COMPILE TIME** by the compiler.

**How it works**:
- Compiler checks the method signature (name + parameter types)
- Based on the arguments passed, the compiler determines which method to call
- This decision is made before the program runs

**Folder**: `method_overloading/`

**Example**:
```java
class Calculator {
    public int add(int a, int b) { }
    public double add(double a, double b) { }
    public int add(int a, int b, int c) { }
}

Calculator calc = new Calculator();
calc.add(5, 10);              // Calls add(int, int)
calc.add(5.5, 10.5);          // Calls add(double, double)
calc.add(5, 10, 15);          // Calls add(int, int, int)
```

**Interview Questions Related to Method Overloading**:

| Question | Answer |
|----------|--------|
| Can we overload by changing return type only? | No, return type alone is not sufficient |
| When is method overloading resolved? | At compile time |
| Can we overload main()? | Yes, but only `public static void main(String[])` is entry point |
| What is type promotion? | If exact match not found, compiler tries broader types (byte → int → long → float → double) |
| Is overloading same as overriding? | No, overloading is compile-time, overriding is runtime |

---

### 2. **Runtime Polymorphism (Dynamic Polymorphism) - Method Overriding**

**What**: A subclass provides its own implementation of a method that is already defined in its parent class.

**When**: Resolved at **RUNTIME** by the JVM.

**How it works**:
- Reference can be of parent type, but object is of child type
- At runtime, JVM checks the actual object type
- Calls the overridden method from the actual object's class
- This decision is made when the program runs

**Folder**: `method_overriding/`

**Example**:
```java
Shape shape = new Circle();     // Reference is Shape, object is Circle
shape.draw();                    // At runtime, calls Circle's draw(), not Shape's

Shape[] shapes = {
    new Circle(),
    new Rectangle(),
    new Triangle()
};

for (Shape s : shapes) {
    s.draw();  // Each calls its own overridden draw() method
}
```

**Interview Questions Related to Method Overriding**:

| Question | Answer |
|----------|--------|
| When is method overriding resolved? | At runtime (late binding) |
| Can we override static methods? | No, static methods are hidden, not overridden |
| Can we change access modifier? | Only to less restrictive (public > protected > default > private) |
| What is covariant return type? | Return type can be a subtype of parent's return type |
| Can we override final methods? | No, final methods cannot be overridden |
| Can we override private methods? | No, private methods are not visible to subclasses |

---

## Key Differences: Overloading vs Overriding

| Aspect | Overloading | Overriding |
|--------|------------|-----------|
| **Class** | Same class | Different classes (parent-child) |
| **Method Name** | Same | Same |
| **Parameters** | Different (number, type, order) | Same (number, type, order) |
| **Return Type** | Can be different | Must be same or subtype |
| **Access Modifier** | Can be any | Cannot be more restrictive |
| **Binding Time** | Compile-time (Static) | Runtime (Dynamic) |
| **Purpose** | Code clarity, method variety | Polymorphism, dynamic behavior |
| **Inheritance** | Not required | Required (parent-child) |
| **@Override annotation** | Not applicable | Recommended |

---

## Polymorphism in Real-world Applications

### Example 1: Payment Processing System
```java
PaymentProcessor processor;

// Based on user choice
if (userChooses == "CreditCard") {
    processor = new CreditCardProcessor();
} else {
    processor = new PayPalProcessor();
}

processor.processPayment(amount);  // Calls appropriate implementation
```

### Example 2: Database Connection
```java
Database db;

// Based on database type
if (dbType == "MySQL") {
    db = new MySQLDatabase();
} else if (dbType == "PostgreSQL") {
    db = new PostgreSQLDatabase();
}

db.connect();  // Calls appropriate database connection
```

### Example 3: Shape Calculation System
```java
Shape[] shapes = {
    new Circle(5),
    new Rectangle(10, 20),
    new Triangle(15, 10)
};

double totalArea = 0;
for (Shape shape : shapes) {
    totalArea += shape.getArea();  // Polymorphic call
}
```

---

## Benefits of Polymorphism

1. **Code Reusability**: Generic code works with different object types
2. **Flexibility**: Easy to add new types without changing existing code
3. **Maintainability**: Changes are localized to specific classes
4. **Extensibility**: New classes can be added without modifying old code
5. **Loose Coupling**: Code depends on abstractions, not concrete implementations
6. **Abstraction**: Hide implementation details, expose only contracts

---

## Key Concepts

### @Override Annotation
- Optional but recommended annotation
- Tells compiler to check if method actually overrides parent method
- Helps catch errors early
- Makes code more readable and maintainable

```java
@Override
void draw() {
    // Implementation
}
```

### Late Binding (Dynamic Binding)
- Resolving method calls at runtime
- The actual method called depends on the object's runtime type
- Enables true polymorphism
- Different from early binding (compile-time)

### Reference Type vs Object Type
```java
Shape shape = new Circle();  // Reference type: Shape, Object type: Circle
shape.draw();               // Calls Circle's draw() method
```

### Upcasting vs Downcasting
```java
// Upcasting (always safe)
Shape shape = new Circle();

// Downcasting (requires instanceof check)
if (shape instanceof Circle) {
    Circle circle = (Circle) shape;
    circle.radiusSpecificMethod();
}
```

---

## Best Practices for Polymorphism

1. **Program to Interfaces**: Use parent type references
2. **Use @Override annotation**: Helps catch errors
3. **Keep Method Signatures Consistent**: Maintain contract between parent and child
4. **Use Abstract Classes for Shared Code**: When parent has implementation
5. **Use Interfaces for Contracts**: When defining behavior only
6. **Avoid Overloading with Similar Parameters**: Can lead to confusion
7. **Use @FunctionalInterface**: For single-method interfaces (Java 8+)
8. **Prefer Composition Over Inheritance**: When appropriate

---

## Common Pitfalls to Avoid

1. **Confusing Overloading with Overriding**: Different concepts
2. **Assuming Return Type for Overloading**: Parameters matter, not return type
3. **Changing Access Modifiers to More Restrictive**: Violates overriding rules
4. **Overriding Static Methods**: Creates method hiding, not overriding
5. **Not Using @Override**: Can miss overriding errors
6. **Overloading with Ambiguous Parameters**: Can cause compilation errors
7. **Forgetting Super Call**: May break parent class initialization

---

## Interview Preparation Notes

### Questions You Should Know

1. What is polymorphism?
2. What are the two types of polymorphism?
3. What is method overloading?
4. What is method overriding?
5. Difference between overloading and overriding
6. When is overloading resolved?
7. When is overriding resolved?
8. Can you override static methods?
9. Can you change access modifier while overriding?
10. What is @Override annotation?

### Things You Should Be Able to Do

- [ ] Write example code for method overloading
- [ ] Write example code for method overriding
- [ ] Explain how polymorphism works
- [ ] Trace code execution to determine which method is called
- [ ] Identify compilation errors vs runtime errors
- [ ] Explain benefits and use cases

---

## How to Run Examples

### Method Overloading
```bash
cd method_overloading
javac Calculator.java MethodOverloading.java
java oops.polymorphism.methodOverloading.MethodOverloading
```

### Method Overriding
```bash
cd method_overriding
javac Shape.java MethodOverriding.java
java oops.polymorphism.methodOverriding.MethodOverriding
```

---

This comprehensive guide covers all aspects of polymorphism in Java, perfect for interview preparation and understanding how polymorphism enables flexible, maintainable, and extensible code.
