# Method Overloading (Compile-time/Static Polymorphism)

This folder demonstrates **Method Overloading**, which is a type of compile-time (static) polymorphism in Java.

## What is Method Overloading?

Method overloading allows a class to have multiple methods with the **SAME NAME** but **DIFFERENT PARAMETERS**.

The compiler determines which method to call based on:
1. **Number of parameters**
2. **Type of parameters**
3. **Order of parameters**

The return type alone is **NOT** sufficient for method overloading.

## Rules for Method Overloading

1. Methods must have the same name
2. Methods must have different parameter lists (number, type, or order)
3. Return type can be different (but is not considered for overloading decision)
4. May throw different exceptions
5. Can have different access modifiers

## When is Method Overloading Decided?

**At COMPILE TIME (Static/Early Binding)**

The Java compiler decides which method to call based on the method signature and the arguments passed at compile time.

## Example: Calculator Class

### Methods with Different Number of Parameters:
- `add(int a, int b)` - Adds 2 integers
- `add(int a, int b, int c)` - Adds 3 integers

### Methods with Different Parameter Types:
- `add(int a, int b)` - Adds integers
- `add(double a, double b)` - Adds doubles
- `add(long a, long b)` - Adds longs
- `add(float a, float b)` - Adds floats

### Methods with Different Order of Parameters:
- `add(String a, int b)` - String followed by int
- `add(int a, String b)` - int followed by String

## Key Interview Questions

### Q1: What is Method Overloading?
**A:** Method overloading is a compile-time polymorphism that allows multiple methods with the same name but different parameters in a single class.

### Q2: Can we overload methods by changing the return type only?
**A:** No, return type alone is not sufficient. The compiler cannot determine which method to invoke based solely on the return type.

### Q3: When is the decision made about which overloaded method to call?
**A:** At COMPILE TIME. The decision is made based on the types and number of arguments passed.

### Q4: What is Type Promotion in Method Overloading?
**A:** If an exact match is not found, the compiler tries to find the next broader type. For example:
- `byte` → `short` → `int` → `long` → `float` → `double`
- `char` → `int` → `long` → `float` → `double`

### Q5: Can we overload main() method?
**A:** Yes! `main()` method can also be overloaded. However, only `public static void main(String[] args)` is the entry point for the program.

### Q6: What happens if two overloaded methods can match the arguments?
**A:** This creates ambiguity and results in a COMPILE-TIME ERROR. Example:
```java
class Test {
    void add(int a, double b) { }
    void add(double a, int b) { }
}
// Test t = new Test();
// t.add(5, 10);  // Compilation Error: Ambiguous
```

### Q7: Can we overload constructors?
**A:** Yes! Constructors can be overloaded just like regular methods. Each constructor must have a different parameter list.
```java
class Person {
    Person() { }  // No args constructor
    Person(String name) { }  // String arg
    Person(String name, int age) { }  // String and int
}
```

### Q8: What is varargs (Variable-Length Arguments)?
**A:** Varargs allow a method to accept variable number of arguments of the same type using `...` notation. They are treated as an array internally.
```java
void add(int... numbers) {  // Can accept 0 to many arguments
    for (int num : numbers) { }
}

add();  // 0 arguments
add(5);  // 1 argument
add(5, 10, 15);  // 3 arguments
```

### Q9: Can we overload a method with varargs?
**A:** Yes, but with caution. Varargs can create ambiguity:
```java
void method(int x, int... args) { }  // Method 1
void method(int... args) { }  // Method 2

method(5);  // Ambiguous! Could call either method
```

### Q10: What is Autoboxing and Unboxing in method overloading?
**A:** Autoboxing automatically converts primitive to wrapper class, and unboxing converts wrapper back to primitive. This can affect method overloading:
```java
void add(int a) { System.out.println("int"); }
void add(Integer a) { System.out.println("Integer"); }

add(5);  // Calls add(int) - wrapper not preferred if primitive exists
```

### Q11: What is the exact order of method selection in overloading?
**A:** The compiler follows this order:
1. **Exact match** - Exact parameter types
2. **Widening** - Primitive type widening (int → long → float → double)
3. **Boxing** - Auto-boxing (int → Integer)
4. **Varargs** - Variable-length arguments

### Q12: Can null be passed to overloaded methods?
**A:** Yes, but it can cause ambiguity:
```java
void display(String s) { System.out.println("String"); }
void display(Integer i) { System.out.println("Integer"); }

display(null);  // Compilation Error: Ambiguous
```

### Q13: What is the difference between method overloading and method overriding in terms of performance?
**A:** 
- **Overloading**: No performance impact. Decision made at compile-time.
- **Overriding**: Slight performance overhead. Method resolution happens at runtime (dynamic dispatch).

### Q14: Can we overload methods in the same package from different classes?
**A:** No. Overloading only works within the same class. When methods are in different classes, it's not overloading even if they have the same name and parameters.

### Q15: What happens with implicit type conversion in overloading?
**A:** The compiler chooses the most specific match. If no exact match, it widens the type:
```java
void add(long a) { System.out.println("long"); }
add(5);  // 5 is int, auto-widened to long
```

## How to Run

```bash
cd method_overloading
javac Calculator.java MethodOverloadingDemo.java
java oops.polymorphism.methodOverloading.MethodOverloadingDemo
```

## Expected Output

```
=== METHOD OVERLOADING DEMONSTRATION ===

Example 1: Adding two integers
Called: add(int, int)
Result: 30

Example 2: Adding three integers
Called: add(int, int, int)
Result: 60

Example 3: Adding two doubles
Called: add(double, double)
Result: 31.0

Example 4: Adding two longs
Called: add(long, long)
Result: 300

Example 5: Adding two floats
Called: add(float, float)
Result: 16.0

Example 6: Concatenating String and int
Called: add(String, int)
Result: Total: 100

Example 7: Concatenating int and String
Called: add(int, String)
Result: 50 is the answer

=== TYPE PROMOTION IN METHOD OVERLOADING ===

Example: Passing int when double is expected
Called: add(double, double)
Result: 15.0
```

## Benefits of Method Overloading

1. **Code Readability**: Same method name for similar operations
2. **Code Reusability**: Same operation name for different data types
3. **Improved Maintainability**: Easier to understand intent
4. **Type Safety**: Compiler catches type errors
5. **Intuitive API**: Natural way to call methods

## Common Pitfalls

1. **Assuming return type matters**: Only parameters matter
2. **Ambiguous method calls**: Can lead to compilation errors
3. **Type promotion issues**: Unexpected method calls due to auto type promotion
4. **Overloading vs Overriding confusion**: Different concepts

## Real-world Examples

- `System.out.println()` - Overloaded to accept different data types
- `Arrays.sort()` - Overloaded for different array types
- `Math.abs()` - Overloaded for int, long, float, double
- `String.valueOf()` - Overloaded for various data types
