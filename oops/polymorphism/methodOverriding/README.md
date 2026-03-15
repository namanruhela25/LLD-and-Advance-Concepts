# Method Overriding (Runtime/Dynamic Polymorphism)

This folder demonstrates **Method Overriding**, which is a type of runtime (dynamic) polymorphism in Java.

## What is Method Overriding?

Method overriding allows a subclass to provide a specific implementation of a method that is already defined in its parent class.

The subclass method must have:
1. **Same method name**
2. **Same parameter list (number, type, and order)**
3. **Same or covariant return type**
4. **Same or less restrictive access modifier**

## Rules for Method Overriding

1. **Method signature must be same**: Name and parameters (including type and order) must match
2. **Return type**: Must be the same or a subtype (Covariant Return Type)
3. **Access modifier**: Cannot be more restrictive than parent class method
4. **Exceptions**: Cannot throw new or broader checked exceptions
5. **Parent method cannot be final**: `final` methods cannot be overridden
6. **Parent class cannot be final**: `final` classes cannot be extended
7. **Only instance methods**: Static methods are hidden, not overridden
8. **Cannot be overridden**: `private` methods cannot be overridden (they are hidden)

## When is Method Overriding Decided?

**At RUNTIME (Dynamic/Late Binding)**

The actual method to execute is determined at runtime based on the **actual object type**, not the reference type.

```java
Shape shape = new Circle();  // Reference is Shape, Object is Circle
shape.draw();  // At runtime, Circle's draw() is called, not Shape's
```

## Polymorphism in Action

### Example 1: Direct References
```java
Circle circle = new Circle(...);
circle.draw();  // Calls Circle's draw()
```

### Example 2: Parent Reference with Child Object (Polymorphism)
```java
Shape shape = new Circle(...);
shape.draw();  // At runtime, calls Circle's draw(), not Shape's
```

### Example 3: Array of Parent Type
```java
Shape[] shapes = {new Circle(...), new Rectangle(...), new Triangle(...)};
for (Shape shape : shapes) {
    shape.draw();  // Each calls its own overridden draw() method
}
```

## Key Interview Questions

### Q1: What is Method Overriding?
**A:** Method overriding allows a subclass to provide its own implementation of a method that is already defined in its parent class. The method signature must be the same.

### Q2: When is Method Overriding resolved?
**A:** At RUNTIME (Late Binding/Dynamic Binding). The JVM determines which method to execute based on the actual object type at runtime, not the reference type.

### Q3: What is the difference between Method Overloading and Method Overriding?
**A:**
| Aspect | Overloading | Overriding |
|--------|------------|-----------|
| Location | Same Class | Parent-Child Classes |
| Method Signature | Different Parameters | Same Parameters |
| Return Type | Can be Different | Must be Same/Subtype |
| Binding Time | Compile Time (Static) | Runtime (Dynamic) |
| Purpose | Code Clarity | Polymorphism |

### Q4: What is the @Override annotation?
**A:** An optional annotation that tells the compiler to check if the method actually overrides a parent method. Helps prevent errors. Not required but recommended.

```java
@Override
void draw() {
    // method implementation
}
```

### Q5: Can we override static methods?
**A:** No. Static methods are NOT overridden; they are **hidden**. If a subclass defines a static method with the same signature as the parent, it hides the parent's method. This is method hiding, not overriding.

```java
class Parent {
    static void staticMethod() { }  // Hidden, not overridden
}
class Child extends Parent {
    static void staticMethod() { }  // Hides parent's method
}
```

### Q6: Can we change the access modifier when overriding?
**A:** The overriding method cannot have a MORE RESTRICTIVE access modifier. It can be less restrictive:

```
public > protected > default > private
```

Valid:
- Parent: `protected` → Child: `public` ✓
- Parent: `public` → Child: `public` ✓

Invalid:
- Parent: `public` → Child: `protected` ✗
- Parent: `protected` → Child: `private` ✗

### Q7: What is Covariant Return Type?
**A:** (Java 5+) The return type of the overriding method can be a **subtype** of the return type in the parent method.

```java
class Parent {
    Animal getAnimal() { return new Animal(); }
}
class Child extends Parent {
    @Override
    Dog getAnimal() { return new Dog(); }  // Dog is subtype of Animal ✓
}
```

### Q8: Can we override private methods?
**A:** No. Private methods cannot be overridden because they are not visible to the subclass. The subclass method with the same signature would be a new method, not overriding.

### Q9: Can we override final methods?
**A:** No. Methods declared as `final` cannot be overridden. `final` is used to prevent overriding.

```java
class Parent {
    final void cannotOverride() { }
}
class Child extends Parent {
    void cannotOverride() { }  // Compilation Error
}
```

### Q10: What is the real-world benefit of method overriding?
**A:** Enables **polymorphism**, allowing you to:
- Write generic code that works with parent class references
- Handle different object types uniformly
- Add new child classes without changing existing code (Open/Closed Principle)
- Implement abstraction effectively

### Q11: What is the super keyword used for in method overriding?
**A:** The `super` keyword is used to call the parent class method or access parent class variables:
```java
class Parent {
    void draw() { System.out.println("Parent draw"); }
}
class Child extends Parent {
    @Override
    void draw() {
        super.draw();  // Calls parent's draw() method
        System.out.println("Child draw");
    }
}
```

### Q12: What is the difference between abstract classes and interfaces regarding method overriding?
**A:**
- **Abstract Classes**: Can have abstract methods (must override) and concrete methods (can choose to override)
- **Interfaces**: All methods are abstract (must override all non-default methods)
- Both enforce method overriding contracts but in different ways

### Q13: What is the instanceof operator and how is it used with overriding?
**A:** `instanceof` checks if an object is an instance of a class. Used to downcast safely:
```java
Shape shape = new Circle();
if (shape instanceof Circle) {
    Circle circle = (Circle) shape;
    circle.radiusSpecificMethod();
}
```

### Q14: Can we override a method and make it final?
**A:** Yes! Once overridden as `final`, it cannot be overridden further in subclasses:
```java
class Parent {
    void draw() { }
}
class Child extends Parent {
    @Override
    final void draw() { }  // Final: cannot override in grandchildren
}
class GrandChild extends Child {
    void draw() { }  // Compilation Error
}
```

### Q15: What is method hiding and how is it different from method overriding?
**A:** 
- **Method Overriding**: For instance methods (called on objects)
- **Method Hiding**: For static methods (called on class)
```java
class Parent {
    static void staticMethod() { System.out.println("Parent static"); }
}
class Child extends Parent {
    static void staticMethod() { System.out.println("Child static"); }  // Hides, not overrides
}

Parent p = new Child();
p.staticMethod();  // Calls Parent's staticMethod() - static dispatch
```

### Q16: Can we override a method from Object class?
**A:** Yes! The `Object` class methods can be overridden. Common examples:
- `toString()`
- `equals()`
- `hashCode()`
```java
class Person {
    @Override
    public String toString() {
        return "Custom representation";
    }
}
```

### Q17: What happens if a parent method throws an exception?
**A:** A child class overriding method can:
- Throw the same exception
- Throw a subclass exception (narrower)
- Throw no exception

BUT cannot throw a new or broader checked exception:
```java
class Parent {
    void method() throws IOException { }
}
class Child extends Parent {
    @Override
    void method() throws FileNotFoundException { }  // OK: FileNotFoundException is subclass of IOException
    // void method() throws Exception { }  // ERROR: Exception is broader
}
```

### Q18: What is polymorphic behavior in practical terms?
**A:** It means you can treat different object types uniformly through a common parent reference:
```java
List<Shape> shapes = new ArrayList<>();  // Parent reference
shapes.add(new Circle());
shapes.add(new Rectangle());

for (Shape shape : shapes) {
    shape.draw();  // Each shape draws itself differently
}
```

### Q19: What is dynamic method dispatch (late binding)?
**A:** The process where the JVM determines at runtime which method to call based on the object's actual type:
```java
Shape shape = new Circle();  // Reference is Shape
shape.draw();  // At runtime, JVM sees object is Circle and calls Circle.draw()
```

### Q20: Can we override a constructor?
**A:** No. Constructors cannot be overridden. They are class-specific and not inherited. Each class has its own constructors.

### Q21: What is stable method resolution in inheritance?
**A:** A method that is overridden in a child class provides the stable resolution point. All further child classes must respect this contract:
```java
class A { void method() { } }
class B extends A { @Override void method() { } }  // Overrides A's method
class C extends B { @Override void method() { } }  // Must match B's signature
```

### Q22: How does overriding support the Liskov Substitution Principle (LSP)?
**A:** Child objects must be substitutable for parent objects without breaking functionality:
```java
// OK: Dog is substitutable for Animal
Animal animal = new Dog();
animal.eat();  // Works correctly
```

## Benefits of Method Overriding

1. **Polymorphism**: Same method call, different behaviors based on object type
2. **Code Flexibility**: Write code for parent type, work with child objects
3. **Loose Coupling**: Parent and child classes are loosely coupled
4. **Extensibility**: Easy to add new child classes without changing parent
5. **Abstraction**: Hide implementation details, expose contracts

## Real-world Examples

1. **Serializable interface**: Different classes implement serialization differently
2. **Comparable interface**: Different objects implement compareTo() differently
3. **Runnable interface**: Different threads implement run() differently
4. **Shape hierarchy**: Different shapes calculate area differently
5. **Database connections**: Different databases implement connection methods differently

## How to Run

```bash
cd method_overriding
javac Shape.java MethodOverridingDemo.java
java oops.polymorphism.methodOverriding.MethodOverridingDemo
```

## Expected Output

```
=== METHOD OVERRIDING DEMONSTRATION ===

--- Example 1: Direct Object References ---

Shape Name: MyCircle, Color: Red
Radius: 5
Drawing a Red Circle named MyCircle
Area: 78.53981633974483

Shape Name: MyRectangle, Color: Blue
Length: 10, Width: 8
Drawing a Blue Rectangle named MyRectangle
Area: 80.0

Shape Name: MyTriangle, Color: Green
Base: 6, Height: 4
Drawing a Green Triangle named MyTriangle
Area: 12.0

--- Example 2: Using Parent Class Reference (Polymorphism) ---

--- Iterating through Shape array ---

=== Processing shape ===
Shape Name: CircleShape, Color: Yellow
Radius: 3
Drawing a Yellow Circle named CircleShape
Area: 28.274333882308138

=== Processing shape ===
Shape Name: RectangleShape, Color: Pink
Length: 12, Width: 6
Drawing a Pink Rectangle named RectangleShape
Area: 72.0

=== Processing shape ===
Shape Name: TriangleShape, Color: Purple
Base: 8, Height: 5
Drawing a Purple Triangle named TriangleShape
Area: 20.0

... (continues with more examples)
```

This implementation demonstrates how method overriding enables true polymorphism, where the same method call produces different results based on the actual object type at runtime.
