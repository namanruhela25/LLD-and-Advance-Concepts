# LSP Signature Rules - Deep Dive

This folder explores the **specific rules that govern Liskov Substitution Principle** at the method signature level. These rules ensure that method overrides maintain the contract defined by parent classes.

---

## LSP Signature Rules Overview

When a subclass overrides a parent class method, it must follow specific rules about **method signatures** to maintain the Liskov Substitution Principle:

### The Four Signature Rules:

1. **Return Type Rule (Covariance)**: Return type can be covariant (narrower/more specific)
2. **Method Argument Rule (Contravariance)**: Parameters must be contravariant (same or broader)
3. **Exception Rule**: Exceptions must be same or narrower (not broader)
4. **Invariant Rule**: Class invariants must be maintained

---

## Rule 1: Return Type Rule (Covariance)

**File**: `ReturnTypeRule.java`

### What is Covariant Return Type?

A subclass can override a method and return a **more specific type** (subtype) than the parent's return type.

### The Concept

```java
// Class hierarchy
class Organism { }
class Animal extends Organism { }
class Dog extends Animal { }

// Parent method returns Organism
class Parent {
    public Organism getAnimal() {
        return new Animal();
    }
}

// Child method returns Dog (narrower/more specific)
class Child extends Parent {
    @Override
    public Dog getAnimal() {  // ✓ Legal - Dog is-a Animal is-a Organism
        return new Dog();
    }
}
```

### Why This Works with LSP

```java
// Client code
Parent p = new Child();
Organism result = p.getAnimal();  // ✓ Works - can assign Dog to Organism
```

Since `Dog` is-a `Animal` is-a `Organism`, returning a `Dog` from `Child.getAnimal()` is compatible with the parent contract that returns `Organism`.

### Visual Representation

```
Parent's Return Type Chain:
────────────────────────
Organism (Widest/Least Specific)
    ↑
  Animal
    ↑
   Dog (Narrowest/Most Specific)

Allowed Override:
  Parent returns: Organism
  Child returns: Dog (✓ More specific/Narrower)

NOT Allowed:
  Parent returns: Dog
  Child returns: Organism (✗ Less specific/Broader)
```

### Code Example

```java
// ✓ VALID Covariant Return Type
class Parent {
    public Number getNumber() {
        return 10;
    }
}

class Child extends Parent {
    @Override
    public Integer getNumber() {  // ✓ Integer is a Number
        return 20;
    }
}

// ✗ INVALID - Violates LSP
class BadChild extends Parent {
    @Override
    public String getNumber() {  // ✗ String is NOT a Number
        return "twenty";
    }
}
```

### Real-World Example

```java
// Parent interface
interface DataStore {
    Object getData();  // Returns generic Object
}

// Implementation for String data
class StringStore implements DataStore {
    @Override
    public String getData() {  // ✓ String is an Object - covariance OK
        return "Hello";
    }
}

// Implementation for specific type
class PersonStore implements DataStore {
    @Override
    public Person getData() {  // ✓ Person is an Object - covariance OK
        return new Person("John");
    }
}

// Client code works with any implementation
DataStore store = getStore();
Object data = store.getData();  // Works for all implementations
```

### Importance in Collections

```java
// This is why Java allows:
class Animal { }
class Dog extends Animal { }

interface AnimalFactory {
    Animal create();
}

class DogFactory implements AnimalFactory {
    @Override
    public Dog create() {  // ✓ Covariant return type
        return new Dog();
    }
}
```

---

## Rule 2: Method Argument Rule (Contravariance)

**File**: `MethodArgumentRule.java`

### What is Contravariant Arguments?

A subclass method should accept the **same or broader parameter types** as the parent method, NOT narrower types.

### The Principle

```java
// Basic example
class Parent {
    public void process(Number num) {  // Accept any Number
        System.out.println("Processing: " + num);
    }
}

// ✗ WRONG - Narrower parameter (violates LSP)
class BadChild extends Parent {
    @Override
    public void process(Integer num) {  // ✗ Only accepts Integer!
        System.out.println("Processing: " + num);
    }
}

// ✓ CORRECT - Same or broader parameter
class GoodChild extends Parent {
    @Override
    public void process(Number num) {  // ✓ Same type - OK
        System.out.println("Processing: " + num);
    }
}

class BetterChild extends Parent {
    @Override
    public void process(Object obj) {  // ✓ Broader type - also OK
        System.out.println("Processing: " + obj);
    }
}
```

### Why Narrowing Arguments Violates LSP

```java
// Parent accepts Number
class Parent {
    public void calculate(Number num) { }
}

// Child accepts only Integer
class Child extends Parent {
    @Override
    public void calculate(Integer num) { }  // ✗ Problem!
}

// Client code expects Parent behavior
Parent parent = new Child();

parent.calculate(10.5);  // ✗ CRASH! Child expects Integer, got Double!
```

### Visual Type Hierarchy

```
Parameter Type Chain (Contravariance):
─────────────────────────────────────

Object (Broadest/Least Specific)
    ↑
 Number
    ↑
Integer (Narrowest/Most Specific)

Parent Method: accept(Number)
  Child can override as: accept(Number) ✓ (Same)
  Child can override as: accept(Object) ✓ (Broader)
  Child CANNOT override as: accept(Integer) ✗ (Narrower)
```

### Practical Example

```java
// Database interface
interface DataProcessor {
    void process(Object data);  // Parent accepts any Object
}

// Email implementation
class EmailProcessor implements DataProcessor {
    @Override
    public void process(Object data) {  // ✓ Same - OK
        if (data instanceof String) {
            sendEmail((String) data);
        }
    }
}

// ✗ WRONG - Only accepts String
class BadEmailProcessor implements DataProcessor {
    @Override
    public void process(String email) {  // ✗ Narrower - violates LSP!
        sendEmail(email);
    }
}

// Client code
DataProcessor processor = new BadEmailProcessor();
processor.process(12345);  // ✗ Client expects this to work, but it fails!
```

### Why Java Method Overriding Works

Java's method overloading vs overriding rules prevent contravariance violations:

```java
class Parent {
    void print(Number n) { }
}

class Child extends Parent {
    // This is OVERLOADING, not OVERRIDING
    void print(Integer i) { }  // Different method!
    
    // This is OVERRIDING
    @Override
    void print(Number n) { }  // Must use same parameter type
}
```

---

## Rule 3: Exception Rule

**File**: `ExceptionRules.java`

### The Exception Hierarchy Rule

A subclass method should throw:
- ✓ **Same exceptions** as parent
- ✓ **Narrower exceptions** (subclass of parent's exception)
- ✗ **Broader exceptions** (superclass of parent's exception)
- ✗ **New/additional exceptions** (if not related)

### Exception Hierarchy

```
Exception (Broadest - Most General)
    ├── Checked Exceptions (Must be declared)
    │   ├── IOException (Broader)
    │   │   ├── FileNotFoundException (Narrower) ✓
    │   │   ├── EOFException (Narrower) ✓
    │   │   └── MalformedURLException (Narrower) ✓
    │   ├── ClassNotFoundException
    │   └── SQLException
    │
    └── RuntimeException (Unchecked)
        ├── ArithmeticException (Narrower) ✓
        ├── NullPointerException (Narrower) ✓
        ├── ArrayIndexOutOfBoundsException (Narrower) ✓
        └── IllegalArgumentException (Narrower) ✓
```

### Valid Exception Throws

```java
// Parent throws IOException
class Parent {
    public void readFile() throws IOException {
        throw new IOException("Read failed");
    }
}

// ✓ VALID - Child throws narrower exception
class Child extends Parent {
    @Override
    public void readFile() throws FileNotFoundException {
        throw new FileNotFoundException("File not found");
    }
}

// ✓ VALID - Child throws same exception
class Child2 extends Parent {
    @Override
    public void readFile() throws IOException {
        throw new IOException("Read failed");
    }
}

// ✓ VALID - Child throws no exceptions (narrower than parent's)
class Child3 extends Parent {
    @Override
    public void readFile() {
        System.out.println("Reading...");
    }
}
```

### Invalid Exception Throws

```java
// Parent throws FileNotFoundException
class Parent {
    public void readFile() throws FileNotFoundException {
        throw new FileNotFoundException("File not found");
    }
}

// ✗ INVALID - Child throws broader exception
class BadChild extends Parent {
    @Override
    public void readFile() throws IOException {  // ✗ Broader!
        throw new IOException("Read failed");
    }
}

// ✗ INVALID - Child throws unrelated exception
class BadChild2 extends Parent {
    @Override
    public void readFile() throws SQLException {  // ✗ Unrelated!
        throw new SQLException("DB error");
    }
}
```

### Why This Matters with LSP

```java
class Client {
    public void processFile(Parent processor) {
        try {
            processor.readFile();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }
}

// Client expects FileNotFoundException only
Client client = new Client();

// If Child throws IOException, the client code breaks!
Parent processor = new Child();  // Actually a Child
client.processFile(processor);  // Unexpected IOException!
```

### Code Example Analysis

```java
class Parent {
    public void getValue() throws RuntimeException {
        throw new RuntimeException("Parent error");
    }
}

// ✓ VALID - Throws narrower exception
class Child extends Parent {
    @Override
    public void getValue() throws ArithmeticException {
        throw new ArithmeticException("Child error");
    }
}

class Client {
    private Parent p;

    public Client(Parent p) {
        this.p = p;
    }

    public void takeValue() {
        try {
            p.getValue();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException occurred: " + e.getMessage());
        }
    }
}

// Both Parent and Child calls are safe
Parent parentInstance = new Parent();
Parent childInstance = new Child();

Client client1 = new Client(parentInstance);
Client client2 = new Client(childInstance);

client1.takeValue();  // ✓ RuntimeException caught
client2.takeValue();  // ✓ ArithmeticException (subclass of RuntimeException) caught
```

### Checked vs Unchecked Exceptions

```java
// With CHECKED Exception (Parent)
class Parent {
    public void process() throws IOException {  // Must declare
        throw new IOException("Error");
    }
}

// Child can throw narrower checked exception
class Child extends Parent {
    @Override
    public void process() throws FileNotFoundException {  // ✓ Narrower
        throw new FileNotFoundException("Not found");
    }
}

// With UNCHECKED Exception (Parent)
class Parent2 {
    public void process() throws RuntimeException {  // Can omit declaration
        throw new RuntimeException("Error");
    }
}

// Child can throw narrower unchecked exception
class Child2 extends Parent2 {
    @Override
    public void process() throws NullPointerException {  // ✓ Narrower
        throw new NullPointerException("Null value");
    }
}
```

---

## Rule 4: Invariant Rule

### Class Invariants Must Be Maintained

A subclass should maintain **all invariants** established by the parent class.

```java
// Parent defines an invariant: balance >= 0
class BankAccount {
    protected double balance = 1000;  // Invariant: balance >= 0
    
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;  // Maintains invariant
        }
    }
}

// ✓ Child maintains invariant
class SavingsAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        if (amount <= balance && balance - amount >= 100) {  // Even stricter
            balance -= amount;
        }
    }
}

// ✗ Child violates invariant
class BadAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        balance -= amount;  // ✗ Can go negative - violates invariant!
    }
}
```

### Invariant Violations in LSP

```java
class Customer {
    private int age = 0;  // Invariant: age >= 0 && age <= 150
    
    public void setAge(int age) {
        if (age >= 0 && age <= 150) {
            this.age = age;  // Maintains invariant
        }
    }
}

// ✗ Child violates invariant
class Person extends Customer {
    @Override
    public void setAge(int age) {
        this.age = age;  // ✗ No validation - can be -5 or 200!
    }
}

// Client expects invariant to hold
Customer customer = new Person();
customer.setAge(-10);  // ✗ Violates invariant!
```

---

## Summary of All Signature Rules

| Rule | Parent | Child | Valid? | Why? |
|------|--------|-------|--------|------|
| **Return Type** | Animal | Dog | ✓ | Covariance - narrower is OK |
| **Return Type** | Dog | Animal | ✗ | Contravariance - broader is NOT OK |
| **Arguments** | Number | Number | ✓ | Same type - always OK |
| **Arguments** | Number | Integer | ✗ | Contravariance - narrower is NOT OK |
| **Arguments** | Integer | Number | ✓ | Contravariance - broader is OK |
| **Exceptions** | IOException | FileNotFoundException | ✓ | Narrower exception is OK |
| **Exceptions** | FileNotFoundException | IOException | ✗ | Broader exception violates LSP |
| **Exceptions** | IOException | RuntimeException | ✗ | Different/broader is NOT OK |
| **Invariants** | age >= 0 | age >= 0 | ✓ | Same/stricter invariant is OK |
| **Invariants** | age >= 0 | No validation | ✗ | Weaker invariant violates LSP |

---

## How to Run the Examples

```bash
# Navigate to the directory
cd /LLD/solid/liskov_substiuion_principle/LSP_rules/SignatureRules

# Compile all files
javac *.java

# Run Return Type Rule example
java solid.liskov_substitution_principle.LSP_rules.SignatureRules.ReturnTypeRule
# Output:
# Parent class : Animal instance returned
# Child class : Dog instance returned

# Run Method Argument Rule example
java solid.liskov_substitution_principle.LSP_rules.SignatureRules.MethodArgumentRule
# Output:
# Parent class : print
# Child class : print

# Run Exception Rules example
java solid.liskov_substitution_principle.LSP_rules.SignatureRules.ExceptionRules
# Output:
# RuntimeException occurred: Parent error
# RuntimeException occurred: Child error
```

---

## Interview Questions and Answers

### Q1: What are LSP Signature Rules?
**A:** LSP Signature Rules are constraints on method overrides to ensure substitutability:

1. **Return Type Rule (Covariance)**: Child can return narrower (more specific) type than parent
2. **Argument Rule (Contravariance)**: Child should accept same or broader types than parent, not narrower
3. **Exception Rule**: Child should throw same or narrower exceptions, not broader
4. **Invariant Rule**: Child must maintain all invariants from parent

**Why?** Because client code calls parent methods - the contract must hold for substituted child classes.

### Q2: Explain Covariant Return Types with an Example
**A:** Covariance allows a subclass method to return a more specific type (subtype) than the parent.

```java
// Parent returns generic Animal
class AnimalShelter {
    public Animal adoptAnimal() {
        return new Animal();
    }
}

// Child returns specific Dog (subtype of Animal)
class DogShelter extends AnimalShelter {
    @Override
    public Dog adoptAnimal() {  // ✓ Covariant - Dog is-an Animal
        return new Dog();
    }
}

// Client code
AnimalShelter shelter = new DogShelter();
Animal animal = shelter.adoptAnimal();  // ✓ Works - Dog is-an Animal
```

**Key**: Since Dog IS-AN Animal, returning Dog satisfies the contract that expects Animal.

### Q3: Why Can't Parameters Be Covariant?
**A:** Because subclass method would become too restrictive, breaking substitutability:

```java
// Parent accepts any Shape
class Painter {
    public void paint(Shape shape) { }
}

// Child accepts only Circle (narrower)
class SpecialPainter extends Painter {
    @Override
    public void paint(Circle circle) { }  // ✗ Problem!
}

// In client code:
Painter painter = new SpecialPainter();
painter.paint(new Square());  // ✗ CRASH! SpecialPainter doesn't handle Square!
```

**Solution**: Parameters must be contravariant (same or broader):

```java
class BetterPainter extends Painter {
    @Override
    public void paint(Shape shape) { }  // ✓ Same - OK
}

class EvenBetterPainter extends Painter {
    @Override
    public void paint(Object obj) { }  // ✓ Broader - also OK
}
```

### Q4: What's the Difference Between Covariance and Contravariance?
**A:**

| Term | Definition | Example | Used For |
|------|-----------|---------|----------|
| **Covariance** | Preserves type ordering; goes from specific to general | Return `Dog` when `Animal` expected | Return Types |
| **Contravariance** | Reverses type ordering; goes from general to specific | Accept `Vehicle` when `Car` expected | Method Parameters |
| **Invariance** | No substitution allowed - exact type required | Must match exactly | Type safety |

**Memory Aid**:
- **Return types go COvariant** (narrow/specific is OK)
- **Parameters are ContraVARiant** (broad/general is OK)

### Q5: How Do Exceptions Relate to LSP?
**A:** Exceptions are part of the method's contract - they define what can go wrong. Subclasses must keep exceptions same or narrower:

```java
// Parent throws broad IOException
class FileReader {
    public void read() throws IOException { }
}

// ✓ Child throws narrower FileNotFoundException
class SafeFileReader extends FileReader {
    @Override
    public void read() throws FileNotFoundException { }
}

// Client catches IOException
Client client = new Client();
try {
    FileReader reader = new SafeFileReader();
    reader.read();
} catch (IOException e) {
    // Still catches FileNotFoundException (it's a IOException)
}

// ✗ If Child threw broader Exception:
class BadFileReader extends FileReader {
    @Override
    public void read() throws Exception { }  // ✗ Broader!
}

Client client2 = new Client();
try {
    FileReader reader = new BadFileReader();
    reader.read();
} catch (IOException e) {
    // ✗ Other Exceptions not caught!
}
```

### Q6: What is Precondition Weakening and Postcondition Strengthening?
**A:**

**Precondition**: What the method requires to be true before calling.

```java
// Parent requires: parameter must be positive
class Calculator {
    public int divide(int a, int positive_b) {
        return a / positive_b;
    }
}

// ✓ Child WEAKENS precondition (accepts more inputs)
class FlexibleCalculator extends Calculator {
    @Override
    public int divide(int a, int b) {  // ✓ Accepts any b
        return b == 0 ? 0 : a / b;
    }
}

// ✗ Child STRENGTHENS precondition (accepts fewer inputs)
class StrictCalculator extends Calculator {
    @Override
    public int divide(int a, int b) {
        if (b < 10) throw new IllegalArgumentException("b must be >= 10");
        return a / b;  // ✗ Violates contract!
    }
}
```

**Postcondition**: What the method guarantees after execution.

```java
// Parent guarantees: result is positive
class Parent {
    public int getAge() {
        // Invariant: age >= 0 && age <= 150
        return age;
    }
}

// ✗ Child WEAKENS postcondition (guarantees less)
class BadChild extends Parent {
    @Override
    public int getAge() {
        return new Random().nextInt();  // ✗ Can be negative!
    }
}

// ✓ Child STRENGTHENS postcondition (guarantees more)
class GoodChild extends Parent {
    @Override
    public int getAge() {
        return age >= 18 ? age : 0;  // ✓ Even stricter guarantee
    }
}
```

### Q7: Can a Child Method Throw a RuntimeException Even if Parent Doesn't?
**A:** Yes, RuntimeExceptions (unchecked exceptions) have special handling:

```java
// Parent doesn't throw anything
class Parent {
    public void process() {
        // No throws clause
    }
}

// ✓ Child can throw unchecked exceptions
class Child extends Parent {
    @Override
    public void process() throws RuntimeException {  // ✓ OK for unchecked
        if (data == null) {
            throw new NullPointerException("Data cannot be null");
        }
    }
}

// ✗ Child cannot throw checked exceptions
class BadChild extends Parent {
    @Override
    public void process() throws IOException {  // ✗ Illegal!
        // ...
    }
}
```

**Why?** Checked exceptions are part of the method contract and must be declared. Unchecked exceptions can happen anywhere and don't need declaration.

### Q8: Can a Child Method Remove the throws Declaration?
**A:** Yes, removing exceptions is always allowed:

```java
// Parent throws IOException
class Parent {
    public void read() throws IOException {
        throw new IOException("Read error");
    }
}

// ✓ Child removes exception handling
class SafeChild extends Parent {
    @Override
    public void read() {  // ✓ No throws - OK!
        try {
            // Safe implementation that doesn't throw
        } catch (IOException e) {
            // Handle internally
        }
    }
}
```

### Q9: What Happens with Method Overloading vs Overriding?
**A:** Different rules apply:

**Overriding** (same signature): Must follow LSP rules

```java
class Parent {
    public void process(int x) { }
}

class Child extends Parent {
    @Override
    public void process(int x) { }  // OVERRIDING - must follow LSP rules
}
```

**Overloading** (different signature): No LSP rules apply

```java
class Parent {
    public void process(int x) { }
    public void process(String s) { }  // Different signature - overloading
}

class Child extends Parent {
    public void process(double d) { }  // OVERLOADING - separate method
}
```

### Q10: How Do You Verify LSP Compliance for Signatures?
**A:** Use the substitutability test:

```java
// Test 1: Return Types
Parent parent = new Child();
Animal result = parent.getAnimal();  // ✓ If this works, return type is OK

// Test 2: Arguments
parent.process(obj);  // ✓ If all argument types work, arguments are OK

// Test 3: Exceptions
try {
    parent.risky();
} catch (ExpectedException e) {
    // ✓ If you catch what you expect, exceptions are OK
}

// Test 4: Invariants
if (parent.invariantHolds()) {
    parent.method();
    if (!parent.invariantHolds()) {
        // ✗ Invariant violated!
    }
}
```

### Q11: What Tools Can Detect LSP Violations?
**A:**

1. **Compiler**: Checks return types and exceptions (partially)
   ```java
   // Detects broadened exception
   class Child extends Parent {
       @Override
       public void method() throws Exception { }  // ✗ Compile error!
   }
   ```

2. **Type Checker**: Static analysis tools like Checker Framework, FindBugs

3. **Unit Tests**: Compare behavior of parent and child substitutes
   ```java
   @Test
   public void testSubstitutability() {
       Parent p1 = new Parent();
       Parent p2 = new Child();
       
       assertEquals(p1.compute(), p2.compute());  // Should behave same
   }
   ```

4. **Code Review**: Manual inspection for violations

### Q12: What's the Relationship Between Signature Rules and Type Theory?
**A:** These rules come from **type theory**:

- **Covariant Return Types**: Safe because we can substitute specific for general
- **Contravariant Parameters**: Safe because method accepts broader range
- **Exception Variance**: Safe because narrower exceptions are catchable in broader handlers

This is called the **Liskov Substitution Principle's formal definition** in terms of type theory.

### Q13: Can Collections Violate Signature Rules?
**A:** Yes, this is why Java has type erasure and wildcards:

```java
// ✗ This would violate LSP if allowed
List<Animal> animals = new ArrayList<Dog>();  // ✗ Compile error!
animals.add(new Cat());  // ✗ Would add Cat to Dog list!

// ✓ Solution: Use wildcards
List<? extends Animal> animals = new ArrayList<Dog>();  // ✓ OK
// animals.add(new Cat());  // ✗ Won't compile - prevents violation

// ✓ Or use super with contravariance
List<? super Dog> animals = new ArrayList<Animal>();  // ✓ OK
animals.add(new Dog());  // ✓ Safe
```

### Q14: What's the Impact of Signature Rule Violations?
**A:** Violations can cause:

1. **Runtime Exceptions**: Unexpected exceptions crash the program
   ```java
   Parent p = new Child();
   p.process(getValue());  // ✗ Unexpected exception!
   ```

2. **Logic Errors**: Methods behave differently for substituted classes
   ```java
   Parent p = new Child();
   int result = p.compute();  // ✗ Returns different value!
   ```

3. **Type Casting**: Code needs explicit type checks
   ```java
   if (parent instanceof SpecificChild) {
       SpecificChild child = (SpecificChild) parent;
       child.specificMethod();  // ✗ Not polymorphic
   }
   ```

4. **Hard to Maintain**: Future changes break substitubility

### Q15: How Do Real-World Frameworks Apply These Rules?
**A:** Examples from common frameworks:

**1. Collections Framework (Covariant Returns)**
```java
interface Iterator<T> {
    T next();
}

class StringIterator implements Iterator<String> {
    @Override
    public String next() { }  // ✓ More specific than Object
}
```

**2. Spring Framework (Exception Handling)**
```java
public interface DAO {
    void save(Object obj) throws DataAccessException;
}

public class HibernateDAO implements DAO {
    @Override
    public void save(Object obj) throws HibernateException { }  // ✗ Better: throw DataAccessException
}
```

**3. Servlet API (Arguments)**
```java
public abstract void doGet(HttpServletRequest req, HttpServletResponse resp) 
    throws ServletException, IOException;

// Subclass can only override with same signatures
public void doGet(HttpServletRequest req, HttpServletResponse resp) 
    throws ServletException, IOException { }
```

---

## Real-World Applications

### 1. Database Drivers
```java
interface Connection {
    Statement createStatement() throws SQLException;
}

// Different drivers (MySQL, Oracle, PostgreSQL)
class MySQLConnection implements Connection {
    @Override
    public MySQLStatement createStatement() throws SQLException { }  // ✓ Narrower return type
}
```

### 2. Payment Processors
```java
interface PaymentProcessor {
    void process(Payment payment) throws PaymentException;
}

class CreditCardProcessor implements PaymentProcessor {
    @Override
    public void process(Payment payment) throws CreditCardException { }  // ✓ Narrower exception
}
```

### 3. Serialization Framework
```java
interface Serializable {
    void writeObject(ObjectOutputStream oos) throws IOException;
}

// Subclasses maintain exception contract
class CustomSerializable implements Serializable {
    @Override
    public void writeObject(ObjectOutputStream oos) throws IOException { }  // ✓ Same exception
}
```

### 4. Event Listeners
```java
interface EventListener {
    void handle(Event event) throws EventProcessingException;
}

// Specific listeners maintain contract
class MouseEventListener implements EventListener {
    @Override
    public void handle(Event event) throws MouseEventException { }  // ✓ Narrower
}
```

---

## Best Practices Checklist

- [ ] **Return Types**: Child returns narrower (more specific) types than parent
- [ ] **Parameters**: Child accepts same or broader types, never narrower
- [ ] **Exceptions**: Child throws same or narrower exceptions, never broader
- [ ] **Invariants**: All class invariants from parent are maintained by child
- [ ] **Preconditions**: Child doesn't strengthen (require more) than parent
- [ ] **Postconditions**: Child doesn't weaken (guarantee less) than parent
- [ ] **No Type Checks**: Client code doesn't have instanceof checks
- [ ] **Polymorphic Safety**: Any substitution works without surprises
- [ ] **Contract Honored**: Method behavior matches parent's documentation
- [ ] **Backward Compatible**: Child is drop-in replacement for parent

---

## Common Mistakes and Solutions

### Mistake 1: Returning Broader Type
```java
// ✗ Wrong
class Parent {
    public Dog getDog() { return new Dog(); }
}

class Child extends Parent {
    @Override
    public Animal getDog() { return new Cat(); }  // ✗ Broader return type!
}

// ✓ Correct
class Child extends Parent {
    @Override
    public Dog getDog() { return new Dog(); }  // ✓ Same type
}

class BetterChild extends Parent {
    @Override
    public Labrador getDog() { return new Labrador(); }  // ✓ Narrower type
}
```

### Mistake 2: Narrowing Parameters
```java
// ✗ Wrong
class Parent {
    void handle(Object obj) { }
}

class Child extends Parent {
    @Override
    void handle(String str) { }  // ✗ Narrower parameter!
}

// ✓ Correct
class Child extends Parent {
    @Override
    void handle(Object obj) { }  // ✓ Same type
}

class BetterChild extends Parent {
    @Override
    void handle(Serializable s) { }  // ✓ Broader (more general)
}
```

### Mistake 3: Throwing Broader Exceptions
```java
// ✗ Wrong
class Parent {
    void read() throws FileNotFoundException { }
}

class Child extends Parent {
    @Override
    void read() throws IOException { }  // ✗ Broader exception!
}

// ✓ Correct
class Child extends Parent {
    @Override
    void read() throws FileNotFoundException { }  // ✓ Same exception
}

class BetterChild extends Parent {
    @Override
    void read() { }  // ✓ No exception (narrower technically)
}
```

### Mistake 4: Breaking Invariants
```java
// ✗ Wrong
class Parent {
    private int count = 0;  // Invariant: count >= 0
    
    void increment() {
        count++;
    }
}

class Child extends Parent {
    @Override
    void increment() {
        count--;  // ✗ Can violate invariant!
    }
}

// ✓ Correct
class Child extends Parent {
    @Override
    void increment() {
        count++;  // ✓ Maintains invariant
    }
}
```

---

## Key Takeaways

1. **LSP Signature Rules** ensure method overrides are substitutable
2. **Return Types are Covariant**: Narrower types allowed
3. **Parameters are Contravariant**: Broader types allowed
4. **Exceptions Matter**: Must be same or narrower
5. **Invariants are Binding**: Child must maintain parent's invariants
6. **Compiler Helps**: Some rules are checked by Java compiler
7. **Contract is Key**: Understanding the contract is essential
8. **Substitutability Test**: If substitution breaks, LSP is violated
9. **Real-World Impact**: Frameworks rely on these rules
10. **Design First**: Follow rules from the beginning, not as afterthought

---

