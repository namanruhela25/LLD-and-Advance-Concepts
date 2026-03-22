# LSP Property Rules - Deep Dive

This folder explores **LSP Property Rules** which focus on maintaining class properties and behavioral contracts through inheritance. These rules ensure that invariants and historical constraints are preserved when subclasses override parent behavior.

---

## Property Rules Overview

While **Signature Rules** focus on method signatures (return types, parameters, exceptions), **Property Rules** focus on the **semantic/logical properties** of classes:

### The Two Key Property Rules:

1. **Class Invariant Rule**: Maintain all constraints defined by the parent class
2. **History Constraint Rule**: Don't narrow or eliminate functionality that existed in parent

These rules are about **behavioral contracts** - what operations should be possible on an object.

---

## Rule 1: Class Invariant Rule

**File**: `ClassInvariant.java`

### What is a Class Invariant?

A **class invariant** is a condition that should always be true for objects of that class, before and after any method call.

### The Concept

```java
// BankAccount defines an invariant: balance >= 0
class BankAccount {
    protected double balance;  // Invariant: balance >= 0
    
    public BankAccount(double b) {
        if (b < 0) throw new IllegalArgumentException("Balance can't be negative");
        balance = b;
    }
    
    public void withdraw(double amount) {
        if (balance - amount < 0) 
            throw new RuntimeException("Insufficient funds");
        balance -= amount;  // ✓ Maintains: balance >= 0
    }
}
```

**The Invariant Contract**: "No BankAccount object should ever have negative balance"

### Violating the Invariant

```java
// ✗ VIOLATION - CheatAccount breaks the invariant
class CheatAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        balance -= amount;  // ✗ No check! Can become negative
        System.out.println("Remaining balance: " + balance);
    }
}

// Test the violation
BankAccount acc = new CheatAccount(5000);
acc.withdraw(7000);
System.out.println(acc.balance);  // ✗ Output: -2000.0
```

**Result**: The invariant is broken! Client code now can't trust that `balance >= 0`.

### Why This Violates LSP

```java
// Client code expects invariant to hold
public void checkBalance(BankAccount account) {
    if (account.balance >= 0) {
        System.out.println("Account is healthy");
    } else {
        System.out.println("Account is negative");  // Should never happen!
    }
}

// With CheatAccount, this can happen:
BankAccount account = new CheatAccount(5000);
account.withdraw(7000);
checkBalance(account);  // ✗ Output: "Account is negative" - Unexpected!
```

### Real-World Example: Stack Data Structure

```java
// Stack defines an invariant: size >= 0
abstract class Stack<T> {
    protected List<T> elements = new ArrayList<>();
    
    // Invariant: size >= 0
    public abstract void push(T element);
    public abstract T pop() throws EmptyStackException;
    public int size() { return elements.size(); }  // ✓ Always >= 0
}

// ✓ Correct implementation maintains invariant
class ProperStack<T> extends Stack<T> {
    @Override
    public void push(T element) {
        elements.add(element);  // ✓ Size increases
    }
    
    @Override
    public T pop() throws EmptyStackException {
        if (elements.isEmpty()) throw new EmptyStackException();
        return elements.remove(elements.size() - 1);  // ✓ Size decreases
    }
}

// ✗ Wrong implementation breaks invariant
class BadStack<T> extends Stack<T> {
    @Override
    public void push(T element) {
        elements.add(element);
    }
    
    @Override
    public T pop() throws EmptyStackException {
        if (elements.isEmpty()) throw new EmptyStackException();
        T element = elements.get(elements.size() - 1);
        // ✗ Forgot to remove! Size never decreases!
        return element;
    }
}
```

### Invariant Examples in Different Domains

| Domain | Class | Invariant |
|--------|-------|-----------|
| **Banking** | BankAccount | `balance >= 0` |
| **Data Structure** | Stack | `size >= 0` |
| **Authentication** | User | `id > 0 && username != null` |
| **Geometry** | Circle | `radius > 0` |
| **Collections** | HashMap | `size == keySet().size()` |
| **Queue** | Queue | `front != null OR size == 0` |

### How to Document Invariants

```java
/**
 * Represents a bank account.
 * 
 * CLASS INVARIANT:
 * - balance >= 0 (can never be negative)
 * - balance <= MAX_BALANCE (no overflow possible)
 * - accountNumber != null (always has valid ID)
 */
class BankAccount {
    protected double balance;
    protected String accountNumber;
    
    // Must maintain these invariants in ALL methods!
}
```

### Testing Invariants

```java
class BankAccountTest {
    private void assertInvariant(BankAccount acc) {
        assertEquals("Balance should be non-negative", 
            acc.balance >= 0, true);
    }
    
    @Test
    public void testInvariantAfterWithdraw() {
        BankAccount acc = new BankAccount(5000);
        acc.withdraw(1000);
        assertInvariant(acc);  // ✓ Invariant maintained
    }
    
    @Test
    public void testInvariantWithSubclass() {
        BankAccount acc = new ProperAccount(5000);
        acc.withdraw(1000);
        assertInvariant(acc);  // ✓ Should pass for all subclasses
    }
}
```

---

## Rule 2: History Constraint Rule

**File**: `HistoryConstraint.java`

### What is the History Constraint?

The **history constraint** states that:
- A subclass should not narrow or eliminate functionality that was available in the parent class
- If the parent allows an operation, the child should also allow it (not make it illegal)

### The Concept

```java
// Parent: BankAccount allows withdrawals
class BankAccount {
    protected double balance;
    
    public void withdraw(double amount) {
        if (balance - amount < 0) 
            throw new RuntimeException("Insufficient funds");
        balance -= amount;  // ✓ Operation allowed
    }
}

// ✗ VIOLATION - FixedDepositAccount disallows withdrawals
class FixedDepositAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        throw new RuntimeException("Withdrawals not allowed");  // ✗ Operation now forbidden!
    }
}
```

**The Contract**: "If account.withdraw() works for BankAccount, it must work for all subclasses"

### Why This Violates LSP

```java
// Client expects to withdraw from ANY BankAccount
public void transferFunds(BankAccount fromAccount, BankAccount toAccount, double amount) {
    fromAccount.withdraw(amount);  // ✗ May throw exception!
    toAccount.deposit(amount);
}

// With FixedDepositAccount, this breaks:
BankAccount from = new FixedDepositAccount(5000);
BankAccount to = new BankAccount(1000);

transferFunds(from, to, 500);  // ✗ CRASH! Can't withdraw from FixedDepositAccount
```

### Real-World Example: File Operations

```java
// Parent interface: File supports read and write
interface File {
    String read();      // Operation available
    void write(String content);  // Operation available
    void delete();      // Operation available
}

// ✓ Correct: ReadWriteFile maintains all operations
class ReadWriteFile implements File {
    @Override
    public String read() { /* ... */ }      // ✓ Supported
    
    @Override
    public void write(String content) { /* ... */ }  // ✓ Supported
    
    @Override
    public void delete() { /* ... */ }      // ✓ Supported
}

// ✗ Wrong: ReadOnlyFile narrows functionality
class ReadOnlyFile implements File {
    @Override
    public String read() { /* ... */ }      // ✓ Supported
    
    @Override
    public void write(String content) {
        throw new UnsupportedOperationException("Read-only file");  // ✗ Narrowed!
    }
    
    @Override
    public void delete() {
        throw new UnsupportedOperationException("Read-only file");  // ✗ Narrowed!
    }
}
```

### Difference Between File Types

Instead of narrowing functionality, use **separate hierarchies**:

```java
// ✓ Better design - separate interfaces by capability
interface ReadableFile {
    String read();
}

interface WritableFile extends ReadableFile {
    void write(String content);
}

interface DeletableFile extends WritableFile {
    void delete();
}

// Each implementation chooses what it supports
class ReadOnlyFile implements ReadableFile {
    @Override
    public String read() { /* ... */ }
}

class ReadWriteFile implements WritableFile {
    @Override
    public String read() { /* ... */ }
    @Override
    public void write(String content) { /* ... */ }
}

class MutableFile implements DeletableFile {
    @Override
    public String read() { /* ... */ }
    @Override
    public void write(String content) { /* ... */ }
    @Override
    public void delete() { /* ... */ }
}
```

### History Constraint vs Interface Segregation Principle

**History Constraint** (LSP): Parent functionality shouldn't be removed by child
```java
class Parent {
    void methodA() { /* works */ }
}

class Child extends Parent {
    @Override
    void methodA() {
        throw new Exception("Not supported");  // ✗ LSP violation
    }
}
```

**Interface Segregation** (ISP): Don't force classes to implement unwanted methods
```java
// ✗ Wrong - forces both to implement all methods
interface AllOperations {
    void read();
    void write();
    void delete();
}

class ReadOnlyFile implements AllOperations {
    void write() { throw new Exception(); }  // Forced!
}

// ✓ Right - only implements needed methods
interface Readable {
    void read();
}

class ReadOnlyFile implements Readable {
    void read() { /* ... */ }
}
```

---

## Comparing Both Property Rules

| Aspect | Class Invariant | History Constraint |
|--------|-----------------|-------------------|
| **Focuses On** | State constraints | Behavioral constraints |
| **Example** | `balance >= 0` | `withdraw()` always works |
| **Violation** | State becomes invalid | Operation becomes unavailable |
| **Impact** | Data corrupted | Client code breaks |
| **Prevention** | Validate in methods | Don't remove functionality |
| **Method** | Checks/assertions | Same or expanded behavior |

---

## Detailed Code Example Analysis

### ClassInvariant.java Breakdown

```java
// ✓ CORRECT - Parent maintains invariant
class BankAccount {
    protected double balance;
    
    public BankAccount(double b) {
        // ✓ Constructor validates: balance must be >= 0
        if (b < 0) throw new IllegalArgumentException("Balance can't be negative");
        balance = b;
    }
    
    public void withdraw(double amount) {
        // ✓ Method maintains invariant: result must be >= 0
        if (balance - amount < 0) 
            throw new RuntimeException("Insufficient funds");
        balance -= amount;
        // ✓ Postcondition: balance >= 0
    }
}

// ✗ VIOLATION - Child breaks invariant
class CheatAccount extends BankAccount {
    public CheatAccount(double b) {
        super(b);  // ✓ Constructor OK
    }
    
    @Override
    public void withdraw(double amount) {
        // ✗ No validation! Allows negative balance
        balance -= amount;
        // ✗ Postcondition violated: balance can be negative
    }
}

// Test
BankAccount acc = new CheatAccount(5000);
acc.withdraw(7000);
System.out.println(acc.balance);  // ✗ Output: -2000.0 (INVARIANT BROKEN!)
```

**Why This is Dangerous**:

```java
// Code that relies on invariant breaks
public void showStatus(BankAccount acc) {
    if (acc.balance < 0) {
        System.out.println("ERROR: Negative balance!");  // Shouldn't happen
    } else {
        System.out.println("Account OK");
    }
}

showStatus(new CheatAccount(5000));
// May output: "ERROR: Negative balance!" -> Unexpected!
```

### HistoryConstraint.java Breakdown

```java
// ✓ CORRECT - Parent defines operation
class BankAccount {
    protected double balance;
    
    public BankAccount(double b) {
        if (b < 0) throw new IllegalArgumentException("Balance can't be negative");
        this.balance = b;
    }
    
    // History Constraint: This operation MUST be available
    public void withdraw(double amount) {
        if (balance - amount < 0) 
            throw new RuntimeException("Insufficient funds");
        balance -= amount;  // ✓ Operation works
        System.out.println("Amount withdrawn. Remaining balance is " + balance);
    }
}

// ✗ VIOLATION - Child removes operation
class FixedDepositAccount extends BankAccount {
    public FixedDepositAccount(double b) {
        super(b);
    }
    
    // ✗ History constraint broken!
    // Parent said withdraw() is allowed
    // Child says withdraw() is NOT allowed
    @Override
    public void withdraw(double amount) {
        // ✗ Operation now forbidden!
        throw new RuntimeException("Withdraw not allowed in Fixed Deposit");
    }
}

// Test
BankAccount acc1 = new BankAccount(5000);
acc1.withdraw(2000);  // ✓ Works - outputs: "Amount withdrawn..."

BankAccount acc2 = new FixedDepositAccount(5000);
acc2.withdraw(2000);  // ✗ CRASH! Exception thrown instead of withdrawal
```

**Why This Breaks Polymorphism**:

```java
// Client code that works for BankAccount breaks for FixedDepositAccount
public void processAccounts(List<BankAccount> accounts, double amount) {
    for (BankAccount acc : accounts) {
        acc.withdraw(amount);  // ✗ May crash for some accounts!
    }
}

List<BankAccount> accounts = Arrays.asList(
    new BankAccount(5000),
    new FixedDepositAccount(5000),
    new BankAccount(3000)
);

processAccounts(accounts, 500);  // ✗ Crashes on FixedDepositAccount!
```

---

## How to Prevent Property Rule Violations

### Strategy 1: Strengthen Invariants, Don't Weaken

```java
// ✓ Good - Child strengthens invariant
class BankAccount {
    protected double balance;  // Invariant: balance >= 0
}

class PremiumAccount extends BankAccount {
    // Strengthened invariant: balance >= 1000
    // ✓ More restrictive is OK
    public void withdraw(double amount) {
        if (balance - amount < 1000)
            throw new RuntimeException("Must maintain minimum balance");
    }
}

// ✗ Wrong - Child weakens invariant
class CheatAccount extends BankAccount {
    // Weakened invariant: balance >= -999999
    public void withdraw(double amount) {
        balance -= amount;  // ✗ Allows negative balance
    }
}
```

### Strategy 2: Don't Remove Operations

```java
// ✓ Good approach - Keep all operations
abstract class Shape {
    abstract void draw();      // Operation 1
    abstract double getArea(); // Operation 2
}

class Circle extends Shape {
    @Override
    void draw() { /* ... */ }      // ✓ Supported
    @Override
    double getArea() { /* ... */ } // ✓ Supported
}

// ✗ Bad approach - Remove operations by throwing exception
class SpecialShape extends Shape {
    @Override
    void draw() {
        throw new Exception("Can't draw");  // ✗ Removed!
    }
    @Override
    double getArea() { /* ... */ }
}
```

### Strategy 3: Use Composition Instead of Inheritance

```java
// ✗ Wrong - Invalid inheritance
class ReadOnlyFile extends File {  // Inherits read, write, delete
    @Override
    void write(String content) {
        throw new UnsupportedOperationException();  // ✗ Removes operation
    }
}

// ✓ Right - Composition
class ReadOnlyFile {
    private String content;
    
    public String read() {
        return content;
    }
    
    // No write() method - doesn't pretend to support it!
}
```

### Strategy 4: Create Proper Hierarchy

```java
// ✗ Wrong - Single hierarchy with conflicting requirements
interface Account {
    void withdraw();
    void transfer();
}

class ReadOnlyAccount implements Account {
    void withdraw() { throw new Exception(); }  // ✗ Can't support
}

// ✓ Right - Separate hierarchies
interface DepositAccount {
    void deposit();
}

interface WithdrawableAccount extends DepositAccount {
    void withdraw();
}

interface TransferableAccount extends WithdrawableAccount {
    void transfer();
}

class FixedDeposit implements DepositAccount { }
class SavingsAccount implements WithdrawableAccount { }
class CurrentAccount implements TransferableAccount { }
```

---

## How to Test Property Rules

### Test 1: Invariant Testing

```java
public class InvariantTest {
    private void assertInvariant(BankAccount acc) {
        assertTrue("Balance must be >= 0", acc.balance >= 0);
    }
    
    @Test
    public void testInvariantHeldAcrossAllOperations() {
        BankAccount acc = new BankAccount(5000);
        assertInvariant(acc);
        
        acc.withdraw(1000);
        assertInvariant(acc);
        
        acc.deposit(500);
        assertInvariant(acc);
        
        // Invariant should hold for ANY subclass
        BankAccount subclass = new ProperSubclass(5000);
        subclass.withdraw(1000);
        assertInvariant(subclass);  // Should pass for all implementations
    }
}
```

### Test 2: Substitutability Testing

```java
public class SubstitutabilityTest {
    @Test
    public void testAllAccountTypesSupportsWithdraw() {
        BankAccount[] accounts = {
            new BankAccount(5000),
            new SavingsAccount(5000),
            new CheckingAccount(5000)
        };
        
        for (BankAccount acc : accounts) {
            // Every account should support withdraw
            // If this fails for any subclass, history constraint is violated
            acc.withdraw(100);
            assertTrue(acc.balance == 4900);
        }
    }
}
```

### Test 3: Behavioral Contract Testing

```java
public class BehaviorTest {
    @Test
    public void testWithdrawBehaviorConsistent() {
        BankAccount acc1 = new BankAccount(5000);
        BankAccount acc2 = new SubclassAccount(5000);
        
        double startBalance1 = acc1.balance;
        double startBalance2 = acc2.balance;
        
        acc1.withdraw(100);
        acc2.withdraw(100);
        
        // Both should behave the same way
        assertEquals(startBalance1 - 100, acc1.balance, 0.01);
        assertEquals(startBalance2 - 100, acc2.balance, 0.01);
    }
}
```

---

## Interview Questions and Answers

### Q1: What is a Class Invariant?
**A:** A class invariant is a condition that must **always be true** for objects of a class, throughout their lifetime.

```java
class Stack {
    private List<T> items = new ArrayList<>();
    
    // Invariants:
    // 1. items != null (never null)
    // 2. size >= 0 (never negative)
    // 3. size == items.size() (size matches list)
}
```

Every method (push, pop, peek) must preserve these invariants.

### Q2: What is the Difference Between Class Invariant and a Regular Validation?
**A:** 

**Regular Validation**: Check method parameters
```java
public void withdraw(double amount) {
    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
    }
    // ... proceed
}
```

**Class Invariant**: Condition on object state that should ALWAYS hold
```java
class BankAccount {
    private double balance;  // Invariant: balance >= 0
    
    public void withdraw(double amount) {
        // Trust the invariant: balance is guaranteed >= 0
        if (balance >= amount) {
            balance -= amount;  // Still maintain invariant
        }
    }
}
```

**Key Difference**: Validation checks one method call; invariants span entire object lifetime.

### Q3: How Does Breaking an Invariant Violate LSP?
**A:** LSP requires that subclasses maintain substitutability. If parent's invariant is broken by child:

```java
// Parent maintains: balance >= 0
class Parent {
    protected double balance = 5000;
}

// Child breaks invariant
class Child extends Parent {
    public void hack() {
        balance = -5000;  // ✗ Breaks invariant
    }
}

// Client code assumes invariant
public void process(Parent p) {
    assertTrue(p.balance >= 0);  // Assumes invariant
}

process(new Child());  // ✗ Assertion fails - LSP violated!
```

### Q4: What is the History Constraint?
**A:** The history constraint states that **a subclass should not narrow or eliminate functionality** that the parent class supports.

```java
// Parent: All accounts support withdraw
class Account {
    public void withdraw(double amount) { /* ... */ }
}

// ✗ Child narrows functionality
class FixedDeposit extends Account {
    @Override
    public void withdraw(double amount) {
        throw new Exception("Not allowed");  // ✗ Breaks history constraint
    }
}

// Client expects withdraw to work
public void transfer(Account from, Account to, double amount) {
    from.withdraw(amount);  // ✗ Fails for FixedDeposit
}
```

### Q5: Why is History Constraint Important?
**A:** Without history constraint, polymorphic code becomes unreliable:

```java
// This should work for ANY Account
public void dailyProcessing(List<Account> accounts) {
    for (Account acc : accounts) {
        acc.withdraw(10);  // ✓ Must work for all
    }
}

// If some subclass restricts withdraw(), code breaks
```

History constraint ensures operations remain available in subclasses.

### Q6: Can a Subclass Provide MORE Functionality Than Parent?
**A:** Yes, absolutely! The rule forbids **removing** functionality, not **adding** it:

```java
// Parent
class BankAccount {
    public void withdraw(double amount) { /* ... */ }
}

// ✓ Child CAN add more methods
class PremiumAccount extends BankAccount {
    public void withdraw(double amount) { /* ... */ }  // ✓ Still works
    
    // ✓ NEW functionality (adds, doesn't remove)
    public void getRewards() { /* ... */ }
    public void getPrivateAdvisor() { /* ... */ }
}
```

### Q7: What's the Difference Between Invariant Rule and History Constraint?
**A:**

| Aspect | Invariant Rule | History Constraint |
|--------|---|---|
| **About** | Object state | Object behavior |
| **Example** | `balance >= 0` | `withdraw()` must work |
| **Violation** | State becomes invalid | Operation throws exception |
| **Fix** | Add validation | Don't restrict operation |

```java
// Invariant: balance >= 0
// History Constraint: withdraw() works
class Account {
    protected double balance;
    public void withdraw(double amount) {
        if (balance < amount) throw new Exception();
        balance -= amount;
    }
}

// ✗ Violates invariant (state broken)
class BadChild1 extends Account {
    public void hack() { balance = -500; }
}

// ✗ Violates history constraint (operation broken)
class BadChild2 extends Account {
    public void withdraw(double amount) {
        throw new Exception("Not allowed");
    }
}
```

### Q8: How Do You Identify Invariant Violations?
**A:** Look for:

1. **No Validation in Subclass**
```java
class Parent {
    protected int value;
    public Parent(int v) {
        if (v < 0) throw new Exception();  // ✓ Parent validates
    }
}

class Child extends Parent {
    public Child(int v) {
        super(v);
        if (someCondition) value = -5;  // ✗ No validation!
    }
}
```

2. **Weaker Conditions in Subclass**
```java
class Parent {
    public void process(int x) {
        if (x <= 0) throw new Exception();  // x must be positive
    }
}

class Child extends Parent {
    public void process(int x) {  // ✗ No validation
        doSomething(x);  // Assumes x positive, but no check
    }
}
```

### Q9: How Do You Identify History Constraint Violations?
**A:** Look for:

1. **Throwing Exception in Overridden Method**
```java
class Parent {
    public void operation() { /* does something */ }
}

class Child extends Parent {
    @Override
    public void operation() {
        throw new Exception("Not supported");  // ✗ Violates constraint
    }
}
```

2. **Conditional Execution Based on Type**
```java
public void process(Parent p) {
    if (p instanceof SpecificChild) {
        // Skip operation for this type
    } else {
        p.operation();  // ✗ Shows constraint violation
    }
}
```

3. **No Implementation**
```java
class Child extends Parent {
    @Override
    public void operation() {
        // Empty or just logs
    }  // ✗ Operation doesn't work
}
```

### Q10: Can Invariants Change Between Parent and Child?
**A:** Yes, but only by **strengthening**, not weakening:

```java
// Parent: balance >= 0
class Parent {
    protected double balance;
}

// ✓ Child STRENGTHENS invariant: balance >= 1000
class Child extends Parent {
    public Child(double b) {
        if (b < 1000) throw new Exception("Min balance 1000");
    }
}

// ✗ Child WEAKENS invariant: balance >= -999
class BadChild extends Parent {
    public BadChild(double b) {
        if (b < -999) throw new Exception("Min balance -999");
    }  // ✗ Weakened!
}
```

### Q11: How Do Invariants Relate to Preconditions and Postconditions?
**A:**

```java
class BankAccount {
    // INVARIANT (always true): balance >= 0
    
    public void withdraw(double amount) {
        // PRECONDITION: amount > 0 && amount <= balance
        // Method body
        // POSTCONDITION: new balance = old balance - amount
        // POSTCONDITION: balance >= 0 (invariant maintained)
    }
}
```

- **Invariant**: Held before, during, and after method
- **Precondition**: Must be true BEFORE method
- **Postcondition**: Must be true AFTER method

### Q12: What Role Do Constructors Play in Maintaining Invariants?
**A:** Constructors establish the invariant for new objects:

```java
class BankAccount {
    protected double balance;  // Invariant: balance >= 0
    
    // ✓ Constructor ensures invariant is established
    public BankAccount(double initialBalance) {
        if (initialBalance < 0)
            throw new IllegalArgumentException("Balance can't be negative");
        this.balance = initialBalance;  // Invariant established
    }
    
    public void withdraw(double amount) {
        // Trust invariant here: balance >= 0
        // Maintain invariant: ensure result >= 0
    }
}

// ✗ Child breaks invariant in constructor
class BadChild extends BankAccount {
    public BadChild(double b) {
        super(b);
        this.balance = -1000;  // ✗ Breaks invariant!
    }
}
```

### Q13: How Should You Document Invariants?
**A:** Use clear documentation:

```java
/**
 * Represents a bank account.
 * 
 * CLASS INVARIANTS:
 * - balance >= 0 (never negative)
 * - balance <= 1_000_000_000 (no overflow)
 * - accountNumber != null (always valid)
 * - username != null && !username.isEmpty() (always has owner)
 * 
 * All public methods must preserve these invariants.
 * Subclasses may strengthen but NOT weaken these constraints.
 */
public class BankAccount {
    protected double balance;
    protected String accountNumber;
    protected String username;
    // ...
}
```

### Q14: What Happens When You Violate Property Rules in Real Code?
**A:** Several problems can occur:

1. **Data Corruption**
```java
BankAccount acc = new CheatAccount(1000);
acc.withdraw(5000);
// balance = -4000, violates accounting invariant
```

2. **Client Code Breaks**
```java
public void assumeValidBalance(BankAccount acc) {
    assertTrue(acc.balance >= 0);  // May fail!
}
```

3. **Difficult Debugging**
```java
// Balance is negative - but WHERE was it set?
// Hard to trace back to violation source
```

4. **Security Issues**
```java
// User could exploit violation
CheatAccount acc = new CheatAccount(1000);
acc.withdraw(999999);
// Creates massive phantom balance
```

### Q15: How Do You Ensure Property Rules in Your Code?
**A:** Multi-layered approach:

1. **Code Review**
   - Check constructors establish invariants
   - Check methods maintain invariants
   - Check subclasses don't restrict operations

2. **Unit Tests**
   - Test invariants hold after each operation
   - Test subclass substitutability
   - Test boundary conditions

3. **Documentation**
   - Document all invariants
   - Explain why they're necessary
   - Note any constraints for subclasses

4. **Design**
   - Use immutable fields where possible
   - Use access modifiers (private/protected)
   - Create proper hierarchy instead of forcing subclasses

5. **Assertions**
```java
class BankAccount {
    public void withdraw(double amount) {
        assert balance >= 0 : "Pre: balance should be >= 0";
        if (balance >= amount) {
            balance -= amount;
        }
        assert balance >= 0 : "Post: balance should be >= 0";
    }
}
```

---

## Real-World Applications

### 1. Database Connections
```java
interface Connection {
    // Invariant: connection is active (or throws exception)
    // History: close() must work, setAutoCommit() must work
    void close() throws SQLException;
    void setAutoCommit(boolean autoCommit) throws SQLException;
}

// All drivers maintain these invariants and support these operations
class MySQLConnection implements Connection { /* ... */ }
class OracleConnection implements Connection { /* ... */ }
```

### 2. Java Collections
```java
interface List<E> {
    // Invariant: size >= 0 && size == actual elements
    // History: add(), remove(), get() always work
    void add(E element);
    E remove(int index);
    E get(int index);
}

// All implementations maintain invariants
class ArrayList<E> implements List<E> { /* ... */ }
class LinkedList<E> implements List<E> { /* ... */ }
```

### 3. HTTP Requests
```java
interface HttpRequest {
    // Invariant: URL != null, method != null
    // History: getURL(), getMethod(), getHeaders() always work
    String getURL();
    String getMethod();
    Map<String, String> getHeaders();
}

// All implementations support same operations
class GET implements HttpRequest { /* ... */ }
class POST implements HttpRequest { /* ... */ }
```

---

## Best Practices Checklist

- [ ] **Identify Invariants**: Document what should always be true
- [ ] **Validate in Constructor**: Ensure invariants are established
- [ ] **Maintain in Methods**: All methods preserve invariants
- [ ] **Check in Subclasses**: Subclasses also maintain invariants
- [ ] **Don't Remove Operations**: History constraint - keep parent operations
- [ ] **Extend, Don't Restrict**: Add functionality, don't take it away
- [ ] **Test Invariants**: Unit tests verify invariants hold
- [ ] **Use Assertions**: Runtime checks for invariants
- [ ] **Document Clearly**: Document all invariants and constraints
- [ ] **Design Properly**: Create correct hierarchy, don't force subclasses

---

## Common Mistakes Summary

| Mistake | Example | Impact | Fix |
|---------|---------|--------|-----|
| **No Validation** | Child sets `balance = -1000` | Invariant broken | Always validate |
| **Weaker Validation** | Child removes checks | Invariant violated | Strengthen or keep same |
| **Throw Exception** | Child throws in overridden method | History constraint broken | Keep operation available |
| **Skip Operation** | Child's method is empty | Operation doesn't work | Implement fully |
| **Conditional Behavior** | Different action for subclass | Substitutability broken | Use correct inheritance |

---

## Conclusion

Property Rules ensure:

✓ **Class Invariant**: State remains valid throughout object lifetime
✓ **History Constraint**: Operations remain available in subclasses
✓ **Substitutability**: Any subclass works just like parent
✓ **Reliability**: Code can trust parent contract in subclasses
✓ **Maintainability**: No special handling needed for different subclasses

Remember: **If you can't substitute child for parent without breaking code, LSP is violated.**

