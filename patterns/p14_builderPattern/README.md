# Builder Design Pattern

## Definition

The **Builder Design Pattern** is a creational design pattern that separates the construction of a complex object from its representation, allowing you to construct objects step by step. It enables the creation of different representations of an object using the same construction process, making it easier to build objects with many optional parameters while maintaining a clean and readable API.

## Problem Statement (Interview Context)

### The Telescoping Constructor Problem

**Scenario:** You have a class with multiple parameters, many of which are optional:

```java
// Without Builder - Telescoping Constructor Anti-Pattern
class HttpRequest {
    // Way too many constructors!
    HttpRequest(String url) { }
    HttpRequest(String url, String method) { }
    HttpRequest(String url, String method, Map<String, String> headers) { }
    HttpRequest(String url, String method, Map<String, String> headers, String body) { }
    HttpRequest(String url, String method, Map<String, String> headers, String body, int timeout) { }
    HttpRequest(String url, String method, Map<String, String> headers, String body, int timeout, 
                Map<String, String> queryParams) { }
    // ... and so on
}
```

**Issue:** 
- Creates an exponential number of constructors (2^n for n optional parameters)
- Difficult to remember which constructor to use
- Hard to pass parameters in specific order
- Breaks if you need to add a new optional parameter
- Code becomes unreadable when passing many parameters

**Solution:** Use the Builder Pattern to create objects step-by-step.

## Purpose & Benefits

| Purpose | How Builder Solves It |
|---------|----------------------|
| **Handle Multiple Optional Parameters** | Each parameter is set via a separate method call |
| **Improve Code Readability** | `setMethod("POST")` is clearer than positional arguments |
| **Avoid Constructor Overloading** | Single constructor, multiple builder methods |
| **Enable Immutability** | All state set before object creation |
| **Support Validation** | Can validate at each step or at build time |
| **Reduce Coupling** | Construction logic isolated from business logic |
| **Flexible Object Creation** | Different builders can create variations |

## Key Characteristics

| Feature | Details |
|---------|---------|
| **Method Chaining** | Builder methods return `this` enabling fluent API |
| **Separation of Concerns** | Construction logic separate from product class |
| **Step-by-Step Assembly** | Object built incrementally through sequential calls |
| **Parameter Flexibility** | Easy to add optional parameters without API breakage |
| **Immutability Support** | Objects can be immutable after construction complete |
| **Validation Point** | Can validate during construction or at `build()` |
| **Return Type Control** | Builder can return different types based on state |

## Core Principles

1. **Fluent Interface** — Each method returns the builder (or interface) to enable chaining
2. **Separation** — Builder is separate from the product class
3. **Step-by-Step** — Object state built incrementally, not all at once
4. **Validation** — Enforces constraints before object creation
5. **Encapsulation** — Construction details hidden from client

---

# Three Implementation Patterns

## 1. SIMPLE BUILDER PATTERN

**File Location:** `simpleBuilder/`  
**Real-World Example:** Building a simple POST request with optional headers and body

### Architecture

**HttpRequest.java Structure:**
```
HttpRequest (Product Class)
├── Private Fields
│   ├── url (String) - Required
│   ├── method (String) - Required
│   ├── headers (Map) - Optional
│   ├── queryParams (Map) - Optional
│   ├── body (String) - Optional
│   └── timeout (int) - Optional
├── Private Constructor (only through builder)
└── Inner Class: HttpRequestBuilder
    ├── Stores: HttpRequest instance
    ├── Methods: All return HttpRequestBuilder
    ├── setUrl() → HttpRequestBuilder
    ├── setMethod() → HttpRequestBuilder
    ├── addHeader() → HttpRequestBuilder
    ├── addQueryParam() → HttpRequestBuilder
    ├── setBody() → HttpRequestBuilder
    ├── setTimeout() → HttpRequestBuilder
    └── build() → HttpRequest
```

**Characteristics:**

1. **Flexibility**: All parameters are optional
   - Can set parameters in any order
   - Can skip any parameter
   - Useful for objects with many optional fields

2. **Validation Strategy**: Runtime validation
   ```java
   public HttpRequest build() {
       if (req.url == null || req.method == null) {
           throw new IllegalStateException("URL and Method are required fields.");
       }
       return req;
   }
   ```
   - Validation happens only at `build()` call
   - Invalid state possible during construction
   - Errors caught late in the process

3. **Method Chaining Pattern**:
   ```java
   new HttpRequestBuilder()
       .setUrl("localhost:127:0:0:1")          // Builder → Builder
       .setMethod("POST")                      // Builder → Builder
       .addHeader("Token", "32r23k")          // Builder → Builder
       .addQueryParam("SQL", "234")           // Builder → Builder
       .setBody("{name:Naman}")               // Builder → Builder
       .setTimeout(1)                         // Builder → Builder
       .build();                               // Builder → HttpRequest
   ```

### Interview Deep Dive: Simple Builder

**Q: How does method chaining work in Simple Builder?**

A: Each builder method returns `this` (the builder instance itself), allowing the next method to be called immediately on the same object. This creates a fluent chain of calls.

```java
public HttpRequestBuilder setUrl(String url) {
    req.url = url;
    return this;  // ← Enables method chaining
}
```

**Q: What are the validation trade-offs in Simple Builder?**

A: 
- **Pro**: Allows maximum flexibility; any parameter combination possible
- **Con**: Invalid states can exist until `build()` is called
- **Con**: Developers might forget to set required fields
- **Solution**: Use Step Builder if you need compile-time validation

**Q: Why is the HttpRequest constructor private?**

A: To enforce that all instances must be created through the builder. This ensures:
- Consistent initialization logic
- All required fields validated
- Clean object state guaranteed

**Q: What happens if required fields are missing?**

A:
```java
new HttpRequestBuilder()
    .setUrl("localhost")
    // Missing .setMethod() - ERROR at build time!
    .build();  // Throws: IllegalStateException: URL and Method are required fields.
```

**Q: Can you set the same parameter twice?**

A: Yes! The last value wins:
```java
new HttpRequestBuilder()
    .setUrl("http://first.com")
    .setUrl("http://second.com")  // ← Overwrites first URL
    .build();  // Uses "http://second.com"
```

**Q: Memory implications of the builder pattern?**

A: 
- Creates temporary builder object
- Stores reference to HttpRequest inside builder
- When `build()` returns, builder can be garbage collected
- No memory leak if builder references are released

**Key Interview Point:** *"Simple Builder prioritizes flexibility over safety. It's perfect for domains where you have many optional parameters and validation is straightforward."*

### Advantages & Disadvantages

**Advantages:**
- ✅ Simple, easy to implement
- ✅ Very flexible (any parameter, any order)
- ✅ Standard fluent interface pattern
- ✅ Can add new parameters without breaking existing code
- ✅ Readable method names make intent clear

**Disadvantages:**
- ❌ No compiler enforcement of mandatory fields
- ❌ Invalid state can exist during building
- ❌ Missing required fields only caught at `build()` time
- ❌ No IDE guidance on required vs optional parameters
- ❌ Requires careful documentation

**Best Use Cases:**
- Objects with mostly optional parameters
- Simple domains where validation is straightforward
- Rapid prototyping and development
- Objects created with different combinations of optional fields

---

## 2. STEP BUILDER PATTERN (Advanced Type Safety)

**File Location:** `stepBuilder/`  
**Interview Level:** Advanced/Senior  
**Real-World Example:** Complex HTTP request with mandatory sequence

### Architecture (Interface-Based State Machine)

**The Four Mandatory Steps:**

```
Step 1: UrlStep
Interface: UrlStep
└── Method: setUrl(String) → MethodStep
    └─ Purpose: Ensure URL is set first
    └─ Returns: MethodStep (next step)

Step 2: MethodStep
Interface: MethodStep  
└── Method: setMethod(String) → HeaderStep
    └─ Purpose: Ensure method is set second
    └─ Returns: HeaderStep (next step)

Step 3: HeaderStep
Interface: HeaderStep
└── Method: addHeader(String, String) → OptionalStep
    └─ Purpose: Ensure at least one header is added
    └─ Returns: OptionalStep (can repeat or move to final)

Step 4: OptionalStep  
Interface: OptionalStep
├── Methods: setBody(), addQueryParam(), setTimeout() → OptionalStep
├── Method: build() → HttpRequest
├── Purpose: All remaining operations are optional and repeatable
└── Returns: Self (can chain more optional steps) or final HttpRequest
```

### How Step Builder Enforces Sequence

**The Genius:** Each interface represents a step, and the builder implements ALL interfaces:

```java
public static class HttpRequestBuilder 
    implements UrlStep, MethodStep, HeaderStep, OptionalStep {
    
    public MethodStep setUrl(String url) {
        req.url = url;
        return this;  // ← Returns MethodStep, so only setMethod() available next
    }
    
    public HeaderStep setMethod(String method) {
        req.method = method;
        return this;  // ← Returns HeaderStep, so only addHeader() available next
    }
    
    public OptionalStep addHeader(String key, String value) {
        req.headers.put(key, value);
        return this;  // ← Returns OptionalStep, so optional methods available
    }
    
    public OptionalStep setBody(String body) {
        req.body = body;
        return this;  // ← Returns OptionalStep again (can chain more optionals)
    }
    
    public HttpRequest build() {
        // ... validation
        return req;
    }
}
```

### How Compiler Enforces It

```java
// ✅ VALID - Compiler allows this
HttpRequest req = new HttpRequestBuilder()
    .setUrl("https://diginode.in")      // Returns MethodStep
    .setMethod("POST")                  // Returns HeaderStep
    .addHeader("Content-type", "json")  // Returns OptionalStep
    .setBody("{...}")                   // Returns OptionalStep (can repeat)
    .setTimeout(5)                      // Returns OptionalStep (can repeat)
    .build();                           // Returns HttpRequest

// ❌ COMPILER ERROR - Impossible to compile
HttpRequest req = new HttpRequestBuilder()
    .setUrl("https://diginode.in")      // Returns MethodStep
    .build();  // Error: The method build() is undefined for the type MethodStep
    // Compiler knows MethodStep interface doesn't have build() method!

// ❌ COMPILER ERROR - Wrong sequence
HttpRequest req = new HttpRequestBuilder()
    .setMethod("POST")  // Error: Cannot resolve method setMethod() for UrlStep
    .setUrl("...")      // Must call setUrl() first!
```

### Type Safety Mechanism

**At Compile Time:**
- IDE autocomplete shows ONLY valid methods for current state
- Wrong method calls are underlined with red squiggles
- Build fails before runtime if wrong sequence
- Developer gets instant feedback

**At Runtime:**
- No sequence enforcement needed (compiler already handled it)
- Focus validation on field values, not sequence

### Interview Deep Dive: Step Builder

**Q: How does Step Builder provide compile-time type safety?**

A: Each interface represents a step, and the builder returns different interface types at each step. The compiler only allows calling methods defined in the returned interface. This makes invalid state impossible at compile time.

**Q: Why implement multiple interfaces in Step Builder?**

A: Because the builder must be:
- Assignable to `UrlStep` type initially (so only `setUrl()` is available)
- Assignable to `MethodStep` type after `setUrl()` (so only `setMethod()` is available)  
- Assignable to `HeaderStep` type after `setMethod()` (so only `addHeader()` is available)
- Assignable to `OptionalStep` type after `addHeader()` (so `build()` becomes available)

**Q: What if I want to skip a step?**

A: You cannot! The interfaces enforce the sequence. This is intentional—it ensures mandatory fields are always set.

```java
// Impossible to skip setMethod():
new HttpRequestBuilder()
    .setUrl("...")
    // Can't call addHeader() directly, must call setMethod() first
    .addHeader(...)  // Compiler error: setMethod() not available on UrlStep type
```

**Q: Can you call optional steps in different order?**

A: Yes! Once you're in `OptionalStep`, all optional methods return `OptionalStep`:

```java
new HttpRequestBuilder()
    .setUrl(...)
    .setMethod(...)
    .addHeader(...)
    .setBody(...)       // OptionalStep
    .setTimeout(...)    // OptionalStep → can do any optional step after
    .addQueryParam(...) // OptionalStep → can still do another one
    .build();           // Now available
```

**Q: Why is this pattern not always used?**

A: Trade-offs:
- **Pro**: Type-safe, compile-time enforcement
- **Con**: More complex to implement
- **Con**: Requires many interfaces
- **Con**: Less flexible (fixed sequence)
- **Con**: Learning curve for junior developers

**Q: Performance implications?**

A: No performance difference at runtime. All interface returns are just type casts. The JVM compiles it away.

**Key Interview Point:** *"Step Builder trades flexibility for safety. It guarantees at compile time that all mandatory steps are performed in the correct order. This is invaluable for safety-critical systems where invalid states could cause expensive failures."*

### Advantages & Disadvantages

**Advantages:**
- ✅ **Compile-time type safety** — Impossible to skip steps
- ✅ **Prevents invalid sequences** — Wrong order caught by compiler
- ✅ **IDE support** — Autocomplete shows only valid methods
- ✅ **Self-documenting** — Interface names document the flow
- ✅ **Enforces mandatory steps** — Can't call `build()` before required steps

**Disadvantages:**
- ❌ Complex implementation (multiple interfaces required)
- ❌ More code to write and maintain
- ❌ Inflexible (single fixed sequence)
- ❌ Hard to refactor if sequence needs to change
- ❌ Steeper learning curve
- ❌ Can be overkill for simple objects

**Best Use Cases:**
- Safety-critical systems (banking, healthcare, aviation)
- Objects with strict construction requirements
- Complex domain models with mandatory prerequisites
- Systems where invalid states must be impossible
- High-reliability enterprise applications

---

## 3. DIRECTOR BUILDER PATTERN

**File Location:** `directorBuilder/`  
**Real-World Example:** Pre-configured HTTP requests

### Architecture

**Pattern Structure:**

```
Director (HttpRequestDirector)
├── Encapsulates common construction patterns
├── Public Static Methods (facades over builder)
│   ├── createHttpGetRequest(url) → HttpRequest
│   └── createHttpJsonRequest(url, jsonBody) → HttpRequest
└── Returns fully configured HttpRequest objects

Builder (HttpRequest.HttpRequestBuilder)
├── Reusable builder implementation
└── Configured by director, used by clients
```

**Implementation Pattern:**

```java
// Director encapsulates construction logic
public class HttpRequestDirector {
    public static HttpRequest createHttpGetRequest(String url) {
        return new HttpRequest.HttpRequestBuilder()
            .setUrl(url)
            .setMethod("GET")
            .build();
    }

    public static HttpRequest createHttpJsonRequest(String url, String jsonBody) {
        return new HttpRequest.HttpRequestBuilder()
            .setUrl(url)
            .setMethod("POST")
            .addHeader("Token", "32r23k")
            .addHeader("Content-Type", "application/json")
            .addQueryParam("SQL", "234")
            .setBody(jsonBody)
            .setTimeout(1)
            .build();
    }
}

// Client code
HttpRequest getRequest = HttpRequestDirector.createHttpGetRequest("https://api.example.com");
HttpRequest jsonRequest = HttpRequestDirector.createHttpJsonRequest("https://api.example.com", "{...}");
```

### When Does Director Get Involved?

**Before Director (Scattered Logic):**
```java
// Different team members build requests differently
// Team A:
new HttpRequestBuilder()
    .setUrl(url)
    .setMethod("GET")
    .addHeader("User-Agent", "MyApp/1.0")
    .build();

// Team B:
new HttpRequestBuilder()
    .setUrl(url)
    .setMethod("GET")
    .build();

// Team C:
new HttpRequestBuilder()
    .setUrl(url)
    .setMethod("GET")
    .addHeader("Accept", "application/json")
    .addHeader("User-Agent", "MyApp/1.0")
    .build();
// ← Inconsistent! Different teams configure differently
```

**With Director (Centralized Pattern):**
```java
// One standard way
HttpRequestDirector.createHttpGetRequest(url);
// ← All teams use same configuration
```

### Interview Deep Dive: Director Pattern

**Q: What's the relationship between Builder and Director?**

A: 
- **Builder**: The tool (how to build)
- **Director**: The blueprint (what to build)

The Builder is like a toolbox; the Director knows which tools to use and in what order.

**Q: When should you add a Director?**

A: When you have **reusable construction patterns** that clients use repeatedly:

```java
// If 80% of your code looks like this:
new HttpRequestBuilder()
    .setUrl(url)
    .setMethod("GET")
    .addHeader("User-Agent", "MyApp/1.0")
    .addHeader("Accept", "application/json")
    .build();

// Then create a director method:
HttpRequestDirector.createStandardGetRequest(url);
// ← Reduces boilerplate, ensures consistency
```

**Q: Is Director always necessary?**

A: No! Only add Director when:
- You have 3+ commonly-used configuration patterns
- Multiple teams/modules use the same patterns
- You want to enforce consistency
- Patterns are stable and likely to be reused

Don't add Director for:
- Single use cases
- Patterns that change frequently
- Very simple objects
- Highly customized requests

**Q: How does Director improve maintainability?**

A: 
- **Single Source of Truth**: Change the pattern in one place
- **Consistency**: All clients use the same configuration
- **API Clarity**: `createHttpJsonRequest()` is clearer than raw builder
- **Encapsulation**: Hides builder complexity from clients

**Q: What if a client needs a custom configuration?**

A: 
- If it's common: Add a new Director method
- If it's rare: Provide `public static builder()` method to return bare builder

```java
// Standard patterns
HttpRequestDirector.createHttpGetRequest(url);

// Custom one-off
new HttpRequest.HttpRequestBuilder()
    .setUrl(url)
    .setMethod("PATCH")
    .addHeader("X-Custom", "value")
    .setTimeout(30)
    .build();
```

**Q: Can Director methods call each other?**

A: Yes! This promotes DRY principle:

```java
public static HttpRequest createHttpGetRequest(String url) {
    return new HttpRequest.HttpRequestBuilder()
        .setUrl(url)
        .setMethod("GET")
        .addHeader("User-Agent", "MyApp/1.0")
        .build();
}

public static HttpRequest createHttpGetRequestWithAuth(String url, String token) {
    return new HttpRequestBuilder()
        .setUrl(url)
        .setMethod("GET")
        .addHeader("User-Agent", "MyApp/1.0")
        .addHeader("Authorization", "Bearer " + token)
        .build();
}

// Better: Reuse base pattern
public static HttpRequest createHttpGetRequestWithAuth(String url, String token) {
    HttpRequest baseRequest = createHttpGetRequest(url);
    // Can't modify after creation (immutable), so this doesn't work
    // Better approach: extract common builder logic
}

// Best approach:
private static HttpRequest.HttpRequestBuilder baseGetRequestBuilder(String url) {
    return new HttpRequest.HttpRequestBuilder()
        .setUrl(url)
        .setMethod("GET")
        .addHeader("User-Agent", "MyApp/1.0");
}

public static HttpRequest createHttpGetRequest(String url) {
    return baseGetRequestBuilder(url).build();
}

public static HttpRequest createHttpGetRequestWithAuth(String url, String token) {
    return baseGetRequestBuilder(url)
        .addHeader("Authorization", "Bearer " + token)
        .build();
}
```

**Key Interview Point:** *"Director Pattern is about encapsulating common construction patterns. It reduces boilerplate, ensures consistency, and provides a semantic API that's clear to clients."*

### Advantages & Disadvantages

**Advantages:**
- ✅ Encapsulates complex construction logic
- ✅ Reduces code duplication
- ✅ Centralizes configuration patterns
- ✅ Easy to use (clean API)
- ✅ Ensures consistency across codebase
- ✅ Single point to modify patterns

**Disadvantages:**
- ❌ Less flexible (limited to predefined patterns)
- ❌ Requires modifying Director to add new patterns
- ❌ Can become bloated if too many patterns
- ❌ Hides builder complexity (might confuse developers)
- ❌ Overkill for simple objects with few patterns

**Best Use Cases:**
- Domain-specific construction patterns
- Recurring configuration combinations
- Enterprise applications with standardized requests
- Systems with versioning requirements
- Multi-team codebases needing consistency

---

# Comprehensive Comparison Table

| Aspect | Simple Builder | Step Builder | Director Builder |
|--------|---|---|---|
| **Complexity** | Low | High | Medium |
| **Type Safety** | Runtime only | Compile-time | Runtime with encapsulation |
| **Flexibility** | Very High | Low | Medium (pattern-based) |
| **Parameter Order** | Any order | Fixed, enforced | Fixed by director |
| **Mandatory Steps** | Runtime validation | Compile-time enforcement | Runtime validation |
| **Ease of Use** | Easy | Medium | Very easy (for standard patterns) |
| **Code Reusability** | Low (client configures) | N/A | High (encapsulated) |
| **Implementation Effort** | 1 builder class | N + builder class (N interfaces) | Builder + Director class |
| **Maintenance Burden** | Low | Medium | Medium |
| **IDE Support** | Method names | Full autocomplete + error checking | Method names |
| **Error Detection** | At `build()` time | At compile time | At `build()` time |
| **Learning Curve** | Easy | Steep | Easy |
| **Best For Beginners** | Yes | For advanced concepts | Medium |
| **Interview Level** | Beginner | Advanced/Senior | Intermediate |
| **Production Use** | Common | Safety-critical systems | Enterprise systems |

---

# Builder Pattern vs Other Creational Patterns

| Pattern | Purpose | Key Difference from Builder |
|---------|---------|---|
| **Factory Pattern** | Creates objects of specific type | Factory returns complete object; Builder constructs step-by-step |
| **Abstract Factory** | Creates families of related objects | Focuses on object types; Builder focuses on construction process |
| **Singleton Pattern** | Ensures one instance exists | Ensures one instance; Builder creates many |
| **Prototype Pattern** | Creates objects by cloning | Clones existing object; Builder constructs from scratch |
| **Object Pool** | Reuses expensive objects | Manages object lifecycle; Builder focuses on creation |

---

# Design Principles Applied

| SOLID Principle | How Builder Implements It |
|---|---|
| **Single Responsibility** | Builder handles construction; Product handles behavior |
| **Open/Closed** | Open for extension (new builder methods); Closed for modification |
| **Liskov Substitution** | All builder methods return compatible types/interfaces |
| **Interface Segregation** | Step Builder uses small interfaces (UrlStep, MethodStep, etc.) |
| **Dependency Inversion** | Builder depends on abstract Product interface, not concrete details |

---

# Interview Scenarios & Answers

## Scenario 1: "Build a Logger Configuration"

**Question:** How would you use Builder Pattern to configure a logger?

**Answer:**
```java
// Simple Builder approach
Logger logger = new LoggerBuilder()
    .setLevel(Level.INFO)
    .addConsoleAppender()
    .addFileAppender("app.log")
    .setFormat("[%d{yyyy-MM-dd HH:mm:ss}] %m")
    .setAsync(true)
    .build();
```

**Interview Points:**
- Useful when many options are optional
- Clear intent (what's being configured)
- Easy to add new options without breaking existing code
- Can validate interdependencies (`async=true` requires specific handlers)

---

## Scenario 2: "Design an HTML Form Generator"

**Question:** How would Builder Pattern help build HTML forms?

**Answer (Director Pattern):**
```java
Form loginForm = FormDirector.createLoginForm();
Form registrationForm = FormDirector.createRegistrationForm();

// Or custom form
Form customForm = new FormBuilder()
    .addField(new TextField("username"))
    .addField(new PasswordField("password"))
    .addField(new CheckboxField("remember"))
    .addButton(new SubmitButton("Login"))
    .build();
```

**Interview Points:**
- Director encapsulates common patterns (login, registration)
- Builder provides flexibility for custom forms
- Separates concerns (what to build vs. how to build)

---

## Scenario 3: "Implement a Step-Based Wizard"

**Question:** How would Step Builder help with a multi-step wizard?

**Answer:**
```java
// Each step must be completed in order
Reservation reservation = new ReservationBuilder()
    .selectDates(startDate, endDate)      // Step 1 - returns DateStep
    .selectGuests(numGuests)              // Step 2 - returns GuestStep
    .selectRooms(rooms)                   // Step 3 - returns RoomStep
    .addSpecialRequests("Late checkout")  // Step 4 - returns RequestStep (optional)
    .enterPaymentInfo(cardInfo)           // Next required step
    .confirmReservation()                 // Final step
    .build();
```

**Interview Points:**
- Enforces business logic sequence at compile time
- Prevents booking without required information
- Clear UI/UX flow (Step interface guides through process)
- Invalid states become compile-time errors

---

## Scenario 4: "SQL Query Builder"

**Question:** Would you use Simple or Step Builder for a SQL query builder?

**Answer:** Depends on queries:

Simple Builder (flexible):
```java
SELECT * FROM users WHERE age > 18 ORDER BY name LIMIT 10
// Built as:
new QueryBuilder()
    .select("*")
    .from("users")
    .where("age > ?", 18)
    .orderBy("name")
    .limit(10)
    .build();
```

Step Builder (strict SQL semantics):
```java
// Must follow SQL structure: SELECT → FROM → WHERE → ORDER BY → LIMIT
new QueryBuilder()
    .select("*")        // SelectStep → FromStep
    .from("users")      // FromStep → WhereStep
    .where("age > ?", 18)  // WhereStep → OrderStep
    .orderBy("name")    // OrderStep → LimitStep
    .limit(10)          // LimitStep → build()
    .build();
```

**Interview Points:**
- Step Builder enforces SQL correctness (can't put LIMIT before FROM)
- Makes query building self-documenting
- Compiler catches SQL syntax errors early

---

## Core Interview Questions

**Q1: Why not just create a class with all getters/setters?**

A: That approach (POJO with setters) allows invalid intermediate states where object exists but is half-initialized. Builder ensures objects are either non-existent or fully valid.

**Q2: How does Builder compare to the Setter approach?**

A:
```java
// Setter approach (anti-pattern for immutability)
HttpRequest req = new HttpRequest();
req.setUrl("...");
req.setMethod("...");
req.addHeader("...");
// ← Object exists in invalid state between each call

// Builder approach (guarantees validity)
HttpRequest req = new HttpRequest.Builder()
    .setUrl("...")
    .setMethod("...")
    .addHeader("...")
    .build();  // ← Only now does object exist, and it's valid
```

**Q3: What is method chaining and why is it important?**

A: Method chaining is when a method returns `this`, enabling another method to call immediately on the same object. It's important because:
- Makes fluent, readable code
- Reduces variable declarations
- Clearly shows object construction flow
- Standard in modern Java APIs (Stream API, etc.)

**Q4: How do you handle validation in Builder?**

A: Three strategies:

1. **Validate in each setter** (eager validation):
```java
public BuilderType setAge(int age) {
    if (age < 0) throw new IllegalArgumentException("Age must be non-negative");
    req.age = age;
    return this;
}
```

2. **Validate at `build()` time** (lazy validation):
```java
public Product build() {
    if (req.url == null) throw new IllegalStateException("URL required");
    return req;
}
```

3. **Dependent validation** (cross-field):
```java
public Product build() {
    if (req.isAsync && req.timeout == 0) {
        throw new IllegalStateException("Async requires timeout > 0");
    }
    return req;
}
```

**Q5: Can Builder Pattern be used with immutable objects?**

A: Yes! That's one of its primary benefits. The builder is mutable, but once `build()` is called, the resulting object is immutable.

**Q6: How does Builder Pattern relate to immutability?**

A:
- Builder constructs the object step-by-step (mutable process)
- Once `build()` is called, object is returned (typically made immutable)
- Client receives fully constructed, immutable object
- No setters on the product class

**Q7: What's the memory overhead of Builder?**

A: 
- Creates one builder object per construction
- Stores reference to product inside builder
- Builder is garbage collected after `build()` returns
- Negligible in most applications

**Q8: When would you NOT use Builder Pattern?**

A:
- Simple objects with 1-3 parameters
- All parameters are mandatory and independent
- Performance is critical (minimal but the language of creation overhead)
- Domain doesn't support step-wise construction
- Legacy codebase with simpler patterns

**Q9: How does Step Builder provide type safety?**

A: By returning different interface types at each step, the compiler enforces which methods are available. Only valid method sequences can be compiled.

**Q10: What's the difference between Director and Simple Builder?**

A:
- **Simple Builder**: Raw construction tool available to anyone
- **Director**: Semantic, encapsulated patterns for specific use cases

Use both: Director for standard configurations, Builder for custom ones.

---

# When to Choose Each Pattern

## Use Simple Builder When:
- Flexibility is more important than enforcement
- You have many optional parameters (5+)
- Construction sequence doesn't matter
- Different clients need different configurations
- Adding new optional parameters is frequent

## Use Step Builder When:
- Safety-critical systems
- Specific mandatory sequence required
- Compile-time type safety is priority
- Preventing invalid states is important
- Team is advanced enough to understand interfaces

## Use Director When:
- You have 3+ well-known construction patterns
- Multiple teams/modules use same patterns
- Consistency across codebase is important
- Patterns are stable (don't change often)
- You want to reduce client-side boilerplate

---

# Anti-Patterns & Pitfalls

## ❌ Pitfall 1: Over-using Builder

```java
// Bad: Using Builder for a simple 2-parameter object
Point point = new PointBuilder()
    .setX(10)
    .setY(20)
    .build();

// Better: Normal constructor
Point point = new Point(10, 20);
```

## ❌ Pitfall 2: Forgetting Null Checks

```java
// Bad: No null checking
public Builder setUrl(String url) {
    req.url = url;  // What if url is null?
    return this;
}

// Better: Validate input
public Builder setUrl(String url) {
    req.url = Objects.requireNonNull(url, "URL cannot be null");
    return this;
}
```

## ❌ Pitfall 3: Mutable Product

```java
// Bad: Product can be modified after build
public HttpRequest build() {
    return req;  // Client can call req.setUrl() later!
}

// Better: Return unmodifiable or immutable object
public HttpRequest build() {
    return Collections.unmodifiableHttpRequest(req);
}
```

## ❌ Pitfall 4: Too Many Validation Rules

```java
// Bad: Complex interdependencies
public Builder build() {
    if (async && timeout == 0) throw ...;
    if (ssl && port != 443) throw ...;
    if (proxy && !url.startsWith("http")) throw ...;
    // Too many rules, hard to test, brittle
}

// Better: Separate validators
public Builder build() {
    new AsyncValidator(req).validate();
    new SslValidator(req).validate();
    new ProxyValidator(req).validate();
}
```

## ❌ Pitfall 5: Breaking Fluent Chain

```java
// Bad: Method doesn't return builder
public void setTimeout(int timeout) {  // ← Returns void!
    req.timeout = timeout;
}

// Better: Return builder to enable chaining
public Builder setTimeout(int timeout) {
    req.timeout = timeout;
    return this;  // ← Enables .setBody().setTimeout()
}
```

---

# Key Takeaways for Interviews

1. **Problem**: Telescoping constructor problem with many optional parameters
2. **Solution**: Builder Pattern separates construction from representation
3. **Three Variants**:
   - Simple: Flexible, runtime validation
   - Step: Type-safe, compile-time enforcement
   - Director: Encapsulates patterns
4. **Method Chaining**: Each method returns builder to enable fluent API
5. **Immutability**: Builder is mutable, product is immutable
6. **Validation**: Can happen at each step or at `build()` time
7. **Trade-offs**: Safety vs. flexibility, complexity vs. usability
8. **When to Use**: Objects with 5+ optional parameters, specific sequences, or domain patterns
9. **When Not**: Simple objects, all mandatory parameters, performance critical
10. **Real-World**: HTTP requests, database configs, UI builders, query builders

---

# Summary: Three Patterns Side-by-Side

### Simple Builder
```java
HttpRequest req = new HttpRequestBuilder()
    .setUrl(url)
    .setMethod("POST")
    .build();
// Flexible, simple, error-prone if wrong sequence
```

### Step Builder
```java
HttpRequest req = new HttpRequestBuilder()
    .setUrl(url)           // ← Forces this first (UrlStep)
    .setMethod("POST")     // ← Forces this second (MethodStep)
    .build();              // ← Type-safe, compiler enforces
// Safe, complex, enforces sequence
```

### Director Builder
```java
HttpRequest req = HttpRequestDirector.createHttpJsonRequest(url, body);
// Semantic, encapsulated, limited to predefined patterns
```

**Interview Insight**: All three are Builder Pattern variants. The choice depends on your requirements:
- Need flexibility? → Simple
- Need safety? → Step  
- Need consistency? → Director
- Need all three? → Combine them!

