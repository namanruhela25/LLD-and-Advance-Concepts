# LSP Method Rules - Preconditions & Postconditions

This folder explores **LSP Method Rules** which focus on how method contracts (preconditions and postconditions) change in inheritance relationships. These rules ensure that method overrides maintain safe behavioral contracts for substitution.

---

## Method Rules Overview

**Method Rules** define how preconditions and postconditions should behave in inheritance:

### The Two Key Method Rules:

1. **Precondition Rule (Weakening)**: Subclass preconditions can be weakened (made more accepting), NOT strengthened
2. **Postcondition Rule (Strengthening)**: Subclass postconditions can be strengthened (guarantee more), NOT weakened

---

## Introduction to Preconditions and Postconditions

### What is a Precondition?

A **precondition** is a condition that **must be true BEFORE** a method is called for the method to work correctly.

```java
class User {
    // Precondition: password.length() >= 8
    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Min 8 characters");
        }
        // Method proceeds only if precondition is satisfied
    }
}

// Usage
User user = new User();
user.setPassword("short");      // ✗ Fails - precondition violated
user.setPassword("longenough"); // ✓ Works - precondition satisfied
```

### What is a Postcondition?

A **postcondition** is a condition that **must be true AFTER** a method completes for the method to have worked correctly.

```java
class Car {
    private int speed;
    
    // Postcondition: speed is reduced (speed_after < speed_before)
    public void brake() {
        speed -= 20;  // ✓ Postcondition satisfied
    }
    
    // Postcondition contract promises: after brake(), speed will decrease
}

// Usage
Car car = new Car();
int beforeSpeed = car.speed;
car.brake();
// Postcondition: car.speed < beforeSpeed
```

---

## Rule 1: Precondition Rule (Weakening)

**File**: `PreConditions.java`

### The Rule

A subclass method **cannot strengthen** its precondition - it can only **weaken** it.

**Why?** Because client code is written for the parent class precondition.

```java
// Parent: Requires password >= 8 characters
class User {
    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Min 8 chars");
        }
    }
}

// ✓ VALID - Weaker: Accepts >= 6 characters (more permissive)
class AdminUser extends User {
    @Override
    public void setPassword(String password) {
        if (password.length() < 6) {  // ✓ Weaker requirement
            throw new IllegalArgumentException("Min 6 chars");
        }
    }
}

// ✗ INVALID - Stronger: Requires >= 12 characters (less permissive)
class StrictUser extends User {
    @Override
    public void setPassword(String password) {
        if (password.length() < 12) {  // ✗ Strengthened precondition!
            throw new IllegalArgumentException("Min 12 chars");
        }
    }
}
```

### Why Strengthening Preconditions Violates LSP

```java
// Client code written for User's precondition (>= 8)
public void setupAccounts(List<User> users) {
    for (User user : users) {
        user.setPassword("12345678");  // ✓ Satisfies User precondition
    }
}

// If one of those users is actually StrictUser (>= 12):
List<User> users = Arrays.asList(
    new User(),
    new StrictUser(),  // Strengthened precondition!
    new User()
);

setupAccounts(users);  // ✗ CRASH! StrictUser requires >= 12 chars
```

### Valid Precondition Examples

```java
// Parent: accept positive numbers
class Calculator {
    public int divide(int numerator, int divisor) {
        if (divisor <= 0) throw new Exception("Divisor must be positive");
        return numerator / divisor;
    }
}

// ✓ VALID - Weaker: accept any number
class FlexibleCalculator extends Calculator {
    @Override
    public int divide(int numerator, int divisor) {
        if (divisor == 0) throw new Exception("Divisor can't be 0");
        return numerator / divisor;
    }
}

// ✓ VALID - Remove precondition entirely
class SuperFlexibleCalculator extends Calculator {
    @Override
    public int divide(int numerator, int divisor) {
        return divisor == 0 ? 0 : numerator / divisor;
    }
}

// ✗ INVALID - Strengthen: require > 100
class StrictCalculator extends Calculator {
    @Override
    public int divide(int numerator, int divisor) {
        if (divisor <= 0 || divisor > 100) {  // ✗ Strengthened!
            throw new Exception("1 < divisor <= 100");
        }
        return numerator / divisor;
    }
}
```

### Visual Representation

```
Precondition Hierarchy (CONTRAVARIANCE):
──────────────────────────────────────

Parent: Requires divisor > 0
    ↓
Acceptable for subclass:
    ├── divisor > 0        (✓ Same)
    ├── divisor != 0       (✓ Weaker)
    └── no requirement     (✓ Weaker)

NOT Acceptable for subclass:
    ├── divisor > 100      (✗ Stronger)
    └── divisor > 500      (✗ Stronger)
```

### Code Example: PreConditions.java

```java
class User {
    // Precondition: password.length() >= 8
    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Min 8 chars");
        }
    }
}

class AdminUser extends User {
    // ✓ Weakened precondition: now accepts >= 6
    @Override
    public void setPassword(String password) {
        if (password.length() < 6) {  // Weaker than parent's 8
            throw new IllegalArgumentException("Min 6 chars");
        }
    }
}

// Test
User user = new User();
User admin = new AdminUser();

admin.setPassword("12345");   // ✓ Works - AdminUser accepts >= 6
// If this was User, it would fail ✗

user.setPassword("1234567");  // ✓ Works - User requires >= 8
```

**Why this is OK**: AdminUser is MORE accepting than User, so any code that works with User will work with AdminUser.

---

## Rule 2: Postcondition Rule (Strengthening)

**File**: `PostCondition.java`

### The Rule

A subclass method **cannot weaken** its postcondition - it can only **strengthen** it.

**Why?** Because client code relies on parent class guarantees.

```java
// Parent: Guarantees speed will reduce after brake()
class Car {
    private int speed;
    
    // Postcondition: speed decreases
    public void brake() {
        speed -= 20;  // ✓ Postcondition guaranteed
    }
}

// ✓ VALID - Strengthen: Also increases charge when braking
class HybridCar extends Car {
    private int charge;
    
    // Postcondition: speed decreases (same as parent)
    // Postcondition: charge increases (additional guarantee)
    @Override
    public void brake() {
        speed -= 20;   // ✓ Original postcondition
        charge += 10;  // ✓ Stronger postcondition
    }
}

// ✗ INVALID - Weaken: Speed might not reduce
class BrokenCar extends Car {
    @Override
    public void brake() {
        // ✗ Doesn't guarantee speed reduction!
        System.out.println("Braking");  // May not reduce speed
    }
}
```

### Why Weakening Postconditions Violates LSP

```java
// Client code relies on Car's postcondition (speed reduces)
public void driveSafely(Car car) {
    int before = car.speed;
    car.brake();
    int after = car.speed;
    
    assert before > after : "Speed should reduce after brake";
}

// Works fine with Car:
driveSafely(new Car());  // ✓ Assertion passes

// Fails with BrokenCar (weakened postcondition):
driveSafely(new BrokenCar());  // ✗ Assertion fails!
```

### Valid Postcondition Examples

```java
// Parent: withdraw removes money from account
class BankAccount {
    protected double balance;
    
    // Postcondition: balance decreases
    public void withdraw(double amount) {
        balance -= amount;
    }
}

// ✓ VALID - Strengthen: Also reward loyalty points
class LoyaltyAccount extends BankAccount {
    protected int points;
    
    // Postcondition: balance decreases (same as parent)
    // Postcondition: points increase (stronger - more benefits)
    @Override
    public void withdraw(double amount) {
        balance -= amount;    // ✓ Original postcondition
        points += amount / 10; // ✓ Stronger postcondition
    }
}

// ✗ INVALID - Weaken: Balance might not change
class BadAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        // ✗ Doesn't guarantee balance decreases!
        System.out.println("Withdrawing: " + amount);
    }
}
```

### Visual Representation

```
Postcondition Hierarchy (COVARIANCE):
────────────────────────────────────

Parent: Guarantees speed < before
    ↓
Acceptable for subclass:
    ├── speed < before             (✓ Same)
    ├── speed < before AND charge++  (✓ Stronger)
    └── speed < before AND ecoPts++  (✓ Stronger)

NOT Acceptable for subclass:
    ├── speed might not change    (✗ Weaker)
    └── speed might increase      (✗ Weaker)
```

### Code Example: PostCondition.java

```java
class Car {
    protected int speed;
    
    public void brake() {
        System.out.println("Applying brakes");
        speed -= 20;  // ✓ Postcondition: speed reduces
    }
}

class HybridCar extends Car {
    private int charge;
    
    // ✓ Strengthened postcondition
    @Override
    public void brake() {
        System.out.println("Applying brakes");
        speed -= 20;   // ✓ Original postcondition maintained
        charge += 10;  // ✓ Additional guarantee
    }
}

// Test
Car car = new Car();
Car hybrid = new HybridCar();

car.brake();    // ✓ Speed reduces only
hybrid.brake(); // ✓ Speed reduces AND charge increases (stronger)
```

**Why this is OK**: HybridCar provides MORE guarantees than Car, so code expecting Car's guarantees will still work.

---

## Comparing Both Method Rules

| Aspect | Precondition | Postcondition |
|--------|--------------|---------------|
| **Nature** | What must be true BEFORE | What must be true AFTER |
| **Variance Type** | Contravariant | Covariant |
| **Can Be** | Weakened ✓ | Strengthened ✓ |
| **Cannot Be** | Strengthened ✗ | Weakened ✗ |
| **Example (Valid)** | Accept more inputs | Guarantee more results |
| **Example (Invalid)** | Accept fewer inputs | Guarantee less results |

### Visual Contract Diagram

```
METHOD CONTRACT

┌─────────────────────────────────────────────────────┐
│                  METHOD EXECUTION                   │
├─────────────────────────────────────────────────────┤
│                                                     │
│  BEFORE: Precondition (input/state requirement)    │
│          ├─ Parent: x > 0                          │
│          ├─ ✓ Child can weaken: x != 0            │
│          └─ ✗ Child cannot strengthen: x > 100    │
│                                                     │
│  PROCESS: Method logic                             │
│                                                     │
│  AFTER: Postcondition (result/state guarantee)     │
│          ├─ Parent: result > 0                     │
│          ├─ ✓ Child can strengthen: result > 100  │
│          └─ ✗ Child cannot weaken: no guarantee   │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## How to Run the Examples

```bash
# Navigate to directory
cd /solid/liskov_substitution_principle/LSP_rules/MethodRules

# Compile all files
javac *.java

# Run Precondition example
java solid.liskov_substitution_principle.LSP_rules.MethodRules.PreConditions
# Output (no exception - AdminUser weakens precondition):
# Password set successfully
# Password set successfully

# Run Postcondition example
java solid.liskov_substitution_principle.LSP_rules.MethodRules.PostCondition
# Output (shows both speed and charge changes):
# Accelerating
# Current speed: 20
# Current charge: 0
# Applying brakes
# Current speed: 0
# Current charge: 10
```

---

## Interview Questions and Answers

### Q1: What's the Difference Between Precondition and Postcondition?
**A:**

**Precondition**: Condition that must be TRUE BEFORE method executes
```java
public void divide(int divisor) {
    if (divisor == 0) throw Exception();  // Precondition: divisor != 0
    return 100 / divisor;
}
```

**Postcondition**: Condition that must be TRUE AFTER method completes
```java
public void withdraw(double amount) {
    balance -= amount;
    // Postcondition: balance decreased by 'amount'
}
```

### Q2: Why Can't Preconditions Be Strengthened in Subclasses?
**A:** Because client code is written for parent's precondition:

```java
// Client code written for User (requires >= 8 chars)
public void createAccount(User user) {
    user.setPassword("12345678");  // Meets User's precondition
}

// If actual object is StrictUser (requires >= 12 chars):
User user = new StrictUser();
createAccount(user);  // ✗ CRASH! "12345678" doesn't meet StrictUser's requirement
```

**Solution**: Subclasses must ONLY weaken preconditions (accept more inputs).

### Q3: Why Can't Postconditions Be Weakened in Subclasses?
**A:** Because client code relies on parent's guarantee:

```java
// Client code relies on Car (guarantees speed reduces)
public void checkBraking(Car car) {
    int before = car.speed;
    car.brake();
    assert car.speed < before : "Speed must reduce!";  // Expects this guarantee
}

// If actual object is BrokenCar (doesn't guarantee speed reduces):
Car car = new BrokenCar();
checkBraking(car);  // ✗ Assertion fails! BrokenCar violates expectation
```

**Solution**: Subclasses must ONLY strengthen postconditions (provide more guarantees).

### Q4: Is It OK for a Subclass to Add Extra Postconditions?
**A:** Yes! Adding extra postconditions STRENGTHENS the contract:

```java
// Parent
class Car {
    public void brake() {
        speed -= 20;  // Postcondition 1: speed decreases
    }
}

// ✓ Child adds extra postcondition (strengthens contract)
class HybridCar extends Car {
    @Override
    public void brake() {
        speed -= 20;       // ✓ Original postcondition maintained
        charge += 10;      // ✓ Extra postcondition (strengthens)
    }
}

// Client gets more guarantees - this is fine!
```

### Q5: What Does Contravariance Mean for Preconditions?
**A:** Contravariance for preconditions means they must be "turned around":

```
Parent precondition: "x must be positive"
Child can override with:
  ✓ "x must be positive" (same - contravariant same)
  ✓ "x must be non-zero" (weaker - less restrictive)
  ✓ "x can be anything" (weaker - no restriction)
  
  ✗ "x must be > 100" (stronger - more restrictive)
```

**Memory Aid**: Preconditions are CONTRAVARIANT = they go the opposite direction in inheritance.

### Q6: What Does Covariance Mean for Postconditions?
**A:** Covariance for postconditions means they go in the same direction:

```
Parent postcondition: "result > 0"
Child can override with:
  ✓ "result > 0" (same - covariant same)
  ✓ "result > 100" (stronger - more guarantee)
  ✓ "result > 100 AND result is even" (stronger - more guarantee)
  
  ✗ "result >= 0" (weaker - less guarantee)
  ✗ "result can be anything" (weaker - no guarantee)
```

**Memory Aid**: Postconditions are COVARIANT = they go the same direction in inheritance.

### Q7: Real-World Example: Database Queries
**A:**

```java
// Parent: Query requires non-empty table name
interface Database {
    // Precondition: tableName != null && !tableName.isEmpty()
    List<Record> query(String tableName) {
        if (tableName == null) throw new Exception("Table name required");
        // Postcondition: returns records from table (possibly empty list)
        return records;
    }
}

// ✓ MySQL relaxes precondition (accepts null, uses default table)
class MySQLDatabase implements Database {
    @Override
    public List<Record> query(String tableName) {
        String table = tableName != null ? tableName : "DEFAULT";  // Weaker precondition
        // Postcondition: returns records (possibly cached for performance)
        return records;
    }
}

// ✓ PostgreSQL strengthens postcondition (guarantees sorted results)
class PostgreSQLDatabase implements Database {
    @Override
    public List<Record> query(String tableName) {
        if (tableName == null) throw new Exception("Table name required");
        List<Record> records = records;
        Collections.sort(records);  // Stronger postcondition - guarantees sorting
        return records;
    }
}
```

### Q8: Can a Method Remove Its Precondition Entirely?
**A:** Yes! Removing precondition is the ultimate weakening:

```java
// Parent requires validation
class User {
    public void setAge(int age) {
        if (age < 0) throw new Exception("Age must be >= 0");
        this.age = age;
    }
}

// ✓ Child removes precondition (doesn't check age)
class FlexibleUser extends User {
    @Override
    public void setAge(int age) {
        this.age = age;  // ✓ Weaker - no precondition check
    }
}

// Client code works for both:
User user = new FlexibleUser();
user.setAge(-5);  // ✓ Works (FlexibleUser doesn't validate)
```

### Q9: Can a Method Remove Its Postcondition?
**A:** No! Removing postcondition is weakening - violates LSP:

```java
// Parent guarantees balance decreases
class BankAccount {
    public void withdraw(double amount) {
        balance -= amount;  // Guarantees: balance decreases
    }
}

// ✗ Child removes postcondition (doesn't guarantee balance changes)
class FakeBankAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawing: " + amount);  // ✗ Doesn't change balance
    }
}

// Client code breaks:
BankAccount acc = new FakeBankAccount();
double before = acc.balance;
acc.withdraw(100);
assert acc.balance < before;  // ✗ Fails - postcondition not met
```

### Q10: How Do You Identify Precondition Violations?
**A:** Look for stricter checks in child methods:

```java
// Parent precondition: x > 0
class Parent {
    public void process(int x) {
        if (x <= 0) throw new Exception();
    }
}

// ✗ Child strengthens precondition: x > 100
class BadChild extends Parent {
    @Override
    public void process(int x) {
        if (x <= 100) throw new Exception();  // ✗ Stricter than parent
    }
}

// Client code breaks:
Parent p = new BadChild();
p.process(50);  // ✗ Throws exception - precondition strengthened
```

### Q11: How Do You Identify Postcondition Violations?
**A:** Look for fewer guarantees in child methods:

```java
// Parent postcondition: result > 0
class Parent {
    public int calculate() {
        return 100;  // Guarantees result > 0
    }
}

// ✗ Child weakens postcondition: result can be anything
class BadChild extends Parent {
    @Override
    public int calculate() {
        return new Random().nextInt();  // ✗ Can be negative
    }
}

// Client code breaks:
Parent p = new BadChild();
int result = p.calculate();
assert result > 0;  // ✗ Fails - postcondition weakened
```

### Q12: Can a Subclass Combine Pre and Postcondition Changes?
**A:** Yes, as long as you follow the rules:

```java
class Parent {
    // Precondition: x > 0
    // Postcondition: result > 0
    public int divide(int x) {
        if (x <= 0) throw new Exception("x must be positive");
        return 1000 / x;
    }
}

// ✓ Valid: Weaken precondition AND strengthen postcondition
class Child extends Parent {
    @Override
    public int divide(int x) {
        if (x == 0) throw new Exception("x can't be 0");  // ✓ Weaker
        int result = 1000 / x;
        if (result < 1) result = 1;  // ✓ Ensure result > 1 (stronger)
        return result;
    }
}

// ✓ Both changes are valid independently
```

### Q13: What's an Example from Java Collections?
**A:**

```java
// Iterator precondition: next() requires hasNext() == true
interface Iterator<T> {
    boolean hasNext();
    // Precondition: hasNext() must be true
    T next() throws NoSuchElementException;
}

// ArrayIterator weakens nothing (follows contract strictly)
class ArrayIterator<T> implements Iterator<T> {
    @Override
    public T next() throws NoSuchElementException {
        if (!hasNext()) throw new NoSuchElementException();
        return array[index++];
    }
}

// ListIterator strengthens postcondition (adds previous element info)
interface ListIterator<T> extends Iterator<T> {
    // Precondition: same as Iterator
    // Postcondition: same as Iterator + provides hasPrevious() and previous()
    boolean hasPrevious();
    T previous();
}
```

### Q14: How Do Preconditions Affect Testing?
**A:** Tests must verify precondition behavior:

```java
@Test
public void testPreconditionRule() {
    User parent = new User();
    User child = new AdminUser();
    
    // Parent requires >= 8 chars
    parent.setPassword("12345678");  // ✓ Works
    // parent.setPassword("short");   // ✗ Would fail
    
    // Child requires >= 6 chars (weaker)
    child.setPassword("123456");     // ✓ Works (weaker precondition)
    
    // Both should work with parent's requirement
    parent.setPassword("12345678");  // ✓ Parent works
    child.setPassword("12345678");   // ✓ Child also works (accepts more)
}
```

### Q15: Can Precondition/Postcondition Rules Be Violated Accidentally?
**A:** Yes, and it's common. Example:

```java
// Parent
class Parent {
    public int getValue() {
        return 10;  // Postcondition: always returns positive
    }
}

// ✗ Accidentally weakens postcondition
class Buggy extends Parent {
    @Override
    public int getValue() {
        return Math.random() > 0.5 ? 10 : -10;  // ✗ May return negative!
    }
}

// ✗ Accidentally strengthens precondition
class StrictLogin extends LoginService {
    @Override
    public void login(String username) {
        if (username.length() < 10) {
            throw new Exception("Username too short!");  // ✗ Strengthened!
        }
    }
}
```

---

## Real-World Applications

### 1. Payment Processing
```java
interface PaymentProcessor {
    // Precondition: amount > 0
    // Postcondition: payment recorded and transaction ID returned
    String processPayment(double amount) throws PaymentException;
}

// Stripe implementation weakens precondition (accepts $0 for testing)
class StripeProcessor implements PaymentProcessor {
    @Override
    public String processPayment(double amount) throws PaymentException {
        if (amount < 0) throw new Exception();  // ✓ Weaker: accepts 0
        // Postcondition: payment recorded
    }
}

// Secure implementation strengthens postcondition (ensures audit log)
class SecureProcessor implements PaymentProcessor {
    @Override
    public String processPayment(double amount) throws PaymentException {
        if (amount <= 0) throw new Exception();
        String id = process(amount);
        auditLog.record(id, amount);  // ✓ Stronger: guaranteed audit
        return id;
    }
}
```

### 2. File Operations
```java
interface FileWriter {
    // Precondition: content != null
    // Postcondition: file written to disk
    void write(String filename, String content) throws IOException;
}

// Cloud storage relaxes precondition (accepts null = clear file)
class CloudFileWriter implements FileWriter {
    @Override
    public void write(String filename, String content) throws IOException {
        String toWrite = content != null ? content : "";  // ✓ Weaker
        upload(filename, toWrite);
    }
}

// Cached implementation strengthens postcondition (ensures cache updated)
class CachedFileWriter implements FileWriter {
    @Override
    public void write(String filename, String content) throws IOException {
        if (content == null) throw new Exception();
        write(filename, content);
        cache.put(filename, content);  // ✓ Stronger: guaranteed cache
    }
}
```

---

## Best Practices Checklist

- [ ] **Preconditions**: Weaken when overriding, never strengthen
- [ ] **Postconditions**: Strengthen when overriding, never weaken
- [ ] **Document**: Clearly document all pre and postconditions
- [ ] **Test**: Test with boundary values for conditions
- [ ] **Substitutability**: Verify child works wherever parent is used
- [ ] **Inheritance Design**: Ensure preconditions/postconditions make sense in hierarchy
- [ ] **Code Review**: Check for accidental pre/postcondition violations
- [ ] **Assertions**: Use assertions to document and validate conditions
- [ ] **Consistency**: Ensure all implementations honor the contract

---

## Common Mistakes and Solutions

| Mistake | Example | Impact | Fix |
|---------|---------|--------|-----|
| **Strengthen Precondition** | Check `x > 100` when parent checks`x > 0` | Breaks client code | Weaken or keep same |
| **Weaken Postcondition** | Don't guarantee result is positive | Breaks client code | Strengthen or keep same |
| **Add Precondition** | Add new input requirement not in parent | Violates contract | Remove or move to precondition rule |
| **Remove Postcondition** | Stop guaranteeing state change | Violates substitutability | Keep guarantee or strengthen |
| **Unclear Contract** | Don't document pre/postconditions | Violations by accident | Document clearly |

---

## Conclusion

Method Rules ensure safe substitution through proper pre and postcondition handling:

✓ **Precondition Weakening**: Accept more inputs than parent
✓ **Postcondition Strengthening**: Guarantee more than parent
✓ **LSP Compliance**: Subclasses maintain contract for client code
✓ **Substitutability**: Any subclass works in place of parent
✓ **Safe for Developers**: Clear rules prevent accidents

Remember:
- **Preconditions** use **CONTRAVARIANCE** (loosen them)
- **Postconditions** use **COVARIANCE** (strengthen them)
- **Violating either rule breaks LSP and polymorphism**

