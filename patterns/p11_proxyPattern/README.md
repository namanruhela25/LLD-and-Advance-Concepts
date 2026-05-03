# Proxy Design Pattern

## Definition

The **Proxy Design Pattern** is a structural design pattern that **provides a placeholder or surrogate for another object to control access to it**.

A Proxy acts as an intermediary between the client and the real object, allowing you to add behavior (like lazy initialization, access control, logging) before delegating to the real object.

Also known as:
- **Surrogate Pattern**
- **Agent Pattern**
- **Placeholder Pattern**

## Purpose

The Proxy pattern is used when:
- You want to delay object creation until it's actually needed (lazy initialization)
- You need to control access to an object (protection/access control)
- You want to add behavior before/after accessing an object (logging, caching)
- You need to handle remote objects or network communication
- You want to replace an expensive object with a lightweight placeholder
- You need to provide authentication/authorization checks
- You want to implement object pooling or resource management

## Key Problem It Solves

**Without Proxy Pattern (Direct Access to Resource):**
```java
Client directly accesses expensive resources:

IDisplay image = new RealImage("vacation.jpg");
// Constructor immediately loads image from disk (expensive!)

image.display();
image.display();
image.display();

Issues:
- Image loaded immediately even if never displayed
- Loading happens regardless of permission
- No caching or reuse optimization
- No access control possible
- Network resources created immediately
- Memory allocated immediately
- No control over creation or access
- Difficult to add logging/monitoring
- Can't intercept or modify behavior
- Remote calls happen immediately even if not used
```

**With Proxy Pattern (Controlled Access Through Proxy):**
```java
Client accesses through proxy:

IDisplay image = new ImageProxy("vacation.jpg");
// Image NOT loaded yet (lightweight proxy created)

image.display();
// Image loads ONLY when display() is first called
// Subsequent calls reuse cached image

image.display();
image.display();

Benefits:
- Image loaded lazily (only when needed)
- Lightweight proxy created immediately
- Resource allocation deferred
- Access can be controlled/restricted
- Behavior can be added transparently
- Caching can be implemented
- Monitoring/logging can be applied
- Remote operations handled transparently
```

---

## Core Participants

| Participant | Role |
|-------------|------|
| **Subject (Interface)** | Common interface for Proxy and RealObject; defines contract |
| **RealObject** | Expensive real object that does actual work; has resource overhead |
| **Proxy** | Placeholder for RealObject; controls access; may defer creation |
| **Client** | Uses Proxy as if it were RealObject; unaware of proxy |

---

## Types of Proxies

### **1. Virtual Proxy (Lazy Initialization Proxy)**

**Purpose:** Delay creation of expensive objects until first use

**Example: ImageProxy**
```java
public class ImageProxy implements IDisplay {
    private String fileName;
    private RealImage realImage;  // Created lazily

    public ImageProxy(String fileName) {
        this.fileName = fileName;
        // RealImage NOT created yet
        // realImage = null
    }

    @Override
    public void display() {
        if (realImage == null) {
            // First call: create real object
            realImage = new RealImage(fileName);
            // Image loaded from disk here
        }
        // Subsequent calls reuse cached object
        realImage.display();
    }
}

Usage:
  IDisplay image = new ImageProxy("large_photo.jpg");
  // Proxy created (lightweight, fast)
  // RealImage NOT created

  image.display();
  // First call: RealImage created (expensive loading)
  
  image.display();
  // Second call: reuses cached RealImage (fast)
```

**Benefits:**
- Defers expensive initialization until needed
- Improves startup time
- Saves memory if object never used
- Transparent to client

---

### **2. Protection Proxy (Access Control Proxy)**

**Purpose:** Control access to object based on permissions/conditions

**Example: DocumentProxy**
```java
class DocumentProxy implements IDocumentReader {
    private RealDocumentReader realReader;
    private User user;

    public DocumentProxy(User user) {
        this.realReader = new RealDocumentReader();
        this.user = user;
    }

    @Override
    public void unlockPDF(String filePath, String password) {
        // Check access permission before delegating
        if (!user.premiumMembership) {
            System.out.println("Access denied");
            return;  // Block access
        }
        // Permission check passed, delegate to real object
        realReader.unlockPDF(filePath, password);
    }
}

Usage:
  User user1 = new User("Naman", false);  // Non-premium
  IDocumentReader reader = new DocumentProxy(user1);
  reader.unlockPDF("file.pdf", "12345");
  // Output: "Access denied"
  
  User user2 = new User("Premium User", true);
  IDocumentReader reader2 = new DocumentProxy(user2);
  reader2.unlockPDF("file.pdf", "12345");
  // Output: Executes real operation
```

**Benefits:**
- Enforces access control
- Authentication/authorization checks
- Different behavior for different users
- Transparent access restrictions

---

### **3. Remote Proxy (Remote Object Proxy)**

**Purpose:** Represent remote object locally; handle network communication

**Example: DataServiceProxy**
```java
class DataServiceProxy implements IDataService {
    private RealDataService realService;

    public DataServiceProxy() {
        this.realService = null;  // Remote object not initialized
    }

    @Override
    public String fetchData() {
        System.out.println("Connecting to remote service...");
        
        if (realService == null) {
            // Establish connection
            realService = new RealDataService();
            // In real scenario: network connection established
        }
        
        // Delegate to remote object
        return realService.fetchData();
    }
}

Usage:
  IDataService service = new DataServiceProxy();
  // Network connection not established yet
  
  String data = service.fetchData();
  // First call: establishes connection, fetches data
  
  String data2 = service.fetchData();
  // Second call: reuses connection
```

**Benefits:**
- Lazy connection establishment
- Hides network communication details
- Automatic reconnection on failure
- Transparent remote access
- Can implement caching of remote data

---

### **4. Other Proxy Types**

**Cache Proxy:**
```java
class CacheProxy implements IDataService {
    private RealDataService realService;
    private String cachedData;
    
    public String fetchData() {
        if (cachedData == null) {
            cachedData = realService.fetchData();
        }
        return cachedData;  // Return cached
    }
}
```

**Logging Proxy:**
```java
class LoggingProxy implements IDisplay {
    private RealImage realImage;
    
    public void display() {
        System.out.println("BEFORE: About to display");
        realImage.display();
        System.out.println("AFTER: Display complete");
    }
}
```

**Validation Proxy:**
```java
class ValidationProxy implements IDocumentReader {
    public void unlockPDF(String filePath, String password) {
        if (!isValidPath(filePath)) {
            throw new IllegalArgumentException("Invalid path");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        realReader.unlockPDF(filePath, password);
    }
}
```

---

## Implementation Details

### Component (Subject Interface)

#### **IDisplay Interface**
```java
Purpose: Common interface for Proxy and RealImage
Methods:
  void display()
    - Display the image
    - Implemented by both Proxy and Real Object

Key Point:
  - Both ImageProxy and RealImage implement IDisplay
  - Client sees only IDisplay
  - Client doesn't know which is Proxy, which is Real
  - Polymorphism enables transparency
```

#### **IDocumentReader Interface**
```java
Purpose: Common interface for DocumentProxy and RealDocumentReader
Methods:
  void unlockPDF(String filePath, String password)
    - Unlock and display PDF document

Key Point:
  - DocumentProxy and RealDocumentReader implement same interface
  - Protection logic transparent to client
```

#### **IDataService Interface**
```java
Purpose: Common interface for DataServiceProxy and RealDataService
Methods:
  String fetchData()
    - Fetch data from remote service

Key Point:
  - Remote communication hidden behind proxy
  - Client unaware of network calls
```

---

### Real Objects

#### **RealImage Class**
```java
Purpose: Expensive resource that loads image from disk
Attributes:
  - String fileName    // Image file path

Constructor:
  public RealImage(String fileName)
    - Sets file name
    - IMMEDIATELY calls loadFromDisk()
    - Expensive operation happens here
  
  Why expensive:
    - Disk I/O operations (slow)
    - Memory allocation for image data (large)
    - Image decoding/processing
    - Network transfer if remote

Methods:
  loadFromDisk()
    - Simulates loading from disk
    - Called immediately in constructor
    - Takes time: 5-100ms depending on size
  
  display()
    - Display loaded image
    - Assumes image already loaded

Key Characteristic:
  - Creation time = loading time
  - Cannot avoid loading (eager initialization)
  - Resource allocated immediately
```

#### **RealDocumentReader Class**
```java
Purpose: Actual PDF reader with no access control
Methods:
  unlockPDF(String filePath, String password)
    - Unlock PDF
    - Display content
    - No permission checks
    - Assumes authorized access

Key Characteristic:
  - No built-in security
  - Access not controlled
  - Proxy adds protection layer
```

#### **RealDataService Class**
```java
Purpose: Remote service that returns data
Constructor:
  - Sets up remote connection (simulated)
  - Expensive network initialization
  
Methods:
  fetchData()
    - Query remote service
    - Return data
    - Network communication required

Key Characteristic:
  - Remote location (network call)
  - Connection establishment expensive
  - Data fetching takes time
  - Proxy handles connection management
```

---

### Proxy Objects

#### **ImageProxy Class (Virtual Proxy)**
```java
Purpose: Lazy initialization proxy for RealImage
Attributes:
  - String fileName           // Image file name
  - RealImage realImage       // Lazy-loaded reference (null initially)

Constructor:
  public ImageProxy(String fileName)
    - Sets file name
    - Does NOT load image
    - realImage remains null
    - Fast operation (no I/O)

Methods:
  display()
    - Check if realImage null
    - If null: create RealImage (triggers loading)
    - If not null: reuse cached object
    - Call realImage.display()

Key Behavior:
  - Creation: O(1) - trivial
  - First display(): O(n) - loading happens
  - Subsequent display(): O(1) - cached
  - Transparent to client (same interface)
```

#### **DocumentProxy Class (Protection Proxy)**
```java
Purpose: Access control proxy for RealDocumentReader
Attributes:
  - RealDocumentReader realReader  // Real object reference
  - User user                       // Current user context

Constructor:
  public DocumentProxy(User user)
    - Stores user reference
    - Creates real reader
    - Does NOT prevent creation

Methods:
  unlockPDF(String filePath, String password)
    - Check user.premiumMembership
    - If false: return (deny access)
    - If true: call realReader.unlockPDF()

Key Behavior:
  - Permission check before delegating
  - Access denied without trying real object
  - Different users get different access
  - Can log access attempts
```

#### **DataServiceProxy Class (Remote Proxy)**
```java
Purpose: Remote object proxy for RealDataService
Attributes:
  - RealDataService realService  // Lazy-loaded remote object

Constructor:
  public DataServiceProxy()
    - realService = null
    - No connection established
    - Fast object creation

Methods:
  fetchData()
    - Check if realService null
    - If null: create RealDataService (establish connection)
    - If not null: reuse existing connection
    - Call and return realService.fetchData()

Key Behavior:
  - Connection established lazily
  - Reuses connection for multiple calls
  - Transparent remote access
  - Can add retry logic, caching, timeout
```

**Proxy Architecture:**
```
┌──────────────────────────────────────────────┐
│              Client Code                     │
└────────────────────┬─────────────────────────┘
                     │
                     │ calls methods on
                     │
        ┌────────────▼──────────────┐
        │     IDisplay (Interface)  │
        └────────────┬──────────────┘
                     │
          ┌──────────┴──────────┐
          │                     │
      ┌───▼────────┐    ┌──────▼─────┐
      │ ImageProxy │    │ RealImage   │
      │ (Proxy)    │    │ (Real Obj)  │
      │            │    │             │
      │ - fileName │    │ - fileName  │
      │ - realImage├───→│ - loadData()│
      │ + display()│    │ + display() │
      └────────────┘    └─────────────┘

Flow:
  1. Client has reference to ImageProxy
  2. Client calls display()
  3. ImageProxy checks if realImage null
  4. If null: creates RealImage (triggers loading)
  5. ImageProxy calls realImage.display()
  6. RealImage displays image
```

---

## Execution Flow: Step-by-Step

### Virtual Proxy Example (Lazy Loading)

```
1. Client creates proxy:
   IDisplay image = new ImageProxy("vacation.jpg");
   
   ImageProxy constructor:
   - this.fileName = "vacation.jpg"
   - this.realImage = null  // Not created!
   - Constructor completes (fast)
   
   State: Proxy exists, real image does NOT exist

2. Client calls display (first time):
   image.display();
   
   ImageProxy.display() executes:
   - Check: if (realImage == null)
   - Condition true (image not loaded yet)
   - Create: realImage = new RealImage("vacation.jpg")
   
     RealImage constructor:
     - this.fileName = "vacation.jpg"
     - Call loadFromDisk()
     - Output: "Loading vacation.jpg"
     - Image data loaded into memory
   
   - Call: realImage.display()
     RealImage.display():
     - Output: "Displaying vacation.jpg"

   Output So Far:
     Loading vacation.jpg
     Displaying vacation.jpg

   State: RealImage created and cached, image in memory

3. Client calls display (second time):
   image.display();
   
   ImageProxy.display() executes:
   - Check: if (realImage == null)
   - Condition false (image already loaded)
   - Skip construction
   - Call: realImage.display()
     RealImage.display():
     - Output: "Displaying vacation.jpg"

   Output:
     Displaying vacation.jpg

   Key: No "Loading" message! Image was cached.

4. Client calls display (third time):
   image.display();
   
   Same as step 3:
   - Image not reloaded
   - Cached object reused
   
   Output:
     Displaying vacation.jpg

Pattern:
  First call: LOAD + DISPLAY (slow, ~100ms)
  Subsequent calls: DISPLAY ONLY (fast, ~1ms)
  Total saved time: 99ms per call
```

### Protection Proxy Example (Access Control)

```
1. Create non-premium user:
   User user1 = new User("Naman", false);
   IDocumentReader reader = new DocumentProxy(user1);
   
   State: user1.premiumMembership = false

2. Non-premium user tries to access:
   reader.unlockPDF("document.pdf", "secret123");
   
   DocumentProxy.unlockPDF() executes:
   - Check: if (!user.premiumMembership)
   - Condition true (user not premium)
   - Print: "[DocumentProxy] Access denied"
   - Return (EXIT EARLY - never calls realReader)
   
   Output:
     [DocumentProxy] Access denied. Only premium members can unlock PDFs.

   State: RealDocumentReader.unlockPDF() NEVER CALLED
          Access denied without attempting real operation

3. Create premium user:
   User user2 = new User("Mishti", true);
   IDocumentReader reader2 = new DocumentProxy(user2);
   
   State: user2.premiumMembership = true

4. Premium user tries to access:
   reader2.unlockPDF("document.pdf", "secret123");
   
   DocumentProxy.unlockPDF() executes:
   - Check: if (!user.premiumMembership)
   - Condition false (user IS premium)
   - Continue to actual operation
   - Call: realReader.unlockPDF("document.pdf", "secret123")
   
     RealDocumentReader.unlockPDF():
     - Output: "[RealDocumentReader] Unlocking PDF at: document.pdf"
     - Output: "[RealDocumentReader] PDF unlocked successfully with password: secret123"
     - Output: "[RealDocumentReader] Displaying PDF content..."

   Output:
     [RealDocumentReader] Unlocking PDF at: document.pdf
     [RealDocumentReader] PDF unlocked successfully with password: secret123
     [RealDocumentReader] Displaying PDF content...

   State: RealDocumentReader.unlockPDF() CALLED and executed
          Same interface, different behavior

Key Difference:
  Non-premium: Access denied at proxy (early return)
  Premium: Request passes through proxy to real object
  Same method call, different outcomes based on context
```

### Remote Proxy Example (Lazy Connection)

```
1. Create proxy:
   IDataService service = new DataServiceProxy();
   
   DataServiceProxy constructor:
   - this.realService = null  // Connection not established
   - Constructor completes quickly
   
   Output: (None)
   State: Proxy exists, no connection yet

2. First fetchData() call:
   String data = service.fetchData();
   
   DataServiceProxy.fetchData() executes:
   - Output: "[DataServiceProxy] Connecting to remote service..."
   - Check: if (realService == null)
   - Condition true (no connection)
   - Create: realService = new RealDataService()
   
     RealDataService constructor:
     - Output: "[RealDataService] Initialized (simulating remote setup)"
     - Simulates: Remote connection established
     - Simulates: Loading remote configuration
   
   - Call: return realService.fetchData()
     RealDataService.fetchData():
     - Return: "[RealDataService] Data from server"

   Output:
     [DataServiceProxy] Connecting to remote service...
     [RealDataService] Initialized (simulating remote setup)
     [RealDataService] Data from server

   State: Connection established, service initialized

3. Second fetchData() call:
   String data2 = service.fetchData();
   
   DataServiceProxy.fetchData() executes:
   - Output: "[DataServiceProxy] Connecting to remote service..."
   - Check: if (realService == null)
   - Condition false (connection exists)
   - Skip initialization
   - Call: return realService.fetchData()
     RealDataService.fetchData():
     - Return: "[RealDataService] Data from server"

   Output:
     [DataServiceProxy] Connecting to remote service...
     [RealDataService] Data from server

   Key Observation: No reinitialization!
   Connection reused, faster response

Benefits:
  First call: Connection established (slow, 500ms+)
  Subsequent calls: Reuse connection (fast, 10ms)
  Network overhead paid only once
```

---

## Key Interview Topics

### 1. **Proxy vs Decorator Pattern**

| Aspect | Proxy | Decorator |
|--------|-------|-----------|
| **Purpose** | Control access to object | Add behavior to object |
| **Intent** | Protection, delay, optimization | Add features dynamically |
| **Relationship** | Knows real object, may hide it | Wraps real object, enhances it |
| **Semantics** | Same interface as target | Same interface, enhanced |
| **Creation** | Proxy controls creation | Decorator created with real object |
| **Performance** | May optimize (lazy load) | May add overhead |
| **Multiple instances** | Usually one proxy per real object | Can chain multiple decorators |

**Key Difference:**
```java
Proxy:
  IDisplay proxy = new ImageProxy("test.jpg");
  // proxy CONTROLS access to RealImage
  // Delays creation or restricts access
  // One proxy per real object typically

Decorator:
  IDisplay decorated = new LoggingDecorator(
      new CachingDecorator(
          new ImageProxy("test.jpg")
      )
  );
  // Decorators ADD behavior
  // Can chain multiple decorators
  // Multiple decorators per object
```

---

### 2. **Proxy vs Adapter Pattern**

| Aspect | Proxy | Adapter |
|--------|-------|---------|
| **Purpose** | Control access | Convert interface |
| **Real object** | Known at proxy creation | May be unknown |
| **Interface** | Same as real object | May differ from real object |
| **Role** | Same type as target | Translator/converter |
| **Intent** | Protection/optimization | Compatibility |

**Example:**
```java
Proxy: Same interface, controlled access
  IDisplay proxy = new ImageProxy(image);
  proxy.display();  // Controls when loading happens

Adapter: Different interface, converts calls
  INewFormat adapter = new ImageAdapter(oldFormatImage);
  adapter.newMethod();  // Converts to old format calls
```

---

### 3. **When Does Proxy Create Real Object?**

**Virtual Proxy (Lazy):**
```java
Real object created: On first method call
Example: ImageProxy creates RealImage on first display()

Timing:
  new ImageProxy("file") - NO creation
  image.display()        - YES, creates now
  image.display()        - NO, reused
```

**Protection Proxy:**
```java
Real object created: During proxy construction
Example: DocumentProxy creates RealDocumentReader in constructor

Timing:
  new DocumentProxy(user) - YES, creates immediately
  reader.unlock()         - Permission check only
```

**Remote Proxy:**
```java
Real object created: Lazily on first use
Example: DataServiceProxy creates RealDataService on first fetchData()

Timing:
  new DataServiceProxy() - NO creation
  service.fetch()        - YES, creates now
  service.fetch()        - NO, reused
```

---

### 4. **Access Control Logic in Protection Proxy**

**Where Access Check Happens:**
```java
class DocumentProxy implements IDocumentReader {
    public void unlockPDF(String path, String pwd) {
        // Check BEFORE delegating
        if (!user.premiumMembership) {
            return;  // Early exit, deny access
        }
        // Only reaches here if permission granted
        realReader.unlockPDF(path, pwd);
    }
}

Advantages:
  - Real object never called if access denied
  - No resource wasted
  - Can log denied access attempts
  - Cleaner than RealObject handling access
```

---

### 5. **Lazy Initialization Technique**

**How Lazy Loading Works:**
```java
public class ImageProxy implements IDisplay {
    private RealImage realImage;  // Initially null
    
    public void display() {
        if (realImage == null) {
            // First call path
            realImage = new RealImage(fileName);
            // Expensive operation happens HERE
        }
        // Reused for all subsequent calls
        realImage.display();
    }
}

Benefits:
  - Object not created if never used
  - Defers expensive initialization
  - Can create many proxies cheaply
  - Initialization happens when needed
  
Trade-off:
  - First call slower (includes initialization)
  - Subsequent calls faster (no initialization)
  - Total time usually less than eager loading
```

---

### 6. **Caching in Proxy**

**How Proxy Enables Caching:**
```java
class ImageProxy implements IDisplay {
    private RealImage realImage;  // Cached reference
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);  // Load once
        }
        realImage.display();  // Reuse cached
    }
}

Proxy acts as:
  - Gatekeeper (controls access)
  - Cache (stores loaded object)
  - Factory (creates on demand)

Without proxy:
  new RealImage() called every time
  
With proxy:
  RealImage created once, reused
```

---

### 7. **Polymorphism Enables Proxy Transparency**

**How Client Stays Unaware:**
```java
// Client doesn't know or care which implementation
IDisplay image = new ImageProxy("test.jpg");  // Could be proxy
// OR
IDisplay image = new RealImage("test.jpg");   // Could be real

// Same code works for both!
image.display();

Benefits of Polymorphism:
  - Proxy and Real have same interface
  - Client sees only interface
  - Runtime determines which to call
  - Can swap proxy/real without code change
  - Add proxy to existing code transparently
```

---

### 8. **Remote Proxy and Network Communication**

**Hiding Network Details:**
```java
class DataServiceProxy implements IDataService {
    private RealDataService realService;
    
    public String fetchData() {
        // Network communication details hidden
        if (realService == null) {
            realService = new RealDataService();
            // In real code: connects to remote server
            // Opens socket, authenticates, etc.
        }
        return realService.fetchData();
        // In real code: sends over network
    }
}

Client code sees:
  service.fetchData()
  
Proxy handles:
  - Connection management
  - Network protocol
  - Error handling and retries
  - Serialization/deserialization
  - Timeout handling
```

---

### 9. **Proxy vs Facade**

| Aspect | Proxy | Facade |
|--------|-------|--------|
| **Purpose** | Control access | Simplify complex subsystem |
| **Object** | Single real object | Multiple subsystems |
| **Interface** | Same as real object | Simplified interface |
| **Creation** | May control creation | Manages subsystems |
| **Relationship** | One-to-one with real object | One-to-many with subsystems |

**Example:**
```java
Proxy:
  ImageProxy controls ONE RealImage
  Same interface (display())
  
Facade:
  ComputerFacade controls many subsystems
  Simplified interface (startComputer())
```

---

### 10. **Adding New Proxy Type**

**How Easy to Extend:**
```java
// New proxy type: Retry Proxy
class RetryProxy implements IDisplay {
    private RealImage realImage;
    private int maxRetries = 3;
    
    public void display() {
        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                if (realImage == null) {
                    realImage = new RealImage(fileName);
                }
                realImage.display();
                return;  // Success
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxRetries) throw e;
                // Retry
            }
        }
    }
}

Benefits:
  - New proxy type added without changing existing code
  - Same interface (IDisplay)
  - Can combine proxies:
    
    IDisplay image = new RetryProxy(
        new ImageProxy("file.jpg")
    );
```

---

## Advantages of Proxy Pattern

✅ **Lazy Initialization**: Defer expensive creation until needed

✅ **Access Control**: Enforce permissions before accessing resources

✅ **Transparent**: Client unaware of proxy (same interface)

✅ **Performance Optimization**: Caching, connection pooling

✅ **Security**: Add authentication/authorization layer

✅ **Logging and Monitoring**: Track access without modifying real object

✅ **Remote Access**: Hide network communication

✅ **Resource Management**: Control resource allocation

✅ **Fail-Safe**: Add retry logic, fallback behavior

✅ **Separation of Concerns**: Access control separate from business logic

---

## Disadvantages & Limitations

❌ **Added Complexity**: Extra layer of indirection

❌ **Performance Overhead**: Method call through proxy adds latency

❌ **Not Always Beneficial**: If real object is cheap, proxy overhead not worth it

❌ **Client Unaware**: Transparency can hide what's happening behind scenes

❌ **Maintenance**: Additional class to maintain and test

❌ **Lazy Loading Gotchas**: First call slower than direct access

❌ **Thread Safety**: Lazy initialization not always thread-safe

❌ **Identity Issues**: proxy != real object (identity checks may fail)

---

## When to Use Multiple Proxies

**Scenario 1: Multiple Proxy Types for Same Object**
```java
// Chain proxies for different concerns
IDisplay image = new LoggingProxy(
    new CachingProxy(
        new ImageProxy("file.jpg")
    )
);

// Execution flow:
// 1. LoggingProxy logs access
// 2. CachingProxy checks cache
// 3. ImageProxy checks if loaded
// 4. RealImage executes
```

**Scenario 2: Different Access Patterns Need Different Proxies**
```java
// Regular users get caching proxy
IDisplay image1 = new CachingProxy(
    new ImageProxy("file.jpg")
);

// Premium users get direct access
IDisplay image2 = new RealImage("file.jpg");

// Admin users get logging + caching
IDisplay image3 = new LoggingProxy(
    new CachingProxy(
        new ImageProxy("file.jpg")
    )
);
```

---

## Real-World Applications

### **1. Virtual Proxy - Image Loading**
```java
Web browsers: Images load on demand, not at page load
  - Webpage displays immediately
  - Images load as user scrolls
  - Large images don't block rendering
  - NetworkImageProxy handles lazy loading
```

### **2. Remote Method Invocation (RMI)**
```java
Java RMI: Remote objects accessed as local
  - Proxy appears as local object
  - Behind scenes: network calls
  - Serialization/deserialization handled
  - Transparent distributed computing
```

### **3. Database Connection Pooling**
```java
Database proxies: Reuse connections instead of creating new
  - Connection pool as proxy
  - Lazy allocation of real connections
  - Reuse existing connections
  - Performance optimization
```

### **4. Hibernate/JPA ORM - Lazy Loading**
```java
ORM frameworks: Lazy load related entities
  - Association fields start as proxies
  - Real objects loaded on first access
  - Collection proxies for one-to-many
  - Transparent to application code
```

### **5. Spring Framework - AOP Proxies**
```java
Aspect-Oriented Programming:
  - Method invocation intercepted by proxy
  - Cross-cutting concerns applied
  - Logging, transaction management, security
  - Original class unmodified
```

### **6. Authentication and Authorization**
```java
System security: Proxy guards access
  - User credentials checked at proxy
  - Unauthorized access denied early
  - No resource waste on denied requests
  - Security policy centralized
```

### **7. Caching Systems**
```java
Cache proxies: Return cached data
  - Expensive computations cached
  - First access: compute and cache
  - Subsequent accesses: return cached value
  - Transparent to caller
```

### **8. Protection Proxy - PDF Reader**
```java
Document access control:
  - Premium users access PDFs
  - Free users denied access
  - Protection at proxy level
  - Same interface for all users
```

---

## Best Practices

### **1. Make Proxy Same Type as Real Object**
```java
Good: Both implement same interface
  class ImageProxy implements IDisplay { }
  class RealImage implements IDisplay { }
  
Bad: Different interface
  class ImageProxy {
      public void show() { }  // Different name
  }
```

### **2. Keep Proxy Logic Focused**
```java
Good: Proxy handles one concern
  class ImageProxy {  // Handles lazy loading only
      private RealImage real;
      public void display() {
          if (real == null) real = new RealImage();
          real.display();
      }
  }

Bad: Proxy does multiple things
  class ImageProxy {
      // Logging, caching, validation all here
      // Too many responsibilities
  }
```

### **3. Lazy Load Thread-Safely**
```java
Good: Thread-safe lazy initialization
  class ImageProxy {
      private RealImage real;
      
      public synchronized void display() {
          if (real == null) {
              real = new RealImage(fileName);
          }
          real.display();
      }
  }
```

### **4. Document Proxy Behavior**
```java
Good: Clear documentation
  /**
   * Proxy for lazy loading RealImage.
   * Image not loaded until first display() call.
   * Subsequent calls reuse cached image.
   */
  public class ImageProxy { }

Bad: No explanation
  public class ImageProxy { }
```

### **5. Consider Performance Impact**
```java
When to use proxy:
  ✓ Real object creation is expensive
  ✓ Not all instances may be used
  ✓ Access control needed
  ✓ Remote communication involved

When not to use:
  ✗ Real object creation is cheap
  ✗ All proxies will be used anyway
  ✗ No access control needed
  ✗ Proxy overhead > benefit
```

### **6. Separate Client and Proxy Creation**
```java
Good: Factory creates appropriate proxy
  IDisplay image = proxyFactory.createImage(fileName);
  // Factory decides proxy vs real

Bad: Client decides
  IDisplay image = new ImageProxy(fileName);
  // Client aware of proxy
```

### **7. Avoid Proxy-Specific Methods**
```java
Good: Only interface methods visible
  IDisplay image = new ImageProxy(fileName);
  image.display();  // Interface only

Bad: Proxy-specific methods
  ImageProxy proxy = new ImageProxy(fileName);
  proxy.setCacheSize(1000);  // Proxy-specific
  proxy.display();
```

---

## Design Variations

### **1. Decorator + Proxy Combination**
```java
// Proxy for lazy loading + Decorator for logging
class SmartImage implements IDisplay {
    private IDisplay decorated;
    
    public SmartImage(String fileName) {
        // Proxy: lazy loading
        IDisplay proxy = new ImageProxy(fileName);
        // Decorator: logging
        decorated = new LoggingDecorator(proxy);
    }
    
    public void display() {
        decorated.display();
    }
}
```

### **2. Copy-on-Write Proxy**
```java
// Proxy that creates copy only when modified
class DocumentProxy {
    private Document original;
    private Document copy;
    private boolean modified = false;
    
    public void edit() {
        if (!modified) {
            copy = original.deepCopy();
            modified = true;
        }
        copy.edit();
    }
}
```

### **3. Timeout Proxy**
```java
// Proxy that enforces timeout on operations
class TimeoutProxy implements IDataService {
    private RealDataService real;
    
    public String fetchData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            if (real == null) real = new RealDataService();
            return real.fetchData();
        });
        
        try {
            return future.get(5, TimeUnit.SECONDS);  // 5 second timeout
        } catch (TimeoutException e) {
            throw new RuntimeException("Operation timed out");
        }
    }
}
```

### **4. Smart Proxy with Statistics**
```java
// Proxy that collects usage statistics
class StatisticsProxy implements IDisplay {
    private RealImage real;
    private int displayCount = 0;
    
    public void display() {
        displayCount++;
        if (real == null) real = new RealImage(fileName);
        real.display();
        System.out.println("Display called " + displayCount + " times");
    }
}
```

---

## Common Interview Questions

**Q1: What is Proxy pattern and what problem does it solve?**
- **A:** Proxy pattern provides a placeholder for another object to control access to it. It solves problems like: (1) Lazy initialization - delay expensive object creation until needed, (2) Access control - restrict access based on permissions, (3) Remote access - hide network communication, (4) Performance optimization - add caching, logging, retry logic.

**Q2: What are the three main types of proxies?**
- **A:** (1) Virtual Proxy - delays creation of expensive objects until first use (lazy initialization). Example: ImageProxy loads image only on first display(). (2) Protection Proxy - controls access based on permissions. Example: DocumentProxy checks if user is premium before allowing access. (3) Remote Proxy - provides local representation of remote object. Example: DataServiceProxy hides network communication details.

**Q3: How does Proxy differ from Decorator?**
- **A:** Proxy controls access and may delay creation; focus is on access control and optimization. Decorator adds behavior to existing object; focus is on adding features. Proxy typically one per real object; Decorator can chain multiple. Proxy and Real have same interface; Decorator wraps and enhances. Proxy intent: protection/optimization; Decorator intent: feature addition.

**Q4: When is proxy created vs when is real object created?**
- **A:** In Virtual Proxy, proxy created immediately but real object created lazily on first use (e.g., ImageProxy created instantly, RealImage created on first display()). In Protection Proxy, both proxy and real object created immediately, but real access controlled by proxy permission check. In Remote Proxy, proxy created immediately but connection established lazily on first method call.

**Q5: Why is proxy transparent to client?**
- **A:** Because proxy and real object implement same interface. Client holds reference to interface type, not concrete type. At runtime, polymorphism determines which implementation runs. Client unaware if it's proxy or real object. This transparency is key - client code doesn't change when proxy added. Same interface enables substitution.

**Q6: What happens on first display() call in ImageProxy?**
- **A:** First call checks if realImage is null. Since it is, ImageProxy creates new RealImage(fileName). RealImage constructor calls loadFromDisk(), which loads image from disk (expensive operation). Then ImageProxy calls realImage.display(). Output: "Loading vacation.jpg" then "Displaying vacation.jpg".

**Q7: What happens on second display() call?**
- **A:** Second call checks if realImage is null. It's not null (cached from first call). ImageProxy skips creation and directly calls realImage.display(). Output: "Displaying vacation.jpg" (no "Loading" message). This demonstrates benefit - first call is slower due to loading, but subsequent calls are fast due to caching.

**Q8: How does Protection Proxy enforce access control?**
- **A:** DocumentProxy checks user.premiumMembership before delegating to RealDocumentReader. If false, it returns early without calling realReader.unlockPDF(). Real object is never accessed by unauthorized users. If true, it delegates to real object. Same method called, different outcome based on context.

**Q9: Can you combine proxy types?**
- **A:** Yes, proxies can be nested/chained:
```java
IDisplay image = new LoggingProxy(
    new CachingProxy(
        new ImageProxy("file.jpg")
    )
);
```
Execution: LoggingProxy logs → CachingProxy checks cache → ImageProxy lazy loads → RealImage executes. Each proxy handles one concern.

**Q10: When should you NOT use Proxy?**
- **A:** When real object is cheap to create (proxy overhead not worth it). When no access control, lazy loading, or remote communication needed. When transparency would hide misleading behavior. When simplicity preferred over optimization. If pattern complicates design without proportional benefit. Proxy is useful when it genuinely solves a problem, not as premature optimization.

---

## Summary

The **Proxy Design Pattern** provides a placeholder or surrogate for another object to control access to it by:

1. **Defining same interface** - Proxy and real object implement same interface
2. **Deferring creation** - Virtual proxy delays expensive initialization
3. **Controlling access** - Protection proxy enforces permissions
4. **Hiding complexity** - Remote proxy hides network communication
5. **Adding behavior** - Proxy can log, cache, validate before delegating
6. **Enabling transparency** - Client unaware of proxy through polymorphism

The pattern is fundamental to many real-world systems including web browsers (lazy loading images), ORM frameworks (lazy loading entities), caching systems, security systems, and remote procedure calls. Understanding proxy deeply shows mastery of access control, lazy initialization, and design patterns in general.
