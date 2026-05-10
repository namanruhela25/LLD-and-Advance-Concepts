# FlyWeight Design Pattern

## Definition

The **FlyWeight Design Pattern** is a structural design pattern that uses sharing to support a large number of fine-grained objects efficiently. It reduces memory consumption and garbage collection overhead by sharing common state (intrinsic state) among multiple objects, while keeping unique state (extrinsic state) separate.

## Core Philosophy

**Main Insight:** Many objects can be **represented efficiently** by separating their **immutable shared properties** from their **mutable unique properties**.

Instead of creating millions of individual objects with duplicate data, create fewer shared objects and pass unique data as parameters.

---

## Problem Statement (Interview Context)

### The Challenge: Creating Millions of Objects

**Scenario:** You're building a space shooter game (like GTA 5) with 1 million asteroids on screen. Each asteroid has:

```java
// ❌ Naive Approach: Each asteroid stores everything
class Asteroid {
    private int length, width, weight;        // Same for all Red asteroids!
    private String color, texture, material;  // Same for all Red asteroids!
    private int posX, posY;                   // UNIQUE for each asteroid
    private int velocityX, velocityY;         // UNIQUE for each asteroid
}

// Creating 1 million asteroids:
List<Asteroid> asteroids = new ArrayList<>();
for (int i = 0; i < 1_000_000; i++) {
    asteroids.add(new Asteroid(25, 25, 250, "Red", "Rocky", "Iron", ...));
    // ↑ Duplicates same properties for 333,333 red asteroids!
}
```

**Problems:**

1. **Memory Waste**: 
   - 1 million objects × (length + width + weight + color + texture + material) memory
   - 999,999 of the Asteroid objects share identical properties with the first red one!

2. **Garbage Collection Overhead**:
   - Creating 1 million objects → massive GC pressure
   - Millions of objects to track and clean up

3. **Cache Misses**:
   - Scattered memory locations for duplicate data
   - CPU cache becomes ineffective

4. **Performance Degradation**:
   - Slower rendering
   - More memory allocations
   - Worse frame rates in games

### Real-World Impact

```
WITHOUT FlyWeight:
- 1,000,000 asteroids
- Each stores: 3 ints + 3 strings ≈ 180 bytes
- Total: ~180 MB (often crashes or runs at 5 FPS)

WITH FlyWeight:
- 3 unique asteroid types (Red, Blue, Gray)
- Only 3 AsteroidFlyweight objects created
- 1 million AsteroidContext objects (16 bytes each)
- Total: ~16 MB (runs smoothly at 60 FPS!)
```

**This is where FlyWeight Pattern comes in!**

---

## Core Concepts

### Intrinsic State (Shared)

**Definition:** Immutable data that is **same across many objects** and can be **shared safely**.

**Characteristics:**
- Cannot change after object creation
- Shared by multiple objects
- Same resource cost regardless of usage
- Thread-safe (immutable)

**In Asteroid Example:**
```java
private int length;      // Same for all Red asteroids
private int width;       // Same for all Red asteroids
private int weight;      // Same for all Red asteroids
private String color;    // Same for all Red asteroids
private String texture;  // Same for all Red asteroids
private String material; // Same for all Red asteroids
```

### Extrinsic State (Unique)

**Definition:** Mutable data that is **unique to each object** and **cannot be shared**.

**Characteristics:**
- Changes frequently
- Specific to each instance
- Passed as parameters at runtime
- Cannot be stored in shared flyweight

**In Asteroid Example:**
```java
private int posX;        // UNIQUE position X
private int posY;        // UNIQUE position Y
private int velocityX;   // UNIQUE velocity X
private int velocityY;   // UNIQUE velocity Y
```

### Separation Principle

```
Original Object
├── Intrinsic Properties (shared)
└── Extrinsic Properties (unique)
         ↓ (SEPARATION)
Flyweight Object (shared)
└── Intrinsic Properties
Context Object (unique to each use)
└── Extrinsic Properties
```

---

## Architecture

### Components

**1. FlyWeight Interface/Class**
```java
class AsteroidFlyweight {
    // Intrinsic state ONLY - shared among instances
    private int length, width, weight;
    private String color, texture, material;
    
    public AsteroidFlyweight(int l, int w, int wt, String col, String tex, String mat) {
        this.length = l;
        this.width = w;
        // ... initialize all intrinsic properties
    }
    
    public void render(int posX, int posY, int velocityX, int velocityY) {
        // Uses intrinsic properties + extrinsic parameters
    }
}
```

**Key Point:** Constructor takes ONLY intrinsic properties. Once created, flyweight is immutable.

**2. Context/Extrinsic State Holder**
```java
class AsteroidContext {
    private AsteroidFlyweight flyweight;  // Reference to shared object
    private int posX, posY;                // Extrinsic: unique position
    private int velocityX, velocityY;      // Extrinsic: unique velocity
    
    public AsteroidContext(AsteroidFlyweight fw, int posX, int posY, int velX, int velY) {
        this.flyweight = fw;
        this.posX = posX;
        this.posY = posY;
        // ... initialize extrinsic properties
    }
    
    public void render() {
        flyweight.render(posX, posY, velocityX, velocityY);  // Pass extrinsic to flyweight
    }
}
```

**Key Point:** Context holds reference to flyweight and adds extrinsic state.

**3. Factory**
```java
class AsteroidFactory {
    private static Map<String, AsteroidFlyweight> flyweights = new HashMap<>();
    
    public static AsteroidFlyweight getAsteroid(int length, int width, int weight,
                                                 String color, String texture, String material) {
        // Create unique key from intrinsic properties
        String key = length + "_" + width + "_" + weight + "_" + color + "_" + texture + "_" + material;
        
        // Return existing flyweight if already created
        if (!flyweights.containsKey(key)) {
            flyweights.put(key, new AsteroidFlyweight(length, width, weight, color, texture, material));
        }
        return flyweights.get(key);
    }
}
```

**Key Point:** Factory caches flyweights by intrinsic properties to enable sharing.

---

## How It Works: Step-by-Step

### Scenario: Creating 1,000,000 Asteroids (3 types: Red, Blue, Gray)

**Step 1: Create Flyweights (Only 3 objects created)**
```
Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron")
→ Key: "25_25_250_Red_Rocky_Iron"
→ Not in cache, create NEW AsteroidFlyweight
→ Store in cache
→ Return reference

Factory.getAsteroid(35, 35, 350, "Blue", "Metallic", "Stone")
→ Key: "35_35_350_Blue_Metallic_Stone"
→ Not in cache, create NEW AsteroidFlyweight
→ Store in cache
→ Return reference

Factory.getAsteroid(45, 45, 450, "Gray", "Icy", "Ice")
→ Key: "45_45_450_Gray_Icy_Ice"
→ Not in cache, create NEW AsteroidFlyweight
→ Store in cache
→ Return reference

// Now cache has 3 flyweight objects
```

**Step 2: Create Contexts with Unique Properties (1,000,000 objects)**
```
for (i = 0 to 999,999) {
    type = i % 3;  // Cycles through 0, 1, 2 (Red, Blue, Gray)
    
    flyweight = Factory.getAsteroid(...)  // Returns EXISTING from cache!
    
    // Create context with UNIQUE extrinsic properties
    context = new AsteroidContext(
        flyweight,
        100 + i * 50,    // Each has unique posX
        200 + i * 30,    // Each has unique posY
        1,              // Velocity
        2
    );
    
    asteroids.add(context);
}
```

**Step 3: Rendering**
```
for (AsteroidContext context : asteroids) {
    context.render();
    // Internally calls:
    // flyweight.render(context.posX, context.posY, context.velocityX, context.velocityY)
}
```

---

## Memory Analysis

### Without FlyWeight

```
Per Asteroid Object:
├── int length (4 bytes)
├── int width (4 bytes)
├── int weight (4 bytes)
├── String color (~40 bytes)         ← DUPLICATE!
├── String texture (~40 bytes)       ← DUPLICATE!
├── String material (~40 bytes)      ← DUPLICATE!
├── int posX (4 bytes)
├── int posY (4 bytes)
├── int velocityX (4 bytes)
└── int velocityY (4 bytes)
───────────────────────
Total: ~180 bytes per object

For 1,000,000 asteroids:
1,000,000 × 180 bytes = 180,000,000 bytes = ~180 MB
```

### With FlyWeight

```
Factory Flyweights (only 3 objects):
├── AsteroidFlyweight #1: ~140 bytes (length, width, weight, color, texture, material)
├── AsteroidFlyweight #2: ~140 bytes
└── AsteroidFlyweight #3: ~140 bytes
Total: ~420 bytes

Context Objects (1,000,000 objects):
├── Reference to flyweight: 8 bytes
├── int posX: 4 bytes
├── int posY: 4 bytes
├── int velocityX: 4 bytes
└── int velocityY: 4 bytes
Total per context: 24 bytes

For 1,000,000 asteroids:
(1,000,000 × 24) + 420 = 24,000,420 bytes ≈ ~24 MB

SAVINGS: 180 MB → 24 MB = 7.5x memory reduction! 🎉
```

---

## Interview Deep Dive

### Q1: What's the difference between Intrinsic and Extrinsic state?

**Answer:**

| Aspect | Intrinsic | Extrinsic |
|--------|-----------|-----------|
| **Mutability** | Immutable (never changes) | Mutable (changes often) |
| **Sharing** | Shared across multiple objects | Unique to each object |
| **Storage** | In FlyWeight | In Context |
| **Thread-Safety** | Always thread-safe (immutable) | Requires synchronization if shared |
| **Cost** | Worth sharing | Wasteful to share |
| **Example** | Red asteroid's color | Asteroid's position |

**Key Insight:** We ONLY share intrinsic state. Extrinsic state is always kept separate.

---

### Q2: Why does Factory use a HashMap for caching?

**Answer:**

```java
// HashMap by intrinsic properties
String key = length + "_" + width + "_" + weight + "_" + color + "_" + texture + "_" + material;

if (!flyweights.containsKey(key)) {
    flyweights.put(key, new AsteroidFlyweight(...));
}
return flyweights.get(key);
```

**Why HashMap?**
- **Fast Lookup**: O(1) average case to check if flyweight exists
- **Key-Value Pairing**: Intrinsic properties → Flyweight object
- **Reuse Detection**: Quickly identifies when same object already created
- **Deterministic**: Same intrinsic properties always return same object

**Example:**
```
First Request: Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron")
→ Creates NEW flyweight
→ Stores in cache with key "25_25_250_Red_Rocky_Iron"

Second Request (asteroid #333): Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron")
→ SAME KEY!
→ Returns EXISTING flyweight from cache (no new object created!)

Third Request (asteroid #666): Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron")
→ SAME KEY!
→ Returns EXISTING flyweight from cache (reused!)
```

---

### Q3: How does FlyWeight help with GC overhead?

**Answer:**

**Without FlyWeight:**
```
Creating 1,000,000 asteroids
GC must track → mark → sweep 1,000,000 objects
Memory fragmentation severe
Young generation fills up fast
Frequent GC pauses (STW - Stop The World)
Frame rate drops during GC
```

**With FlyWeight:**
```
Creating 3 flyweights + 1,000,000 contexts
GC primarily tracks 1,000,000 small context objects (24 bytes each)
Memory layout more compact
Young generation fills slower
GC runs more efficiently
Minimal frame rate impact
```

**GC Performance:**
- Fewer objects = faster GC
- Smaller objects = better memory locality
- Less allocation = less fragmentation

---

### Q4: Can FlyWeight objects be modified after creation?

**Answer:** No! FlyWeight objects MUST be immutable.

**Why?**
```java
// If we allow modification:
flyweight1.setColor("Red → Blue");  // ❌ Changes shared object!

// All 333,333 asteroids using this flyweight now have wrong color!
for (AsteroidContext context : asteroids) {
    if (context.flyweight == flyweight1) {
        // All affected! Color is now Blue for all
    }
}
```

**Solution:** Make FlyWeight classes immutable
```java
class AsteroidFlyweight {
    private final int length;        // ← final keyword
    private final int width;         // ← final keyword
    private final int weight;        // ← final keyword
    private final String color;      // ← final keyword
    private final String texture;    // ← final keyword
    private final String material;   // ← final keyword
    
    // Constructor only, no setters
    public AsteroidFlyweight(int l, int w, int wt, String col, String tex, String mat) {
        this.length = l;
        this.width = w;
        // ...
    }
}
```

**Thread Safety Bonus:** Immutability makes FlyWeight thread-safe automatically!

---

### Q5: What if two different asteroids need different "shared" properties?

**Answer:** They're not truly intrinsic then—move to extrinsic!

**Example Problem:**
```
What if asteroid #50 needs a slightly different Red color shade?
"Red" vs "Deep Red"
```

**Approaches:**

1. **Option A: Create Separate FlyWeight**
```java
Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron");
Factory.getAsteroid(25, 25, 250, "Deep Red", "Rocky", "Iron");  // New flyweight
// Now 4 flyweights instead of 3
```

2. **Option B: Move to Extrinsic**
```java
class AsteroidContext {
    AsteroidFlyweight flyweight;
    String colorShade;  // Add as extrinsic: "Deep Red", "Bright Red", etc.
    
    void render() {
        flyweight.render(colorShade, posX, posY, ...);
    }
}
```

**Decision:** Depends on frequency of variations.

---

### Q6: Can you have nested FlyWeights?

**Answer:** Yes! One FlyWeight can contain other FlyWeights.

**Example:**
```java
class Planet implements Flyweight {
    String name;           // Intrinsic
    String material;       // Intrinsic
    List<Moon> moons;      // Each moon is also a flyweight!
}

class Moon implements Flyweight {
    String moonType;       // Intrinsic
    int radius;           // Intrinsic
}
```

**Benefits:**
- Compound object sharing
- Hierarchical memory optimization

---

### Q7: What's the Factory's role exactly?

**Answer:** Three key responsibilities:

1. **Creation Control**
   - Decides when to create new flyweight
   - Decides when to reuse existing

2. **Caching Management**
   - Maintains pool of created flyweights
   - Prevents duplicates

3. **Identity Preservation**
   - Two flyweights with same intrinsic properties are same object (identity equality, not just equality)
   ```java
   AsteroidFlyweight fw1 = Factory.getAsteroid(...);
   AsteroidFlyweight fw2 = Factory.getAsteroid(...);
   fw1 == fw2;  // TRUE (same object reference, not just equal)
   ```

**Factory Pattern:**
```
Factory is itself a design pattern for object creation.
FlyWeight uses Factory to manage creation and caching.
They complement each other perfectly.
```

---

### Q8: What happens if Factory.cleanup() is called?

**Answer:**
```java
public static void cleanup() {
    flyweights.clear();  // Clear the cache
}
```

**Consequences:**
```
After cleanup():
- All cached flyweights removed
- Next request creates fresh flyweights
- Old flyweights unreachable (GC eligible)

BUT:
- AsteroidContext objects still hold references!
- Those references become "dangling"
- Calling render() might crash or behave unpredictably

Solution: Only cleanup when sure no contexts reference flyweights
```

---

### Q9: How would you handle dynamic intrinsic properties?

**Answer:** Use a configuration object or database:

```java
class MaterialDatabase {
    static Map<String, Material> materials = new HashMap<>();
    
    static {
        materials.put("Iron", new Material("Iron", Color.GRAY, Density.HIGH));
        materials.put("Stone", new Material("Stone", Color.BROWN, Density.MEDIUM));
    }
    
    public static Material getMaterial(String name) {
        return materials.get(name);
    }
}

// In Asteroid
class AsteroidFlyweight {
    private Material material;  // Reference to material (itself a flyweight)
    
    public AsteroidFlyweight(int l, int w, int wt, String materialName, ...) {
        this.material = MaterialDatabase.getMaterial(materialName);
        // One level of indirection for true intrinsic properties
    }
}
```

---

### Q10: Can you have 1 million contexts but only 1 flyweight?

**Answer:** Yes! All asteroids of same type would share single flyweight.

```
Scenario: All 1,000,000 asteroids are identical ("Red", "Rocky", "Iron")

Factory creates: 1 AsteroidFlyweight
Contexts created: 1,000,000 AsteroidContext objects

Each context holds reference to SAME flyweight object:
context0 → flyweight
context1 → flyweight (SAME)
context2 → flyweight (SAME)
...
context999999 → flyweight (SAME)

Memory usage is EXTREMELY optimized:
Only 140 bytes for properties + 1 million * 24 bytes for contexts
Total: ~24 MB (vs 180 MB without pattern)
```

---

## Common Use Cases

### 1. Game Development
```java
// Trees in a forest
class TreeFlyweight {
    String species;      // Oak, Pine, etc. (intrinsic)
    Texture bark;        // (intrinsic)
    Model meshData;      // 3D model (intrinsic)
}

class TreeContext {
    TreeFlyweight flyweight;
    int posX, posY, posZ;  // Position in world (extrinsic)
    int scale;             // Size (extrinsic)
}

// 10,000 trees in scene, only 5 unique types
// Much memory saved with 5 flyweights + 10,000 contexts
```

### 2. Text Editor
```java
// Character in document
class CharacterFlyweight {
    char character;      // 'A', 'B', etc. (intrinsic)
    Font font;           // Arial, Times New Roman (intrinsic)
    Color color;         // Font color (intrinsic)
}

class CharacterContext {
    CharacterFlyweight flyweight;
    int posX, posY;      // Position (extrinsic)
    int bold;            // Applies to this instance (extrinsic)
}

// 1 million characters, ~50 unique font+color combinations
// Huge memory savings
```

### 3. Web Server Request Handling
```java
// String literals in HTTP requests
class String {
    char[] value;        // Actual string content (intrinsic)
    int hash;            // Cached hash (intrinsic)
}

// Java's String Interning uses FlyWeight concept
String s1 = "Hello";
String s2 = "Hello";
s1 == s2;  // TRUE! Strings are interned (same object)
```

### 4. Graphics Rendering
```java
// Texture object
class TextureFlyweight {
    byte[] pixelData;    // Image data (intrinsic)
    int width, height;   // Dimensions (intrinsic)
    Format format;       // Compression format (intrinsic)
}

class TextureInstance {
    TextureFlyweight texture;
    int x, y;            // Position (extrinsic)
    int rotation;        // Rotation (extrinsic)
    int scale;           // Scale (extrinsic)
}
```

### 5. Connection Pooling
```java
// Database Connection
class ConnectionFlyweight {
    String url;          // Connection string (intrinsic)
    String user;         // Username (intrinsic)
    Properties config;   // Configuration (intrinsic)
}

class ConnectionContext {
    ConnectionFlyweight connection;
    Timestamp acquired;  // When acquired (extrinsic)
    int queryCount;      // Queries executed (extrinsic)
}

// Pool reuses 10 connections for 1000 concurrent requests
```

---

## Advantages

✅ **Massive Memory Savings**
- 7.5x reduction in asteroid example
- Scales with number of duplicate objects

✅ **Improved Cache Performance**
- Smaller objects = better CPU cache utilization
- Memory locality improves

✅ **Reduced GC Pressure**
- Fewer objects to track
- Faster garbage collection
- Fewer pauses in real-time applications

✅ **Shared Responsibility**
- Multiple contexts, single flyweight
- Reduces duplication

✅ **Performance Boost**
- Games: Better frame rates (fewer GC pauses)
- Web servers: Handle more connections
- Graphics: Render more objects

✅ **Scalability**
- Can handle millions of objects
- Practical to have billions of lightweight objects

---

## Disadvantages

❌ **Implementation Complexity**
- Requires careful separation of state
- More classes (Flyweight, Context, Factory)
- Harder to understand for beginners

❌ **Thread Safety**
- Shared objects need synchronization
- Extrinsic state must be thread-safe
- Can introduce bugs if not careful

❌ **CPU Overhead (Indirection)**
```java
// Without FlyWeight: Direct access
asteroid.color;

// With FlyWeight: One level of indirection
context.flyweight.color;  // ← Extra pointer dereference
```
- Accessing intrinsic properties requires pointer lookup
- For simple/fast objects, this overhead might outweigh memory savings

❌ **Complexity in Modification**
- Can't modify flyweight (must be immutable)
- If properties change, can't reuse
- Must create new flyweight for variations

❌ **Hidden Coupling**
- Context intimately coupled with Flyweight
- Changes to Flyweight affect all Contexts
- Harder to refactor

❌ **Testing Difficulty**
- Hard to mock shared objects
- State spread across Flyweight and Context
- Complex test setup

---

## When to Use FlyWeight

✅ **Use When:**
- Millions of similar objects created
- Intrinsic state is significant portion of memory
- Many objects share common state
- Memory is constraint (mobile, embedded systems, large-scale servers)
- Performance/responsiveness critical (games, graphics, real-time systems)
- Object identity preservation needed

❌ **Avoid When:**
- Small number of objects (overhead not worth it)
- No shared state possible
- Objects are simple, lightweight
- Modification of properties is frequent
- Simplicity is priority over performance
- Team unfamiliar with pattern

---

## Design Principle Analysis

### SRP (Single Responsibility Principle)
```
✅ Followed:
- FlyWeight: Manage intrinsic state
- Context: Manage extrinsic state
- Factory: Create and cache flyweights
Each class has one reason to change
```

### DIP (Dependency Inversion Principle)
```
✅ Followed:
- Contexts depend on FlyWeight abstraction
- Factory decouples creation from usage
- High-level code doesn't depend on low-level implementation
```

### ISP (Interface Segregation Principle)
```
✅ Followed:
- Minimal interface (just create/retrieve)
- Clients don't need unused methods
```

---

## Time & Space Complexity

| Operation | Time | Space |
|-----------|------|-------|
| Get existing flyweight | O(1) | O(1) |
| Create new flyweight (first time) | O(1) | O(1) |
| HashMap lookup + insert | O(1) average | O(1) |
| Render all asteroids | O(n) | O(1) |
| Full traversal | O(n) | O(1) |
| Memory savings | - | O(n - m) where m = unique types |

---

## Common Pitfalls

### ❌ Pitfall 1: Mutable Intrinsic State

```java
// ❌ BAD: Changes affect all contexts!
class BadFlyweight {
    private String color;  // Not final!
    
    public void setColor(String c) {
        this.color = c;  // ← All contexts affected!
    }
}

// ✅ GOOD: Immutable
class GoodFlyweight {
    private final String color;  // final
    // No setters
}
```

### ❌ Pitfall 2: Forgetting Factory Pattern

```java
// ❌ BAD: Clients create flyweights directly
new AsteroidFlyweight(25, 25, 250, "Red", "Rocky", "Iron");
new AsteroidFlyweight(25, 25, 250, "Red", "Rocky", "Iron");  // Different object!

// ✅ GOOD: Use Factory for sharing
Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron");  // Object 1
Factory.getAsteroid(25, 25, 250, "Red", "Rocky", "Iron");  // SAME object
```

### ❌ Pitfall 3: Taking Key Generation Too Lightly

```java
// ❌ BAD: Keys might not be unique
String key = asteroid.type;  // "Red" and "RED" are different!

// ✅ GOOD: Comprehensive, deterministic keys
String key = length + "_" + width + "_" + weight + "_" 
           + color + "_" + texture + "_" + material;
```

### ❌ Pitfall 4: Premature Optimization

```java
// ❌ BAD: Using FlyWeight for 10 objects
for (int i = 0; i < 10; i++) {
    asteroids.add(Factory.getAsteroid(...));  // Overkill!
}

// ✅ GOOD: Use only when many objects
if (NUM_ASTEROIDS > 10000) {
    // Use FlyWeight
} else {
    // Simple approach
}
```

### ❌ Pitfall 5: Poor Separation of State

```java
// ❌ BAD: Unclear what's intrinsic vs extrinsic
class ConfusedAsteroid {
    private int health;  // Extrinsic? But shared in pool...
    private String color;  // Intrinsic? But might change...
    private int ammo;  // Extrinsic? Only some asteroids...
}

// ✅ GOOD: Clear separation
class AsteroidFlyweight {
    private final int length;      // Intrinsic: immutable, shared
    private final String color;    // Intrinsic: immutable, shared
}

class AsteroidContext {
    private int posX, posY;        // Extrinsic: unique to instance
    private int velocityX, velocityY;  // Extrinsic: unique
}
```

---

## Memory Calculation Example

**Creating 1,000,000 asteroids with 3 types:**

```
Flyweight Objects (Created once, shared):
AsteroidFlyweight #1 (Red):
  - int length: 4 bytes
  - int width: 4 bytes
  - int weight: 4 bytes
  - String color: ~40 bytes
  - String texture: ~40 bytes
  - String material: ~40 bytes
  Total: ~132 bytes

× 3 types = ~396 bytes

Context Objects (1 per asteroid):
AsteroidContext:
  - Reference to flyweight: 8 bytes
  - int posX: 4 bytes
  - int posY: 4 bytes
  - int velocityX: 4 bytes
  - int velocityY: 4 bytes
  Total: 24 bytes

× 1,000,000 = 24,000,000 bytes

TOTAL: 24,000,396 bytes ≈ 22.8 MB

Savings: 180 MB → 22.8 MB = 87% memory reduction!
```

---

## Real-World Example Explained

### GTA 5 Game Scenario

**Problem:** Rendering a city with:
- 50,000 trees
- 5 tree types (Oak, Pine, Maple, Birch, Elm)
- Each tree has 3D model, texture, physics data

```
Without FlyWeight:
50,000 trees × ~5 MB per tree = 250 GB! 🤯
Game crashes before starting.

With FlyWeight:
- 5 Tree Flyweights: 25 MB (shared, once in memory)
- 50,000 Tree Contexts: 1 MB (just position, rotation data)
- Total: 26 MB ✅
- Smooth 60 FPS gameplay
```

**Why Context Matters:**
```
Each tree instance needs its own:
- Position (X, Y, Z)
- Rotation (Yaw, Pitch, Roll)
- Scale (width factor)
- Animation state (swaying wind)
- Health/damage

But ALL Oak trees share:
- 3D model mesh data
- Texture files
- Physics properties
- Material properties
```

---

## Interview Scenario

**Interviewer:** "Design a parking lot system. We need to efficiently store information about 10,000 parking spaces, 5000 of which are reserved. Each space has an ID, location, and whether it's reserved."

**Good Answer Structure:**

1. **Identify Intrinsic Properties:**
   - Reserved status / Type (reserved vs normal)
   - These don't change

2. **Identify Extrinsic Properties:**
   - Parking space ID
   - Location (row, column)
   - Current occupancy status
   - Vehicle info

3. **Design:**
   ```java
   // Flyweight: Immutable property
   class ParkingSpaceType {
       private final boolean reserved;
       private final double costPerHour;
   }
   
   // Context: Unique properties
   class ParkingSpace {
       private ParkingSpaceType type;
       private int spaceId;
       private Location location;
       private Vehicle occupant;
   }
   
   // Factory: Cache types
   class ParkingSpaceFactory {
       Map<String, ParkingSpaceType> types = new HashMap<>();
   }
   ```

4. **Memory Savings:**
   - 2 types (reserved, normal) vs 10,000 spaces
   - Storing type object once vs 5,000+ times

---

# Quick Notes and Diagram

![FlyWeight Pattern Diagram](FlyWeightPattern.png)

The diagram shows:

**Top Section - Core Concept:**
- Separation of intrinsic (shared) and extrinsic (unique) properties
- Why storing + playing songs together violates SRP
- Solution: separate concerns using FlyWeight + Context

**Middle Sections - Architecture:**

**Intrinsic Context:**
- AsteroidFlyWeight contains immutable shared properties (length, width, weight, color, texture)
- AsteroidFlyWeight has intrinsic state only

**Extrinsic Context:**
- AsteroidContext holds reference to flyweight
- AsteroidContext adds extrinsic state (posX, posY, velocityX, velocityY)
- Context has a reference to flyweight (has-a relationship)

**Factory & Cache:**
- FlyWeightFactory maintains cache (HashMap)
- `getAsteroidFlyweight(l, w, wt, col, texture)` returns cached or new flyweight
- Cache key made from intrinsic properties ensures reusability

**Implementation:**
- Combined: "len + width + weight + color + texture" → AndroidFlyWeight1
- Multiple contexts can reference same flyweight

**Real-World Application:**
- GTA 5 example: 1.6 million trees remain same at same coordinates (intrinsic)
- Runtime: changing objects just take reference of already created ones (extrinsic context at runtime)
- Prevents creating identical objects repeatedly

**Key Insight from Diagram:**
- Factory is central to pattern
- Multiple contexts share single flyweight
- Memory is saved on flyweights; contexts are lightweight
- Combination = efficient object creation

---

## Summary Table: FlyWeight vs Normal Approach

| Aspect | Normal Approach | FlyWeight |
|--------|---|---|
| **Objects Created** | 1,000,000 asteroids | 3 flyweights + 1,000,000 contexts |
| **Memory Per Object** | ~180 bytes | 24 bytes (context) + shared (140 bytes) |
| **Total Memory** | 180 MB | ~24 MB |
| **Modification Hope** | Change individual asteroid | Can't modify (immutable) |
| **Sharing** | No sharing | Massive sharing |
| **GC Pressure** | Million objects to track | Much smaller pressure |
| **Cache Efficiency** | Poor (scattered memory) | Excellent (compact data) |
| **Code Complexity** | Simple | Complex (factory, separation) |

---

## Key Interview Takeaways

1. **Purpose**: Reduce memory for millions of similar objects
2. **Split State**: Intrinsic (shared, immutable) vs Extrinsic (unique, mutable)
3. **Factory Pattern**: Essential for caching and reuse
4. **Immutability**: FlyWeights must be immutable to be safe
5. **Memory Savings**: 5-10x typical; 7-8x in this example
6. **GC Benefits**: Fewer/faster garbage collections
7. **Trade-off**: Complexity for performance
8. **Use Cases**: Games, graphics, large-scale systems
9. **Real-World**: String interning, texture pooling, connection pooling
10. **When Not**: Few objects, no shared state, simplicity priority

