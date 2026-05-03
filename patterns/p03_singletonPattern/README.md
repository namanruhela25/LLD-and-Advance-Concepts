# Singleton Design Pattern

## Definition

The **Singleton Design Pattern** is a creational design pattern that ensures a class has only **one instance** throughout the application's lifetime and provides a **global point of access** to that instance.

## Purpose

The Singleton pattern is used when:
- You need exactly one object of a class at any given time
- You want a global point of access to that instance
- You need to control object instantiation
- You want lazy or eager initialization of shared resources
- Multiple threads need to access the same resource safely

## Key Characteristics

| Feature | Details |
|---------|---------|
| **Private Constructor** | Prevents instantiation from outside the class |
| **Static Instance** | Holds the single instance of the class |
| **Static Method** | Provides global access via `getInstance()` |
| **Thread Safety** | Needed for multi-threaded environments |
| **Memory** | Single object persists throughout application lifetime |

## Implementations & Interview Details

### 1. Basic Singleton Pattern (Naive / Not Thread-Safe)

**File:** `SingletonPattern.java`

**Implementation:**
```
- Private constructor prevents external instantiation
- Static instance variable holds the single object
- getInstance() returns the instance (lazily initialized)
- NO synchronization mechanism
```

**Problems:**
- **Race Condition**: In multi-threaded environments, two threads can enter `if(instance == null)` simultaneously
- **Multiple Instances Created**: Both threads will create separate instances, violating the singleton principle
- **NOT recommended** for production code with concurrent access

**Time Complexity:** O(1)  
**Space Complexity:** O(1)

**Interview Point:** *"This is the most basic Singleton. It's not thread-safe and can create multiple instances in concurrent environments due to a race condition in the instantiation check."*

---

### 2. Thread-Safe Singleton with Full Synchronization (Synchronized Method)

**File:** `ThreadSafeLocking.java`

**Implementation:**
```
- Private constructor prevents instantiation
- Entire getInstance() method is synchronized
- Lock is acquired on the class object: synchronized(ThreadSafeLocking.class)
- Every method call acquires and releases lock
```

**Advantages:**
- **Thread-Safe**: Guarantees only one instance is created
- **Lazy Initialization**: Instance created only when first accessed
- **Simple**: Easy to understand and implement

**Disadvantages:**
- **Performance Bottleneck**: EVERY call to `getInstance()` acquires a lock
- **Unnecessary Contention**: After first instantiation, lock is still acquired on every call
- **Scalability Issue**: In high-concurrency scenarios, threads queue up waiting for the lock
- **Not Efficient**: Slows down application as synchronization has overhead

**Time Complexity:** O(1)  
**Space Complexity:** O(1)  
**Lock Behavior:** Acquired on EVERY call (inefficient)

**Interview Point:** *"While this is thread-safe, it has a major performance issue. The synchronization happens on every call, even after the instance is created. This creates unnecessary contention and slows down the application."*

---

### 3. Thread-Safe Singleton with Double-Checked Locking (DCL)

**File:** `ThreadSafeDoubleLocking.java`

**Implementation:**
```
- Private constructor
- Static volatile instance variable
- Two checks:
  1. First check (unsynchronized) - outside the lock
  2. Second check (synchronized) - inside the lock
- Lock acquired only when instance is null
```

**How It Works:**
1. First thread calls `getInstance()` → checks `if(instance == null)` (true) → enters synchronized block
2. Acquires lock → checks again `if(instance == null)` (true) → creates instance → releases lock
3. Second thread calls `getInstance()` → checks `if(instance == null)` (false) → returns without entering synchronized block
4. Subsequent threads never enter the synchronized block

**Advantages:**
- **Thread-Safe**: Guarantees only one instance
- **Performance Optimized**: Lock acquired only during first initialization
- **Lazy Initialization**: Instance created on first access
- **Efficient**: After initialization, no lock overhead

**Disadvantages:**
- **Complex**: Requires understanding of visibility and memory barriers
- **Volatile Keyword Required**: Must use `volatile` for thread visibility (though not shown in code, it's important)
- **Java-Specific**: Implementation details vary across languages
- **JVM-Specific Issues**: Prior to Java 5, DCL had memory visibility issues with the JVM

**Lock Behavior:**
- Only acquired when `instance == null`
- Reduces lock contention significantly

**Time Complexity:** O(1)  
**Space Complexity:** O(1)

**Interview Points:**
- *"This is a performance optimization over full synchronization. The first check outside the lock allows threads to bypass the synchronized block once the instance is created."*
- *"The synchronized block inside ensures that even if multiple threads pass the first check, only one will enter and create the instance."*
- *"This pattern significantly reduces lock contention while maintaining thread safety."*

---

### 4. Thread-Safe Eager Loading Singleton

**File:** `ThreadSafeEagerLoading.java`

**Implementation:**
```
- Private constructor
- Static instance initialized at class loading time
- getInstance() simply returns the pre-created instance
- No synchronization needed
```

**How It Works:**
1. When the class is loaded by the ClassLoader, the static variable is initialized
2. Class loading is inherently thread-safe in Java
3. The instance is created before any thread accesses it
4. `getInstance()` just returns the pre-initialized instance

**Advantages:**
- **Guaranteed Thread-Safe**: Class loading is atomic and thread-safe
- **Simple Implementation**: No synchronization code needed
- **No Null Checks**: Instance always exists
- **Deterministic**: Initialization time is predictable
- **No Performance Overhead**: No locks required

**Disadvantages:**
- **Not Lazy**: Instance created even if never used (wastes memory for resource-intensive objects)
- **No Control Over Initialization**: Instance created during class loading, not when first needed
- **Not Flexible**: Can't defer initialization for performance reasons
- **Wasted Resources**: If the Singleton is rarely used, creating it immediately is inefficient

**Memory Impact:** Immediate allocation at startup (can be significant for heavy objects)

**Interview Points:**
- *"This approach leverages Java's thread-safe class loading mechanism. Since class initialization is atomic, the instance is guaranteed to be created by only one thread."*
- *"It's the simplest and most thread-safe approach, but not ideal when the Singleton holds expensive resources that might not be used."*

---

## Comparison Table

| Aspect | Naive | Full Sync | Double-Check | Eager Loading |
|--------|-------|-----------|--------------|---------------|
| **Thread-Safe** | ❌ No | ✅ Yes | ✅ Yes | ✅ Yes |
| **Lazy Init** | ✅ Yes | ✅ Yes | ✅ Yes | ❌ No |
| **Performance** | ⭐ Excellent (but unsafe) | ⭐ Poor | ⭐⭐⭐⭐⭐ Excellent | ⭐⭐⭐⭐⭐ Excellent |
| **Complexity** | ⭐ Simple | ⭐⭐ Medium | ⭐⭐⭐⭐ Complex | ⭐ Simple |
| **Lock Overhead** | None | On every call | Only on first call | None |
| **When to Use** | Single-threaded only | Legacy systems | Most scenarios | Lightweight objects |

---

## Key Interview Questions & Answers

### Q1: Why is the constructor private?
**A:** The private constructor prevents instantiation from outside the class. This ensures no one can create another instance using `new`, maintaining the singleton constraint.

### Q2: Why do we need synchronization in Singleton?
**A:** In multi-threaded environments, without synchronization, two threads can simultaneously check `if(instance == null)` and both enter the block, creating multiple instances. Synchronization ensures atomicity of the check-and-create operation.

### Q3: What's the problem with Naive Singleton in multi-threading?
**A:** **Race Condition**: Thread A checks `if(instance == null)` (true) but is preempted. Thread B checks `if(instance == null)` (true), creates instance. Thread A resumes and also creates another instance. Result: two instances exist, violating the Singleton pattern.

### Q4: Why is Double-Checked Locking better than full synchronization?
**A:** The first unsynchronized check allows threads to bypass the expensive lock acquisition after initialization. Only the first thread acquiring the lock creates the instance; subsequent threads skip the synchronized block entirely, eliminating lock contention overhead.

### Q5: Why use Eager Loading?
**A:** When you know the Singleton will definitely be used and the object is lightweight, Eager Loading is simpler, completely thread-safe by design, and has zero runtime overhead. No synchronization or null checks are needed.

### Q6: What's wrong with this Eager Loading code?
**A:** The main method tries to create instances directly using `new ThreadSafeEagerLoading()`, which violates the Singleton pattern. It should only use `getInstance()`. This might be intentional to demonstrate the private constructor works.

### Q7: Can Singleton be broken?
**A:** Yes, through reflection or serialization/deserialization. Reflection can make the private constructor accessible. Serialization creates a new instance during deserialization. These require additional protective measures.

### Q8: What is the "volatile" keyword role in Double-Checked Locking?
**A:** `volatile` ensures visibility of the instance across threads. Without it, a thread might see a partially initialized object due to JVM memory reordering, even though DCL initially appeared to be broken before Java 5's memory model improvements.

### Q9: How is Eager Loading thread-safe without explicit synchronization?
**A:** Java guarantees that class loading is atomic and thread-safe. When the class is first loaded, static initializers run exactly once, supervised by the ClassLoader, ensuring atomicity without explicit synchronization code.

### Q10: Which implementation should I use in production?
**A:** 
- **Bill Pugh Singleton** (Eager Loading variant using static inner class) is best practice
- If you need laziness: **Double-Checked Locking** despite its complexity
- If simplicity is priority: **Eager Loading**
- Avoid: **Naive Singleton** (not thread-safe) and **Full Synchronization** (performance issues)

---

## Real-World Use Cases

1. **Database Connection Pool**: Single database connection manager serving entire application
2. **Logger**: Application-wide logging instance
3. **Configuration Manager**: Single source of configuration across application
4. **Thread Pool**: Unified thread pool for concurrent task execution
5. **Cache Manager**: Application-wide cache instance
6. **Thread-Local Storage**: Managing thread-specific data
7. **Service Locator**: Single registry for all application services

---

## Important Concepts for Interview

### Memory Visibility
- In multi-threaded scenarios, changes made by one thread aren't visible to another without proper synchronization
- `synchronized` keyword ensures visibility through memory barriers

### Atomic Operations
- The check-and-create operation must be atomic (indivisible)
- Without atomicity, multiple threads can both see `null` and create separate instances

### Class Loading
- Java guarantees class loading is thread-safe
- Static initializers execute exactly once during class loading
- This is why Eager Loading is inherently thread-safe

### Synchronization Overhead
- Synchronization has performance cost (lock acquisition, memory barriers, context switching)
- Double-Checked Locking optimizes by minimizing lock acquisition to only when necessary

### Race Conditions vs Data Races
- **Race Condition**: Multiple threads competing to update shared state simultaneously
- **Data Race**: Unsynchronized access to shared mutable data

---

## Summary
The Singleton pattern is fundamental in system design. For interviews:

1. **Understand the Problem**: Why we need Singleton (global access, controlled instantiation)
2. **Know the Implementations**: Each has trade-offs between thread-safety, performance, and complexity
3. **Thread-Safety is Critical**: In today's multi-threaded world, thread-safety is non-negotiable
4. **Performance Matters**: Choose implementation based on whether immediate initialization or lazy initialization is needed
5. **Production Best Practice**: Use Bill Pugh Singleton (static inner class) or Enum Singleton for maximum safety and simplicity
