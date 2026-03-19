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

### Comprehensive Interview Questions and Answers

#### Q1: What is polymorphism?
**A:** Polymorphism means "many forms". It is the ability of an object to take on multiple forms or the ability of a method to behave differently based on context. In Java, it's achieved through method overloading and method overriding.

#### Q2: What are the two types of polymorphism in Java?
**A:** 
1. **Compile-time Polymorphism (Static)**: Method Overloading - resolved at compile time
2. **Runtime Polymorphism (Dynamic)**: Method Overriding - resolved at runtime

#### Q3: What is method overloading?
**A:** Method overloading allows multiple methods with the same name but different parameters (number, type, or order) in the same class. It's resolved at compile-time.

#### Q4: What is method overriding?
**A:** Method overriding allows a subclass to provide its own implementation of a method defined in a parent class. Same method signature, different implementation. Resolved at runtime.

#### Q5: What is the difference between overloading and overriding?
**A:** 
| Aspect | Overloading | Overriding |
|--------|------------|-----------|
| **Class** | Same class | Parent-child |
| **Binding** | Compile-time | Runtime |
| **Parameters** | Different | Same |
| **Return Type** | Can differ | Must match |
| **Purpose** | Code clarity | Polymorphism |

#### Q6: When is method overloading resolved?
**A:** At COMPILE TIME. The compiler decides which method to call based on the method signature and argument types passed.

#### Q7: When is method overriding resolved?
**A:** At RUNTIME (Dynamic/Late Binding). The JVM determines which method to call based on the actual object type at runtime, not the reference type.

#### Q8: Can you override static methods?
**A:** No. Static methods are NOT overridden; they are HIDDEN. Method overriding applies only to instance methods.

#### Q9: Can you change the access modifier while overriding?
**A:** The overriding method cannot have a MORE RESTRICTIVE access modifier. It can be less restrictive:
- Parent: `public` → Child: `public` ✓
- Parent: `public` → Child: `protected` ✗ (ERROR)

#### Q10: What is the @Override annotation?
**A:** An optional annotation that tells the compiler to check if the method actually overrides a parent method. Helps catch errors at compile-time. Not required but recommended best practice.

#### Q11: Can we overload methods by changing only the return type?
**A:** No. Return type alone is not sufficient for method overloading. The compiler looks at parameter types and count.

#### Q12: What is Type Promotion in method overloading?
**A:** If an exact parameter match isn't found, the compiler tries the next broader type:
- `byte` → `short` → `int` → `long` → `float` → `double`
- This can lead to unexpected method calls or ambiguity

#### Q13: What is Covariant Return Type?
**A:** (Java 5+) The overriding method's return type can be a SUBTYPE of the parent method's return type:
```java
class Parent { Animal getAnimal() { } }
class Child extends Parent { @Override Dog getAnimal() { } }  // OK
```

#### Q14: Can we override private methods?
**A:** No. Private methods are not visible to subclasses, so they cannot be overridden. A subclass method with the same signature would be a new method.

#### Q15: Can we override final methods?
**A:** No. Methods declared as `final` cannot be overridden. Final is used to prevent overriding.

#### Q16: What is the purpose of the super keyword?
**A:** The `super` keyword is used to:
- Call parent class methods: `super.methodName()`
- Call parent class constructor: `super()`
- Access parent class variables: `super.variableName`

#### Q17: What is the Liskov Substitution Principle (LSP)?
**A:** Objects of a superclass must be replaceable with objects of its subclasses without breaking the application. Overridden methods must maintain the contract.

#### Q18: Explain late binding (dynamic binding)
**A:** The process where the JVM determines at runtime which method to execute based on the actual object type, not the reference type:
```java
Shape shape = new Circle();  // Reference is Shape, object is Circle
shape.draw();  // Calls Circle's draw(), determined at runtime
```

#### Q19: What is method hiding and how is it different from overriding?
**A:**
- **Method Overriding**: Applies to instance methods, determined at runtime
- **Method Hiding**: Applies to static methods, determined at compile-time
```java
class Parent { static void method() { } }
class Child extends Parent { static void method() { } }  // Hides, not overrides
```

#### Q20: Can we overload constructors?
**A:** Yes! Constructors can be overloaded just like regular methods. Each constructor must have different parameters.

#### Q21: What is varargs (Variable-Length Arguments)?
**A:** Varargs allow a method to accept a variable number of arguments of the same type using `...` notation:
```java
void method(int... numbers) { }
method();  method(5);  method(5, 10, 15);  // All valid
```

#### Q22: Can we overload with varargs?
**A:** Yes, but carefully. Varargs can create ambiguity:
```java
void method(int x, int... args) { }
void method(int... args) { }
method(5);  // Ambiguous! ERROR
```

#### Q23: What is autoboxing and how does it affect overloading?
**A:** Autoboxing automatically converts primitives to wrapper classes. The compiler prefers exact matches:
```java
void method(int a) { }        // Called first
void method(Integer a) { }
method(5);  // Calls method(int), not method(Integer)
```

#### Q24: How does polymorphism support the Open/Closed Principle?
**A:** Polymorphism allows classes to be OPEN for extension (new child classes) but CLOSED for modification (parent class doesn't change). Add new shapes without modifying existing Shape class.

#### Q25: What are real-world examples of polymorphism in Java?
**A:**
1. **Collections Framework**: List, Set, Map interfaces with multiple implementations
2. **JDBC**: Different database drivers implementing Connection interface
3. **Streams**: Different stream classes (FileInputStream, ByteArrayInputStream, etc.)
4. **Comparator**: Different comparators for same objects
5. **Exception Handling**: Different exception types in catch blocks

#### Q26: What is the significance of instanceof operator?
**A:** Used to check if an object is an instance of a class before casting:
```java
if (shape instanceof Circle) {
    Circle circle = (Circle) shape;  // Safe downcasting
    circle.radiusMethod();
}
```

#### Q27: Explain method overriding with abstract classes
**A:** Abstract classes define contracts through abstract methods that MUST be overridden by concrete subclasses. Provides a template for subclasses to follow.

#### Q28: Explain method overriding with interfaces
**A:** Interfaces define contracts where all methods are abstract. Classes implementing interfaces MUST override all methods (except default methods in Java 8+).

#### Q29: What happens if a parent method throws an exception?
**A:** Child's overriding method CAN:
- Throw the same exception ✓
- Throw a sub-exception (narrower) ✓
- Throw no exception ✓
- But CANNOT throw new or broader checked exceptions ✗

#### Q30: Design a system that demonstrates polymorphism effectively
**A:** Example - Payment Processing:
```java
interface PaymentMethod {
    void processPayment(double amount);
}
class CreditCard implements PaymentMethod { @Override void processPayment(double amt) { } }
class PayPal implements PaymentMethod { @Override void processPayment(double amt) { } }
class Bitcoin implements PaymentMethod { @Override void processPayment(double amt) { } }

class PaymentProcessor {
    void pay(PaymentMethod method, double amount) {
        method.processPayment(amount);  // Polymorphic call
    }
}
```

### Key Questions You Must Know

1. What is polymorphism and its types?
2. Explain method overloading with examples
3. Explain method overriding with examples
4. Difference between compile-time and runtime polymorphism
5. When is overloading/overriding resolved?
6. Can you override static methods?
7. What is the @Override annotation?
8. Can you change access modifiers while overriding?
9. Explain late binding with examples
10. How does polymorphism enable code reusability?

### Things You Should Be Able to Do

- [ ] Write example code for method overloading
- [ ] Write example code for method overriding
- [ ] Explain how polymorphism works
- [ ] Trace code execution to determine which method is called
- [ ] Identify compilation errors vs runtime errors
- [ ] Explain benefits and use cases
- [ ] Design a polymorphic system
- [ ] Explain SOLID principles related to polymorphism
- [ ] Handle casting and instanceof correctly
- [ ] Differentiate between method hiding and overriding

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
