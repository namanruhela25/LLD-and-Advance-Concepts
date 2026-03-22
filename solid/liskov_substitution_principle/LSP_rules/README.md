# LSP Rules - Complete Guide

This folder contains a **comprehensive breakdown of the Liskov Substitution Principle (LSP) Rules**. Understanding these rules is essential for writing maintainable, substitutable code where subclasses can safely replace parent classes without breaking functionality.

---

## What is Liskov Substitution Principle (LSP)?

**Definition**: Objects of a superclass should be replaceable with objects of its subclasses without breaking the application.

In simpler terms: **If a program can use a Parent class, it should work perfectly fine when you substitute it with any Child class.**

---

## The Three Categories of LSP Rules

LSP compliance is enforced through three interdependent rule categories:

### 1. **Signature Rules** 📋
Rules that govern **method signatures** when overriding parent methods.

**Files**: `ExceptionRules.java`, `MethodArgumentRule.java`, `ReturnTypeRule.java`

**Key Rules**:
- **Return Type Rule**: Child can return narrower (more specific) types - **COVARIANCE**
- **Method Argument Rule**: Child must accept same or broader parameter types - **CONTRAVARIANCE**
- **Exception Rule**: Child throws same or narrower exceptions - **NO BROADENING**

**Example**: 
```java
class Parent {
    public Animal getAnimal() { }      // Returns Animal
    public void process(Object obj) { } // Accepts Object
    void risky() throws IOException { } // Throws IOException
}

class Child extends Parent {
    @Override
    public Dog getAnimal() { }              // ✅ Narrower return type (Dog is-an Animal)
    @Override
    public void process(Serializable s) { } // ❌ WRONG - narrower parameter
    @Override
    void risky() throws FileNotFoundException { } // ✅ Narrower exception
}
```

---

### 2. **Property Rules** 🏗️
Rules that govern **semantic/logical properties** like invariants and functional constraints.

**Files**: `ClassInvariant.java`, `HistoryConstraint.java`

**Key Rules**:
- **Class Invariant Rule**: Maintain all constraints from parent class - ✅ **STRENGTHEN OR KEEP SAME**
- **History Constraint Rule**: Don't remove/narrow functionality - ✅ **KEEP ALL OPERATIONS**

**Example**:
```java
class BankAccount {
    protected double balance;  // Invariant: balance >= 0
    public void withdraw(double amount) { }  // Operation available
}

class CheatAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        balance -= amount;  // ❌ WRONG - can make balance negative (breaks invariant)
    }
}

class FixedDepositAccount extends BankAccount {
    @Override
    public void withdraw(double amount) {
        throw new Exception("Not allowed");  // ❌ WRONG - removes operation (history constraint)
    }
}
```

---

### 3. **Method Rules** 🔧
Rules that govern **method contracts** (preconditions and postconditions).

**Files**: `PreConditions.java`, `PostCondition.java`

**Key Rules**:
- **Precondition Rule**: Can weaken (be more accepting), NOT strengthen - **CONTRAVARIANCE**
- **Postcondition Rule**: Can strengthen (guarantee more), NOT weaken - **COVARIANCE**

**Example**:
```java
class User {
    public void setPassword(String pw) {
        if (pw.length() < 8) throw new Exception();  // Precondition: >= 8 chars
    }
}

class AdminUser extends User {
    @Override
    public void setPassword(String pw) {
        if (pw.length() < 6) throw new Exception();  // ✅ Weaker precondition (>= 6)
    }
}

class Car {
    public void brake() {
        speed -= 20;  // Postcondition: speed reduces
    }
}

class HybridCar extends Car {
    @Override
    public void brake() {
        speed -= 20;   // ✅ Original postcondition kept
        charge += 10;  // ✅ Additional guarantee (strengthened)
    }
}
```

---

## How These Rules Work Together

```
LSP COMPLIANCE CHECKLIST
├── Signature Rules (Method Signature Level)
│   ├── Return types: Covariant (narrower OK)
│   ├── Parameters: Contravariant (broader OK)
│   └── Exceptions: Narrower OK
│
├── Property Rules (Object State & Behavior)
│   ├── Invariants: Maintain or strengthen
│   └── Operations: Don't remove functionality
│
└── Method Rules (Contract Level)
    ├── Preconditions: Weaken or keep same
    └── Postconditions: Strengthen or keep same
```

---

## Quick Rule Summary Table

| Rule Category | Rule Name | Can | Cannot | Variance |
|---------------|-----------|-----|--------|----------|
| **Signature** | Return Type | Narrow ✅ | Broaden ❌ | Covariant |
| **Signature** | Parameters | Broaden ✅ | Narrow ❌ | Contravariant |
| **Signature** | Exceptions | Narrow ✅ | Broaden ❌ | - |
| **Property** | Invariants | Strengthen ✅ | Weaken ❌ | - |
| **Property** | Operations | Add ✅ | Remove ❌ | - |
| **Method** | Preconditions | Weaken ✅ | Strengthen ❌ | Contravariant |
| **Method** | Postconditions | Strengthen ✅ | Weaken ❌ | Covariant |

---

## Why These Rules Matter

### Without LSP Rules
```java
// Code breaks unexpectedly
List<BankAccount> accounts = new ArrayList<>();
accounts.add(new BankAccount(5000));
accounts.add(new FixedDepositAccount(5000));

// This crashes on FixedDepositAccount:
for (BankAccount acc : accounts) {
    acc.withdraw(100);  // ❌ Exception for FixedDepositAccount!
}
```

### With LSP Rules (Proper Design)
```java
// Code works predictably
List<WithdrawableAccount> withdrawable = new ArrayList<>();
List<DepositOnlyAccount> depositOnly = new ArrayList<>();

withdrawable.add(new SavingsAccount(5000));
depositOnly.add(new FixedDepositAccount(5000));

// Both work as expected:
for (WithdrawableAccount acc : withdrawable) {
    acc.withdraw(100);  // ✅ Always works
}

for (DepositOnlyAccount acc : depositOnly) {
    acc.deposit(100);   // ✅ Always works
}
```

---

## How to Use This Folder

1. **Start with SignatureRules** - Understand method signature contracts
   - Learn about return type covariance
   - Understand parameter contravariance
   - See how exceptions work

2. **Then PropertyRules** - Understand object state and behavior
   - Learn about class invariants
   - Understand history constraints
   - See how to maintain consistency

3. **Finally MethodRules** - Understand method contracts
   - Learn about precondition weakening
   - Understand postcondition strengthening
   - See real-world contract examples

4. **Review & Practice** - Read the comprehensive READMEs
   - Each subfolder has detailed README with 15+ interview Q&A
   - Study the code examples
   - Practice implementing LSP-compliant designs

---

## Interview Preparation

When asked about **LSP in interviews**, be prepared to discuss:

**Understand the Big Picture**
- LSP ensures substitutability
- Enables safe polymorphism
- Prevents runtime surprises

**Know the Three Categories**
- Signature Rules (return types, parameters, exceptions)
- Property Rules (invariants, operations)
- Method Rules (preconditions, postconditions)

**Provide Examples**
- Bank accounts (signature rules)
- File operations (property rules)
- Password validation (method rules)

**Explain the Impact**
- What happens when LSP is violated
- Why it matters for team projects
- How to identify violations

---

## Key Takeaways

| Concept | Remember |
|---------|----------|
| **Substitutability** | Child must work everywhere parent works |
| **Covariance** | Return types can be narrower (more specific) |
| **Contravariance** | Parameters and preconditions can be broader |
| **Invariants** | State constraints should be maintained |
| **Operations** | Don't remove functionality from parent |
| **Preconditions** | Weaken them or keep same in child |
| **Postconditions** | Strengthen them or keep same in child |

---

## Quick Navigation

- 📋 **[SignatureRules/README.md](SignatureRules/README.md)** - Method signature rules with 15+ Q&A
- 🏗️ **[PropertyRules/README.md](PropertyRules/README.md)** - Object property rules with 15+ Q&A
- 🔧 **[MethodRules/README.md](MethodRules/README.md)** - Method contract rules with 15+ Q&A

---

## Practice Exercises

Try implementing these while following LSP rules:

1. **Payment System**: Different payment methods (credit card, PayPal, crypto)
   - Ensure each can be substituted for others
   - Check signature, property, and method rules

2. **Data Structures**: Stack, Queue, List implementations
   - Maintain class invariants
   - Keep all operations available
   - Honor method contracts

3. **Vehicle System**: Different vehicle types (Car, Truck, Bike)
   - Ensure substitutability
   - Check what assumptions the caller makes
   - Ensure those hold for all subclasses

---

## Important Note

LSP is about **making implicit contracts explicit** and **ensuring those contracts are honored**. The rules help you avoid costly runtime errors and make your code more maintainable.

**Remember**: A well-designed class hierarchy makes LSP violations obvious through the type system. If you find yourself using `instanceof` checks or try-catch blocks to handle different subclasses, it's a sign LSP might be violated.

