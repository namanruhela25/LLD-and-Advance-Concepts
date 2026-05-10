# Builder Design Pattern

## Definition

The **Builder Design Pattern** is a creational design pattern that separates the construction of a complex object from its representation, allowing you to construct objects step by step. It enables the creation of different representations of an object using the same construction process, making it easier to build objects with many optional parameters.

## Purpose

The Builder pattern is used when:
- You need to create complex objects with multiple optional parameters
- You want to avoid constructor overloading (telescoping constructor problem)
- You need to ensure immutability after object creation
- You want to make the construction process clear and readable
- You need to construct objects with conditional logic
- You want to construct a composite object step by step

## Key Characteristics

| Feature | Details |
|---------|---------|
| **Separation of Concerns** | Construction logic is separated from the product class |
| **Method Chaining** | Builder methods return `this` to enable fluent interface |
| **Step-by-Step Construction** | Object is built incrementally through multiple method calls |
| **Flexibility** | Easy to add optional parameters without changing the API |
| **Immutability** | Product object is typically immutable after construction |
| **Validation** | Can validate parameters during construction or at build time |

## Real-World Analogies

- **House Construction**: A builder assembles different components (walls, roof, doors, windows) step by step
- **Meal Preparation**: Building a meal with optional additions (sauce, spices, toppings)
- **Computer Assembly**: Building a computer with optional components (RAM, GPU, SSD)
- **Document Creation**: Building a document with optional formatting and content

## Three Implementation Variants

### **1. Simple Builder Pattern**

**File Location:** `simpleBuilder/`

**Description:** The most straightforward implementation of the Builder pattern. All builder methods are optional, and the builder returns itself to enable method chaining.

**Key Features:**
```java
HttpRequest req = new HttpRequest.HttpRequestBuilder()
    .setUrl("localhost:127:0:0:1")
    .setMethod("POST")
    .addHeader("Token", "32r23k")
    .addQueryParam("SQL", "234")
    .setBody("{name:Naman}")
    .setTimeout(1)
    .build();
```

**Advantages:**
- ✅ Simple and straightforward
- ✅ Flexible (all parameters are optional)
- ✅ Easy to understand and maintain
- ✅ Standard fluent interface pattern
- ✅ Easy to add new optional parameters

**Disadvantages:**
- ❌ No enforcement of mandatory parameters during construction
- ❌ Invalid states might only be detected at `build()` time
- ❌ No compile-time type safety for required steps
- ❌ Methods can be called in any order
- ❌ Harder to enforce mandatory steps

**Best For:**
- Objects with mostly optional parameters
- Simple domains where flexibility is more important than constraint
- Rapid development scenarios
- Cases where validation happens at build time

**Time Complexity:** O(n) where n is the number of parameters set  
**Space Complexity:** O(1) for each method call

**Interview Point:** *"This is the basic Builder pattern. It's simple and flexible but doesn't enforce mandatory parameters at compile time. Validation happens at the `build()` method."*

---

### **2. Step Builder Pattern (Fluent Interface with Type Safety)**

**File Location:** `stepBuilder/`

**Description:** An advanced implementation that uses multiple interfaces to enforce a specific sequence of builder calls. Each interface represents a step in the construction process, and only specific methods are available at each step.

**Key Features:**
```java
HttpRequest req = new HttpRequest.HttpRequestBuilder()
    .setUrl("https://diginode.in")           // Required: UrlStep → MethodStep
    .setMethod("GET")                         // Required: MethodStep → HeaderStep
    .addHeader("Content-type", "Application/json")  // Required: HeaderStep → OptionalStep
    .build();                                 // build() available from OptionalStep
```

**Implementation Details:**

The pattern uses multiple interfaces to guide the construction:

```java
interface UrlStep {
    MethodStep setUrl(String url);        // First step: must set URL
}

interface MethodStep {
    HeaderStep setMethod(String method);    // Second step: must set Method
}

interface HeaderStep {
    OptionalStep addHeader(String key, String value);  // Third step: must add header
}

interface OptionalStep {
    OptionalStep setBody(String body);
    OptionalStep addQueryParam(String key, String value);
    OptionalStep setTimeout(int timeout);
    HttpRequest build();                    // Final step: build the object
}

public static class HttpRequestBuilder implements UrlStep, MethodStep, HeaderStep, OptionalStep {
    // Implements all interfaces to enforce step sequence
}
```

**How It Works:**

1. `new HttpRequestBuilder()` returns a `UrlStep` interface type
2. After calling `setUrl()`, you get a `MethodStep` interface (can only call `setMethod()`)
3. After calling `setMethod()`, you get a `HeaderStep` interface (can only call `addHeader()`)
4. After calling `addHeader()`, you get an `OptionalStep` interface (optional parameters and `build()`)
5. Only `OptionalStep` has the `build()` method

**Advantages:**
- ✅ **Compile-time type safety** — Mandatory steps are enforced by the compiler
- ✅ **Prevents invalid sequences** — Wrong method order causes compilation error
- ✅ **Clear construction workflow** — Each step is explicit and predictable
- ✅ **IDE support** — Autocomplete shows only available methods at each step
- ✅ **Impossible to miss required fields** — Compiler ensures all required fields are set
- ✅ **Self-documenting** — The interface names document the construction flow

**Disadvantages:**
- ❌ More complex to implement
- ❌ Requires creating multiple interfaces
- ❌ Less flexible (must follow exact sequence)
- ❌ Can't skip steps or set in different order
- ❌ More code to maintain

**Example Error (If Done Wrong):**
```java
// This code won't compile:
HttpRequest req = new HttpRequest.HttpRequestBuilder()
    .setUrl("https://diginode.in")
    .build();  // Error: The method build() is undefined for the type HeaderStep
    // Must call addHeader() or setMethod() first
```

**Best For:**
- Objects with specific mandatory step sequences
- High-reliability systems where construction order matters
- Cases where type safety is critical
- Complex domain models with strict validation needs
- Interview-level sophisticated solutions

**Time Complexity:** O(n) where n is the number of parameters set  
**Space Complexity:** O(1) for each method call

**Interview Point:** *"This is an advanced Builder pattern with compile-time type safety. Each interface represents a mandatory construction step. The compiler prevents calling methods in the wrong order, making invalid object states impossible."*

---

### **3. Director Builder Pattern**

**File Location:** `directorBuilder/`

**Description:** The Director pattern combines the Builder with a Director class that encapsulates common construction patterns. The Director knows how to construct standard configurations of a complex object.

**Key Components:**

**Builder:**
```java
public static class HttpRequestBuilder {
    // Same builder implementation as Simple Builder
    public HttpRequestBuilder setUrl(String url) { ... }
    public HttpRequestBuilder setMethod(String method) { ... }
    // ... other methods
    public HttpRequest build() { ... }
}
```

**Director:**
```java
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
```

**Usage:**
```java
// Simple GET request
HttpRequest getReq = HttpRequestDirector.createHttpGetRequest("https://diginode.in");
getReq.execute();

// JSON POST request with all standard headers and params
HttpRequest jsonReq = HttpRequestDirector.createHttpJsonRequest(
    "https://diginode.in", 
    "{name:Mishti}"
);
jsonReq.execute();
```

**Advantages:**
- ✅ **Encapsulates Complex Logic** — Common construction patterns are centralized
- ✅ **Reduces Code Duplication** — Standard configurations are defined once
- ✅ **Easy to Use** — Client code is simple and readable
- ✅ **Maintainability** — Changes to standard configurations affect all places
- ✅ **Semantic Clarity** — `createHttpGetRequest()` is clearer than `new Builder().setMethod("GET").build()`
- ✅ **Hides Complexity** — Clients don't need to know builder details

**Disadvantages:**
- ❌ Less flexible (limited to predefined patterns)
- ❌ Adding new patterns requires modifying Director
- ❌ Overkill for simple objects
- ❌ Hides the Builder from clients who might need customization

**When to Add Director:**
```java
// Good: Common, reusable patterns
HttpRequest.createHttpGetRequest(url);
HttpRequest.createHttpJsonRequest(url, body);

// Maybe Director: Less common combinations
HttpRequest.createHttpXmlRequest(url, xmlBody);  // If many use cases

// Not with Director: Highly custom one-off requests
new HttpRequest.HttpRequestBuilder()
    .setUrl(url)
    .setMethod("PATCH")
    .addHeader("X-Custom-Header", "value")
    .build();
```

**Best For:**
- Objects with well-known, frequently-used construction patterns
- Enterprise applications with domain-specific configurations
- When you need to encapsulate complex construction logic
- Reducing boilerplate for clients
- Systems with versioning requirements

**Time Complexity:** O(n) where n is the number of parameters for that specific request type  
**Space Complexity:** O(1) per director method

**Interview Point:** *"The Director pattern complements the Builder by encapsulating common construction patterns. Instead of clients using the Builder directly, they call semantic methods like `createHttpGetRequest()` that handle all the configuration details."*

---

## Comparison of All Three Implementations

| Aspect | Simple Builder | Step Builder | Director Builder |
|--------|---|---|---|
| **Complexity** | Low | High | Medium |
| **Type Safety** | Runtime | Compile-time | Runtime |
| **Flexibility** | Very High | Low | Medium |
| **Parameter Order** | Any | Fixed | Fixed (per pattern) |
| **Mandatory Steps** | Runtime Validation | Compile-time Enforcement | Runtime Validation |
| **Ease of Use** | Easy | Medium | Very Easy (for standard patterns) |
| **Code Reusability** | Low (client configures) | N/A | High (encapsulated patterns) |
| **Best For** | Simple, flexible objects | Safety-critical systems | Common domain patterns |
| **Interview Level** | Basic | Advanced | Intermediate |

---

## Builder Pattern Benefits

| Benefit | Explanation |
|---------|-------------|
| **Solves Telescoping Constructor Problem** | No need for multiple constructors with different parameter combinations |
| **Fluent Interface** | Method chaining makes code more readable and elegant |
| **Optional Parameters** | Easy to handle objects with many optional fields |
| **Immutability** | Objects can be truly immutable; all state set during construction |
| **Validation Control** | Can validate at each step or at the final `build()` call |
| **Clear Intent** | Code clearly shows what parameters are being set |
| **Extensibility** | Easy to add new optional parameters without breaking existing code |
| **Separation of Concerns** | Construction logic is separate from business logic |

## Builder Pattern Drawbacks

| Drawback | Explanation |
|----------|-------------|
| **More Code** | Requires writing separate builder classes/interfaces |
| **Performance Overhead** | Creating intermediate builder objects has minimal but non-zero overhead |
| **Complexity** | Overkill for simple objects with few parameters |
| **Boilerplate** | Repetitive getter/setter-like methods in builder |
| **Learning Curve** | Step Builder variant is complex for beginners |

## When to Use Builder Pattern

✅ **Use Builder Pattern when:**
- Creating objects with **10+ parameters** (or many optional parameters)
- You have **multiple ways** to construct similar objects
- You want **immutable objects**
- Constructor parameters **have defaults or dependencies**
- You need **validation** during construction
- You want **readable, self-documenting** code

❌ **Avoid Builder Pattern when:**
- Object has **few simple parameters**
- All parameters are **mandatory and independent**
- Performance is **critical** (minimal but construction overhead exists)
- Simplicity is more important than flexibility

## Design Pattern Relationships

| Pattern | Relationship |
|---------|-------------|
| **Abstract Factory** | Both are creational; Factory creates families, Builder constructs complex objects |
| **Composite** | Builder often creates Composite structures |
| **Factory Method** | Builder is like an alternative to Factory for complex object creation |
| **Strategy** | Director pattern is similar—encapsulates algorithms/patterns |
| **Decorator** | Both involve step-by-step augmentation |

## Common Use Cases in Real-World Applications

1. **HTTP Request Building** (shown in implementations)
   ```java
   HttpRequest request = new HttpRequestBuilder()
       .setUrl("example.com")
       .setMethod("POST")
       .addHeader("Authorization", "Bearer token")
       .setBody(jsonBody)
       .build();
   ```

2. **SQL Query Building**
   ```java
   Query query = new QueryBuilder()
       .select("id", "name", "email")
       .from("users")
       .where("age > ?", 18)
       .orderBy("name")
       .build();
   ```

3. **UI Component Building**
   ```java
   Button button = new ButtonBuilder()
       .setText("Click me")
       .setColor(Color.BLUE)
       .setSize(100, 50)
       .setClickListener(onClickHandler)
       .build();
   ```

4. **Configuration Objects**
   ```java
   DatabaseConfig config = new DatabaseConfigBuilder()
       .host("localhost")
       .port(5432)
       .database("mydb")
       .username("user")
       .password("password")
       .connectionPool(10)
       .build();
   ```

5. **Email Message Building**
   ```java
   Email email = new EmailBuilder()
       .to("recipient@example.com")
       .subject("Hello")
       .body("This is the message")
       .addAttachment("file.pdf")
       .build();
   ```

## Interview Questions & Answers

**Q: What problem does the Builder Pattern solve?**

A: The Builder Pattern solves the "telescoping constructor problem"—when you have a class with many parameters, some optional. Instead of creating multiple constructors for every combination, you use a builder that lets you set parameters individually using a fluent interface.

**Q: What's the difference between Simple Builder and Step Builder?**

A: 
- **Simple Builder**: All parameters are optional. The builder is flexible but doesn't enforce ordering or mandatory fields at compile time.
- **Step Builder**: Uses interfaces to enforce a specific construction sequence. Mandatory steps are enforced by the compiler, making invalid states impossible.

**Q: When would you use Director Builder Pattern?**

A: Use the Director pattern when you have well-known, frequently-used construction patterns. The Director encapsulates those patterns so clients don't need to know about the Builder. For example, `HttpRequestDirector.createHttpGetRequest()` is clearer than `new Builder().setMethod("GET").build()`.

**Q: How is Builder different from Factory?**

A: 
- **Factory**: Returns a ready-made object of a specific type.
- **Builder**: Step-by-step construction process; flexible and can return different configurations.

**Q: Can Builder Pattern be used with immutable objects?**

A: Yes! In fact, it's ideal for immutable objects. The builder is mutable, but once `build()` is called, the resulting object is immutable.

**Q: What's the time complexity of creating an object with Builder?**

A: O(n) where n is the number of parameters set. Each method call is O(1), but setting all parameters takes O(n) total.

## Key Takeaways

- **Builder Pattern** separates object construction from representation
- **Simple Builder** is flexible but requires runtime validation
- **Step Builder** provides compile-time type safety through interfaces
- **Director Builder** encapsulates common construction patterns
- Use **method chaining** to enable fluent, readable interfaces
- **Immutability** is often achieved with Builder Pattern
- Choose the variant based on your **safety vs. flexibility** needs

## Time & Space Complexity

| Aspect | Complexity |
|--------|------------|
| **Construction Time** | O(n) — n = number of parameters set |
| **Build Time** | O(1) — assuming validation is O(1) |
| **Method Call Time** | O(1) — each setter call is constant |
| **Space Complexity** | O(1) — builder stores constant references |

## Related Patterns

- **Abstract Factory** — Creates families of objects
- **Composite Pattern** — Builder often creates composite structures
- **Factory Method** — Alternative for object creation
- **Prototype Pattern** — Another creational pattern for object creation
- **Strategy Pattern** — Similar structure to Director pattern

## Implementation Files Reference

```
p14_builderPattern/
├── simpleBuilder/
│   ├── SimpleBuilder.java       // Main client code
│   └── HttpRequest.java         // Object with inner builder class
├── stepBuilder/
│   ├── Main.java                // Client code demonstrating type safety
│   └── HttpRequest.java         // Object with interfaces for step enforcement
└── directorBuilder/
    ├── DirectorBuilder.java     // Main client code
    ├── HttpRequest.java         // Object with standard builder
    └── HttpRequestDirector.java // Director encapsulating patterns
```

## Summary Table: When to Use Each Variant

| Situation | Use |
|-----------|-----|
| **Simple flexible objects** | Simple Builder |
| **Safety-critical, mandatory steps** | Step Builder |
| **Recurring construction patterns** | Director Builder |
| **Learning the pattern** | Simple Builder first, then Step |
| **Production enterprise code** | Depends on requirements; often Step or Director |
