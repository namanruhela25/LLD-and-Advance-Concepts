# Memento Design Pattern

## Definition

The **Memento Design Pattern** is a behavioral design pattern that provides the ability to save and restore an object's previous state without violating the principle of encapsulation. It captures the internal state of an object at a particular moment in time and stores it in a way that allows restoring the object to that state later.

## Core Philosophy

**Main Insight:** Externalize an object's state without exposing its internal structure, enabling undo/redo and rollback functionality.

The pattern separates **state snapshot** from the **object that owns the state**, allowing safe restoration without breaking encapsulation.

```
Without Memento (Tight Coupling):
Client tries to save/restore state:
├── Direct access to object's fields (BREAKS ENCAPSULATION!)
├── Complex state management logic in client
├── Risk of inconsistent state
└── Difficult to add undo/redo

With Memento (Clean Separation):
Memento stores snapshot:
├── Originator creates memento (internal knowledge)
├── Caretaker manages mementos (no internal knowledge)
├── Client initiates operations
└── Safe restoration preserving encapsulation
```

---

## Problem Statement (Interview Context)

### The Challenge: Undo/Redo and Rollback

**Scenario:** You have a `Database` with records that can be inserted, updated, or deleted. You need:
- Undo functionality: Revert to previous state after failed operations
- Rollback: Cancel transaction and return to starting state
- Snapshot management: Save state at any point

**Without Memento Pattern:**

```java
// ❌ BAD: Client directly saves state
class Database {
    private Map<String, String> records;
    
    public void insert(String key, String value) {
        records.put(key, value);
    }
    
    public void update(String key, String value) {
        records.put(key, value);
    }
}

// Client must manually handle state saving
class Client {
    Map<String, String> backup = new HashMap<>();  // Manual backup!
    
    // Save state (how to do this safely?)
    public void saveState(Database db) {
        // Need direct access to records!
        // This breaks encapsulation
        backup = db.getRecords();  // Exposed internal state!
    }
    
    public void saveState2(Database db) {
        // Deep copy? Shallow copy? How much to copy?
        // Complex logic in client
        // Prone to errors
    }
    
    // Restore state
    public void restoreState(Database db) {
        // Must know how to restore
        // Must set private fields
        // Violates encapsulation
        db.setRecords(backup);
    }
}
```

**Problems:**
1. **Encapsulation Violation**: Client accesses internal state
2. **Complexity**: Client manages snapshot logic
3. **State Inconsistency**: Easy to create partial/corrupt snapshots
4. **Tight Coupling**: Client depends on internal structure
5. **Hard to Modify**: Change object internals → must update client code
6. **Multiple Versions**: Managing multiple snapshots becomes messy
7. **No Clear Responsibility**: Who owns state snapshots?

**With Memento Pattern:**

```java
// ✅ GOOD: State encapsulation preserved
class DatabaseMemento {
    private Map<String, String> data;  // Snapshot
    
    public DatabaseMemento(Map<String, String> dbData) {
        this.data = new HashMap<>(dbData);  // Deep copy
    }
    
    public Map<String, String> getState() {
        return data;
    }
}

class Database {
    private Map<String, String> records;
    
    // Create memento - only DB knows how to snapshot
    public DatabaseMemento createMemento() {
        return new DatabaseMemento(records);
    }
    
    // Restore from memento - only DB knows how to restore
    public void restoreFromMemento(DatabaseMemento memento) {
        records = new HashMap<>(memento.getState());
    }
    
    public void insert(String key, String value) {
        records.put(key, value);
    }
}

// Caretaker manages mementos (doesn't know internals)
class TransactionManager {
    private DatabaseMemento backup;
    
    public void beginTransaction(Database db) {
        backup = db.createMemento();  // Save snapshot
    }
    
    public void commitTransaction() {
        backup = null;  // Discard snapshot (success)
    }
    
    public void rollbackTransaction(Database db) {
        db.restoreFromMemento(backup);  // Restore snapshot
        backup = null;
    }
}
```

**Benefits:**
- ✅ Encapsulation preserved
- ✅ Client doesn't know internals
- ✅ Object controls snapshot/restore
- ✅ Clear separation of concerns
- ✅ Easy to add undo/redo
- ✅ Multiple snapshots manageable
- ✅ State consistency guaranteed

---

## Core Concepts

### 1. Originator (Object with State)

**Definition:** The object whose state needs to be saved and restored.

**Responsibilities:**
- Maintains current state
- Creates memento containing state snapshot
- Restores state from memento
- Only entity that knows how to snapshot/restore

```java
class Database {
    private Map<String, String> records;
    
    // Create memento - capture state
    public DatabaseMemento createMemento() {
        return new DatabaseMemento(records);
    }
    
    // Restore from memento - restore state
    public void restoreFromMemento(DatabaseMemento memento) {
        records = new HashMap<>(memento.getState());
    }
    
    // Operations that change state
    public void insert(String key, String value) {
        records.put(key, value);
    }
}
```

**Key Point:** Originator encapsulates how snapshots are created and restored!

### 2. Memento (Snapshot Container)

**Definition:** An immutable object that stores a snapshot of the originator's state.

**Responsibilities:**
- Store state snapshot
- Provide access to snapshot (possibly restricted)
- Never allow modification after creation
- Act as "receipt" of state at a moment in time

```java
class DatabaseMemento {
    private Map<String, String> data;  // Snapshot
    
    // Constructor - only originator can create
    public DatabaseMemento(Map<String, String> dbData) {
        this.data = new HashMap<>(dbData);  // Deep copy!
    }
    
    // Getter - provide access to snapshot
    public Map<String, String> getState() {
        return data;
    }
    
    // No setter - immutable!
}
```

**Key Point:** Memento is typically immutable after creation!

### 3. Caretaker (Snapshot Manager)

**Definition:** The object that manages memento objects and their history.

**Responsibilities:**
- Store mementos (in stack, list, undo history)
- Know when to request snapshots
- Know when to restore from snapshots
- **NOT** know the internal structure of originator
- Not modifying mementos

```java
class TransactionManager {
    private DatabaseMemento backup;  // Simple: one backup
    
    // Could be more complex:
    private Stack<DatabaseMemento> undoStack = new Stack<>();
    private Stack<DatabaseMemento> redoStack = new Stack<>();
    
    public void saveSnapshot(Database db) {
        backup = db.createMemento();  // Ask originator
    }
    
    public void restoreSnapshot(Database db) {
        db.restoreFromMemento(backup);  // Ask originator
    }
}
```

**Key Point:** Caretaker is "store keeper" but doesn't know internal details!

### 4. Deep Copy vs Shallow Copy

**Critical Issue:** State snapshots must be **independent copies**!

```java
// ❌ BAD: Shallow copy (reference copy)
class BadMemento {
    private Map<String, String> data;
    
    public BadMemento(Map<String, String> dbData) {
        this.data = dbData;  // SHALLOW COPY!
    }
    
    public Map<String, String> getState() {
        return data;
    }
}

// Usage:
Map<String, String> records = ...;
BadMemento memento = new BadMemento(records);
records.put("key3", "value3");  // MODIFIES SNAPSHOT TOO!

// ✅ GOOD: Deep copy (independent copy)
class GoodMemento {
    private Map<String, String> data;
    
    public GoodMemento(Map<String, String> dbData) {
        this.data = new HashMap<>(dbData);  // DEEP COPY!
    }
    
    public Map<String, String> getState() {
        return data;
    }
}

// Usage:
Map<String, String> records = ...;
GoodMemento memento = new GoodMemento(records);
records.put("key3", "value3");  // Snapshot unchanged!
```

**Key Point:** Always deep copy to ensure snapshot independence!

---

## Architecture

### Component Relationships

```
Originator (Database)
├── has current state (Map<String, String>)
├── createMemento() → Memento
└── restoreFromMemento(Memento)

Memento (DatabaseMemento)
├── stores state snapshot
└── provides getState()

Caretaker (TransactionManager)
├── stores Mementos
├── knows when to snapshot
├── knows when to restore
└── uses Originator methods
```

### Interaction Patterns

**Simple Undo:**
```
Caretaker:
├── backup = Originator.createMemento()  [Screenshot]
└── Originator.restoreFromMemento(backup) [Restore]
```

**Undo/Redo Stack:**
```
Caretaker:
├── undoStack.push(Originator.createMemento())
├── When undo: Originator.restoreFromMemento(undoStack.pop())
├── redoStack.push(last_memento)
└── When redo: Originator.restoreFromMemento(redoStack.pop())
```

---

## Interaction Flow: Step-by-Step

### Scenario: Database Transaction with Rollback

**Step 1: Begin Transaction**
```java
Database db = new Database();
TransactionManager txn = new TransactionManager();

db.insert("user1", "Naman");  // Initial state: {user1=Naman}

txn.beginTransaction(db);
// TransactionManager calls: backup = db.createMemento()
// Memento stores: {user1=Naman}
```

**Step 2: Perform Operations**
```java
db.insert("user2", "Mishti");
db.insert("user3", "Shivam");
// Database now: {user1=Naman, user2=Mishti, user3=Shivam}
// Memento still: {user1=Naman}  ← Unchanged (deep copy!)
```

**Step 3: Error Occurs - Rollback**
```java
System.out.println("ERROR: Something broke!");

txn.rollbackTransaction(db);
// TransactionManager calls: db.restoreFromMemento(backup)
// Database restored to: {user1=Naman}
```

**Step 4: Verification**
```java
db.displayRecords();
// Output: {user1=Naman}
// ✅ All changes since beginTransaction() are undone!
```

---

## Interview Deep Dive

### Q1: What problem does Memento Pattern solve?

**Answer:**

Memento Pattern solves the **undo/rollback problem** while preserving **encapsulation**.

**Without Memento:**
- Client must access object internals to save state
- Breaks encapsulation
- Complex snapshot logic in wrong place
- Hard to maintain consistency

**With Memento:**
- Object creates its own snapshots (knows internals)
- Caretaker manages snapshots (doesn't know internals)
- Encapsulation preserved
- Clear responsibilities

**Interview Example:**
```
Database has: {id: 123, name: "John", balance: 1000}

Without Memento:
- How do I safely copy this?
- Deep copy or shallow? For nested objects?
- What if fields change? Must update client code

With Memento:
- Database.createMemento() handles it!
- Caretaker just stores it
- Simple, safe, maintainable
```

---

### Q2: How is deep copy different from shallow copy in Memento?

**Answer:**

**Shallow Copy:** Copies references only

```java
Map<String, String> original = new HashMap<>();
original.put("user1", "Naman");

// ❌ SHALLOW COPY
Map<String, String> shallowCopy = original;
shallowCopy.put("user2", "Mishti");

System.out.println(original);  // {user1=Naman, user2=Mishti}
// Both variables point to SAME map!
```

**Deep Copy:** Copies values and creates new objects

```java
Map<String, String> original = new HashMap<>();
original.put("user1", "Naman");

// ✅ DEEP COPY
Map<String, String> deepCopy = new HashMap<>(original);
deepCopy.put("user2", "Mishti");

System.out.println(original);   // {user1=Naman}
System.out.println(deepCopy);   // {user1=Naman, user2=Mishti}
// Different maps!
```

**In Memento (Critical!):**

```java
// ❌ BAD: Shallow copy - Snapshot gets modified!
class BadMemento {
    private Map<String, String> data;
    
    public BadMemento(Map<String, String> dbData) {
        this.data = dbData;  // SHALLOW!
    }
}

Database db = new Database();
db.insert("user1", "Naman");

BadMemento memento = new BadMemento(db.getRecords());

db.insert("user2", "Mishti");
// Memento ALSO has user2 now! (shared reference)

// ✅ GOOD: Deep copy - Snapshot is independent!
class GoodMemento {
    private Map<String, String> data;
    
    public GoodMemento(Map<String, String> dbData) {
        this.data = new HashMap<>(dbData);  // DEEP!
    }
}

Database db = new Database();
db.insert("user1", "Naman");

GoodMemento memento = new GoodMemento(db.getRecords());

db.insert("user2", "Mishti");
// Memento ONLY has user1 (independent copy)
```

**Key Interview Point:** "Always use deep copy for mementos to ensure snapshots are independent and true point-in-time captures."

---

### Q3: What's the difference between Memento and Prototype Pattern?

**Answer:**

| Aspect | Memento | Prototype |
|--------|---------|-----------|
| **Purpose** | Save/restore object state | Clone object quickly |
| **Use Case** | Undo/redo, rollback | Create copies, reduce creation cost |
| **What Copies** | State snapshot | Entire object |
| **When Stored** | At specific moments | Usually on demand |
| **Restoration** | Restore to exact same state | Use as template for new objects |
| **Encapsulation** | Preserved (originator creates memento) | May be violated (cloning needs internals) |

**Example:**

```
Memento:
Database state at T1: {user1=Naman}
Database state at T2: {user1=Naman, user2=Mishti}
Database state at T3: {user1=Naman, user2=Mishti, user3=Shivam}
Goal: Revert to T1

Prototype:
Create new Document from existing Document
clone() → Independent copy
Use new copy for different purpose
```

---

### Q4: Can you implement undo AND redo?

**Answer:** **YES!** Use two stacks!

```java
class TransactionManager {
    private Stack<DatabaseMemento> undoStack = new Stack<>();
    private Stack<DatabaseMemento> redoStack = new Stack<>();
    
    // Save current state for undo
    public void saveState(Database db) {
        undoStack.push(db.createMemento());
        redoStack.clear();  // Clear redo when new action
    }
    
    // Undo: pop from undo, restore, save to redo
    public void undo(Database db) {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return;
        }
        
        // Save current state to redo
        redoStack.push(db.createMemento());
        
        // Restore from undo
        DatabaseMemento memento = undoStack.pop();
        db.restoreFromMemento(memento);
    }
    
    // Redo: pop from redo, restore, save to undo
    public void redo(Database db) {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo!");
            return;
        }
        
        // Save current state to undo
        undoStack.push(db.createMemento());
        
        // Restore from redo
        DatabaseMemento memento = redoStack.pop();
        db.restoreFromMemento(memento);
    }
}

// Usage:
Database db = new Database();
TransactionManager tm = new TransactionManager();

db.insert("user1", "Naman");
tm.saveState(db);

db.insert("user2", "Mishti");
tm.saveState(db);

db.insert("user3", "Shivam");
tm.saveState(db);

tm.undo(db);  // Remove user3
tm.undo(db);  // Remove user2
tm.redo(db);  // Add user2 back
tm.redo(db);  // Add user3 back
```

**Flow Diagram:**
```
State 1: {user1}
  ↓ save
State 2: {user1, user2}
  ↓ save
State 3: {user1, user2, user3}

Undo path:
undoStack: [3, 2, 1]
redoStack: []
  ↓ undo
State 2: {user1, user2}
undoStack: [3, 2]
redoStack: [3]

  ↓ undo
State 1: {user1}
undoStack: [3]
redoStack: [2, 3]

  ↓ redo
State 2: {user1, user2}
undoStack: [3, 2]
redoStack: [3]
```

---

### Q5: What if you need to save state history?

**Answer:** Store mementos with history metadata!

```java
class HistoryEntry {
    private DatabaseMemento memento;
    private String operation;
    private long timestamp;
    
    public HistoryEntry(DatabaseMemento m, String op) {
        this.memento = m;
        this.operation = op;
        this.timestamp = System.currentTimeMillis();
    }
    
    public void display() {
        System.out.println(timestamp + " - " + operation);
    }
    
    public DatabaseMemento getMemento() {
        return memento;
    }
}

class TransactionManager {
    private List<HistoryEntry> history = new ArrayList<>();
    
    public void recordOperation(Database db, String operation) {
        history.add(new HistoryEntry(db.createMemento(), operation));
    }
    
    public void displayHistory() {
        for (HistoryEntry entry : history) {
            entry.display();
        }
    }
    
    public void restoreToOperation(Database db, int index) {
        if (index >= 0 && index < history.size()) {
            db.restoreFromMemento(history.get(index).getMemento());
        }
    }
}

// Usage:
Database db = new Database();
TransactionManager tm = new TransactionManager();

db.insert("user1", "Naman");
tm.recordOperation(db, "Insert user1");

db.insert("user2", "Mishti");
tm.recordOperation(db, "Insert user2");

db.insert("user3", "Shivam");
tm.recordOperation(db, "Insert user3");

tm.displayHistory();
// Output:
// 1715946000123 - Insert user1
// 1715946001456 - Insert user2
// 1715946002789 - Insert user3

tm.restoreToOperation(db, 1);  // Back to Insert user2
```

---

### Q6: How do you handle large objects in Memento?

**Answer:** Several strategies:

**Strategy 1: Compress Snapshots**
```java
class CompressedMemento {
    private byte[] compressedData;
    
    public CompressedMemento(Map<String, String> data) {
        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(data);
        
        // Compress
        byte[] serialized = baos.toByteArray();
        this.compressedData = compress(serialized);
    }
    
    public Map<String, String> getState() {
        byte[] serialized = decompress(compressedData);
        // Deserialize and return
        return (Map<String, String>) deserialize(serialized);
    }
}
```

**Strategy 2: Lazy Snapshots**
```java
class LazyMemento {
    private Map<String, String> data;
    private boolean loaded = false;
    private String filepath;
    
    public LazyMemento(Map<String, String> data) {
        // Don't copy yet - just store path
        filepath = saveToFile(data);
        loaded = false;
    }
    
    public Map<String, String> getState() {
        if (!loaded) {
            data = loadFromFile(filepath);
            loaded = true;
        }
        return data;
    }
}
```

**Strategy 3: Incremental Snapshots**
```java
class IncrementalMemento {
    private Map<String, String> baseSnapshot;
    private Map<String, String> changes;
    
    public IncrementalMemento(Map<String, String> current,
                            Map<String, String> previous) {
        // Store only what changed
        this.baseSnapshot = previous;
        this.changes = calculateDelta(current, previous);
    }
    
    public Map<String, String> getState() {
        Map<String, String> result = new HashMap<>(baseSnapshot);
        result.putAll(changes);
        return result;
    }
}
```

---

### Q7: Can Memento be used with nested objects?

**Answer:** **YES!** But requires recursive deep copy!

```java
class Person {
    private String name;
    private Address address;  // Nested object!
}

class Address {
    private String street;
    private String city;
}

// ❌ BAD: Shallow copy leaves nested object shared
class BadMemento {
    private Person person;
    
    public BadMemento(Person p) {
        this.person = p;  // Shallow - Address still shared!
    }
}

// ✅ GOOD: Deep copy clones nested objects
class GoodMemento {
    private String name;
    private Address addressCopy;
    
    public GoodMemento(Person p) {
        this.name = p.getName();
        // Deep copy nested object
        this.addressCopy = new Address(
            p.getAddress().getStreet(),
            p.getAddress().getCity()
        );
    }
    
    public Person restorePerson() {
        Address restoredAddress = new Address(
            addressCopy.getStreet(),
            addressCopy.getCity()
        );
        return new Person(name, restoredAddress);
    }
}
```

---

### Q8: What's the performance cost of Memento?

**Answer:**

| Factor | Cost | Mitigation |
|--------|------|-----------|
| **Memory** | O(n) per memento where n = state size | Compression, incremental snapshots |
| **Copy Time** | O(n) to create memento | Lazy loading, background threads |
| **Restoration Time** | O(n) to restore | GC optimization |
| **Storage** | Grows with history size | Limit history, cleanup old mementos |

**Example:**

```java
// Memory cost calculation
Database db = new Database();
// Add 1,000,000 records

DatabaseMemento m1 = db.createMemento();
// Memory: 1M records × (key size + value size)
// If avg 100 bytes per record → 100MB memento!

DatabaseMemento m2 = db.createMemento();
// Another 100MB

// With 10 mementos in history:
// 1GB of memory just for snapshots!

// Solution: Limit history
class LimitedHistoryManager {
    private LinkedList<DatabaseMemento> history;
    private static final int MAX_SIZE = 5;
    
    public void addMemento(Database db) {
        history.addLast(db.createMemento());
        if (history.size() > MAX_SIZE) {
            history.removeFirst();  // Keep only last 5
        }
    }
}
```

---

### Q9: Compare Memento with Snapshot Pattern

**Answer:**

| Aspect | Memento | Snapshot |
|--------|---------|----------|
| **Encapsulation** | Preserved (originator creates) | Often violated (client accesses) |
| **Complexity** | Moderate (well-defined roles) | Can be ad-hoc |
| **History** | Designed for history management | Not necessarily |
| **Restoration** | Automatic via memento structure | Manual or custom logic |
| **Formality** | Formal pattern with roles | Informal approach |

**Real Scenario:**

```
Memento (Formal):
- Clear interface: createMemento(), restoreFromMemento()
- Encapsulation guaranteed
- History management built-in
- Used in transactions

Snapshot (Informal):
- Just serialize object to file/db
- Restore by deserializing
- Ad-hoc logic
- Used for backups
```

---

### Q10: Can you combine Memento with Command Pattern?

**Answer:** **YES!** For powerful undo/redo!

```java
interface Command {
    void execute();
    void undo();
}

class InsertCommand implements Command {
    private Database db;
    private String key, value;
    private DatabaseMemento snapshot;
    
    public InsertCommand(Database db, String key, String value) {
        this.db = db;
        this.key = key;
        this.value = value;
    }
    
    @Override
    public void execute() {
        snapshot = db.createMemento();  // Before snapshot
        db.insert(key, value);
    }
    
    @Override
    public void undo() {
        db.restoreFromMemento(snapshot);
    }
}

class CommandHistory {
    private Stack<Command> executed = new Stack<>();
    
    public void execute(Command cmd) {
        cmd.execute();
        executed.push(cmd);
    }
    
    public void undo() {
        if (!executed.isEmpty()) {
            executed.pop().undo();
        }
    }
}

// Usage:
Database db = new Database();
CommandHistory history = new CommandHistory();

history.execute(new InsertCommand(db, "user1", "Naman"));
history.execute(new InsertCommand(db, "user2", "Mishti"));
history.execute(new InsertCommand(db, "user3", "Shivam"));

history.undo();  // Removes user3
history.undo();  // Removes user2
```

**Power:** Command + Memento = perfect undo/redo!

---

## Real-World Use Cases

### 1. Text Editor - Undo/Redo

```java
// Document state
class Document {
    private String content;
    
    public DocumentMemento createMemento() {
        return new DocumentMemento(content);
    }
    
    public void restoreFromMemento(DocumentMemento m) {
        content = m.getContent();
    }
}

// Editor with history
class TextEditor {
    private Stack<DocumentMemento> undoStack = new Stack<>();
    private Stack<DocumentMemento> redoStack = new Stack<>();
    
    public void typeCharacter(Document doc, char c) {
        undoStack.push(doc.createMemento());
        doc.appendCharacter(c);
        redoStack.clear();
    }
    
    public void undo(Document doc) {
        redoStack.push(doc.createMemento());
        doc.restoreFromMemento(undoStack.pop());
    }
}
```

### 2. Database Transactions - Rollback

```java
// Transaction management
class TransactionManager {
    private DatabaseMemento transactionStartSnapshot;
    
    public void beginTransaction(Database db) {
        transactionStartSnapshot = db.createMemento();
    }
    
    public void commit() {
        // Discard snapshot
        transactionStartSnapshot = null;
    }
    
    public void rollback(Database db) {
        db.restoreFromMemento(transactionStartSnapshot);
        transactionStartSnapshot = null;
    }
}
```

### 3. Game State - Save/Load

```java
// Game state
class GameState {
    private int health, mana;
    private Map<String, Item> inventory;
    
    public GameMemento createMemento() {
        return new GameMemento(health, mana, inventory);
    }
    
    public void restoreFromMemento(GameMemento m) {
        health = m.getHealth();
        mana = m.getMana();
        inventory = m.getInventory();
    }
}

// Save/Load
class SaveManager {
    private Map<String, GameMemento> saves = new HashMap<>();
    
    public void save(GameState game, String filename) {
        saves.put(filename, game.createMemento());
    }
    
    public void load(GameState game, String filename) {
        game.restoreFromMemento(saves.get(filename));
    }
}
```

### 4. Configuration Management - Checkpoint

```java
// Configuration state
class AppConfig {
    private Map<String, String> settings;
    
    public ConfigMemento checkpoint() {
        return new ConfigMemento(settings);
    }
    
    public void revertToCheckpoint(ConfigMemento m) {
        settings = m.getSettings();
    }
}

// Usage
AppConfig config = new AppConfig();
ConfigMemento beforeUpdate = config.checkpoint();

config.updateSetting("theme", "dark");  // Change
config.updateSetting("language", "es");

// Issues found
config.revertToCheckpoint(beforeUpdate);  // Back to before
```

### 5. Workflow State - Process Snapshots

```java
// Order processing
class Order {
    private String status;
    private double total;
    private List<Item> items;
    
    public OrderMemento snapshot() {
        return new OrderMemento(status, total, items);
    }
    
    public void restore(OrderMemento m) {
        status = m.getStatus();
        total = m.getTotal();
        items = m.getItems();
    }
}

// Workflow
class OrderWorkflow {
    private LinkedList<OrderMemento> checkpoints = new LinkedList<>();
    
    public void processStep(Order order) {
        checkpoints.add(order.snapshot());
        // Process order
    }
    
    public void revertToPreviousStep(Order order) {
        order.restore(checkpoints.removeLast());
    }
}
```

---

## Advantages

✅ **Encapsulation Preserved**
- Only originator knows internal structure
- Memento is opaque container
- Client doesn't need internal knowledge

✅ **Clean Responsibility Separation**
- Originator: Create/restore snapshots
- Caretaker: Manage mementos
- Clear roles

✅ **Undo/Redo Support**
- Natural fit for history management
- Multiple snapshots independent
- Easy to navigate history

✅ **Easy to Implement**
- Straightforward pattern
- Clear implementation steps
- Works with any object

✅ **Supports Complex Operations**
- Combine with Command, Composite
- Multi-step transactions
- Nested undo/redo

✅ **State Consistency**
- Snapshots capture complete state
- Easy to verify consistency
- Point-in-time guarantees

---

## Disadvantages

❌ **Memory Overhead**
- Memento stores entire state copy
- Multiple mementos → significant memory
- O(n) storage per memento

❌ **Performance Cost**
- Deep copying expensive
- Restoration takes time
- Not suitable for very large objects

❌ **Serialization Complexity**
- Large nested objects hard to copy
- Circular references problematic
- Serialization cost

❌ **Memento Fragility**
- Easy to break immutability accidentally
- Deep copy requirements easy to miss
- Shared references cause bugs

❌ **Limited History Management**
- Caretaker responsible for cleanup
- Easy to run out of memory
- No built-in expiration policy

❌ **Tight Coupling to State Structure**
- Change object structure → update memento
- Add/remove fields → breaking change
- Not forward-compatible

❌ **Version Control Issues**
- Multiple object versions in history
- Memory bloat with large histories
- Difficult to garbage collect unused mementos

---

## When to Use Memento Pattern

✅ **Use Memento when:**
- Need undo/redo functionality
- Transaction rollback required
- Checkpointing system states
- Version history management
- Save/load features
- State needs protection from external modification

❌ **Avoid Memento when:**
- Object state extremely large
- Snapshots very frequent
- Memory constraints critical
- Simple operations (not worth overhead)
- State rarely changes
- No undo requirement

---

## Common Implementation Issues

### ❌ Issue 1: Shallow Copy in Memento

```java
// ❌ BAD: Snapshot shares references
class BadMemento {
    private Map<String, String> data;
    
    public BadMemento(Map<String, String> dbData) {
        this.data = dbData;  // SHALLOW COPY!
    }
}

// Original and snapshot both change:
Map<String, String> records = new HashMap<>();
records.put("user1", "Naman");

BadMemento memento = new BadMemento(records);
records.put("user2", "Mishti");  // Memento ALSO changes!

// ✅ GOOD: Independent snapshot
class GoodMemento {
    private Map<String, String> data;
    
    public GoodMemento(Map<String, String> dbData) {
        this.data = new HashMap<>(dbData);  // DEEP COPY!
    }
}

// Original and snapshot independent:
Map<String, String> records = new HashMap<>();
records.put("user1", "Naman");

GoodMemento memento = new GoodMemento(records);
records.put("user2", "Mishti");  // Memento unchanged
```

---

### ❌ Issue 2: Modifying Memento After Creation

```java
// ❌ BAD: Memento not immutable
class BadMemento {
    public Map<String, String> data;  // Public!
    
    public BadMemento(Map<String, String> d) {
        this.data = new HashMap<>(d);
    }
}

BadMemento memento = new BadMemento(records);
memento.data.put("hack", "corrupted");  // Corrupts snapshot!

// ✅ GOOD: Immutable memento
class GoodMemento {
    private Map<String, String> data;  // Private!
    
    public GoodMemento(Map<String, String> d) {
        this.data = new HashMap<>(d);
    }
    
    public Map<String, String> getState() {
        return Collections.unmodifiableMap(data);  // Immutable!
    }
}

GoodMemento memento = new GoodMemento(records);
memento.getState().put("hack", "corrupted");  // Exception!
```

---

### ❌ Issue 3: Caretaker Modifying Memento

```java
// ❌ BAD: Caretaker can modify
class BadCaretaker {
    private DatabaseMemento memento;
    
    public void corruptSnapshot() {
        memento.getData().put("corrupted", "data");
    }
}

// ✅ GOOD: Caretaker only stores
class GoodCaretaker {
    private DatabaseMemento memento;
    
    public void saveSnapshot(Database db) {
        memento = db.createMemento();  // Only storing
    }
    
    public void restoreSnapshot(Database db) {
        db.restoreFromMemento(memento);  // Only using
    }
    
    // No methods to access/modify memento data
}
```

---

### ❌ Issue 4: Unlimited History Causing Memory Leak

```java
// ❌ BAD: History grows unbounded
class BadHistory {
    private List<DatabaseMemento> history = new ArrayList<>();
    
    public void saveState(Database db) {
        history.add(db.createMemento());  // Grows forever!
    }
}

// After 1 million operations:
// | 1MB memento × 1M = 1GB RAM
// | If each memento is larger: 10GB+
// | Memory leak!

// ✅ GOOD: Limited history
class GoodHistory {
    private LinkedList<DatabaseMemento> history = new LinkedList<>();
    private static final int MAX_SIZE = 100;
    
    public void saveState(Database db) {
        history.addLast(db.createMemento());
        if (history.size() > MAX_SIZE) {
            history.removeFirst();  // Keep only last 100
        }
    }
}
```

---

### ❌ Issue 5: Nested Objects Not Deeply Copied

```java
// ❌ BAD: Nested Address not copied
class Person {
    private String name;
    private Address address;
}

class UndeepMemento {
    private Person person;  // Shallow copy!
    
    public UndeepMemento(Person p) {
        this.person = p;  // Address still shared
    }
}

Person p = new Person("John");
p.setAddress(new Address("123 Main"));

UndeepMemento m = new UndeepMemento(p);
p.getAddress().setStreet("456 Oak");  // Memento also changes!

// ✅ GOOD: Full deep copy
class DeepMemento {
    private String name;
    private AddressCopy addressCopy;  // Separate copy!
    
    public DeepMemento(Person p) {
        this.name = p.getName();
        Address origAddress = p.getAddress();
        this.addressCopy = new AddressCopy(
            origAddress.getStreet(),
            origAddress.getCity()
        );
    }
}

Person p = new Person("John");
p.setAddress(new Address("123 Main"));

DeepMemento m = new DeepMemento(p);
p.getAddress().setStreet("456 Oak");  // Memento unchanged
```

---

## Time & Space Complexity

| Operation | Time | Space |
|-----------|------|-------|
| createMemento() | O(n) where n = state size | O(n) for copy |
| restoreFromMemento() | O(n) | O(1) modified state |
| Store memento (caretaker) | O(1) | O(1) reference |
| Access memento | O(1) | O(1) |
| History with m mementos | O(m) space | O(m × n) total |

**Optimization Example:**
```
Without optimization:
- 100,000 operations
- Each memento: 1MB (100 records × 10KB)
- Total: 100GB memory!

With optimization:
- Compress 10:1 → 10GB
- Limit history to 100 → 100MB
- Incremental snapshots → 1GB
```

---

## Design Principles

### SRP (Single Responsibility Principle)
```
✅ Followed:
- Originator: Save/restore state
- Memento: Store snapshot
- Caretaker: Manage mementos
Each has one reason to change
```

### OCP (Open/Closed Principle)
```
✅ Followed:
- Open for new caretaker implementations
- Closed for memento modification
- Can extend history strategies
```

### LSP (Liskov Substitution Principle)
```
✅ Followed:
- Mementos are interchangeable
- Any memento can restore state
- Polymorphic history management
```

---

## Interview Scenario

**Interviewer:** "Design a transactional database system where:
1. Operations: insert, update, delete
2. Business requirement: ACID compliance with rollback
3. Future: May add savepoints
4. Constraint: Must preserve encapsulation"

**Good Answer:**

```java
// 1. Memento - snapshot container
class DatabaseSnapshot {
    private Map<String, String> data;
    
    private DatabaseSnapshot(Map<String, String> d) {
        this.data = new HashMap<>(d);  // Deep copy
    }
    
    Map<String, String> getState() {
        return data;
    }
}

// 2. Originator - knows how to snapshot
class Database {
    private Map<String, String> records;
    
    public DatabaseSnapshot snapshot() {
        return new DatabaseSnapshot(records);
    }
    
    public void restore(DatabaseSnapshot snap) {
        records = new HashMap<>(snap.getState());
    }
    
    public void insert(String k, String v) { records.put(k, v); }
    public void delete(String k) { records.remove(k); }
}

// 3. Caretaker - manages mementos
class TransactionManager {
    private DatabaseSnapshot backup;
    private LinkedList<DatabaseSnapshot> savepoints = new LinkedList<>();
    
    public void begin(Database db) {
        backup = db.snapshot();  // Backup for rollback
    }
    
    public void createSavepoint(Database db) {
        savepoints.add(db.snapshot());  // For future feature
    }
    
    public void commit() {
        backup = null;  // Success - discard backup
    }
    
    public void rollback(Database db) {
        if (backup != null) {
            db.restore(backup);  // Restore
            backup = null;
        }
    }
    
    public void restoreToSavepoint(Database db, int index) {
        if (index >= 0 && index < savepoints.size()) {
            db.restore(savepoints.get(index));
        }
    }
}

// 4. Usage
Database db = new Database();
TransactionManager tm = new TransactionManager();

tm.begin(db);
db.insert("user1", "Naman");
tm.createSavepoint(db);

db.insert("user2", "Mishti");
tm.createSavepoint(db);

db.insert("user3", "Shivam");

// Error - rollback
tm.rollback(db);  // Back to user1

// Later - restore to savepoint
tm.restoreToSavepoint(db, 1);  // user1 + user2
```

**Why This Works:**
- ✅ Encapsulation protected (DB creates/restores snapshots)
- ✅ ACID rollback (full state restoration)
- ✅ Extensible (savepoints can be added)
- ✅ Clear separation (Originator, Caretaker, Memento roles)
- ✅ Memory managed (caretaker controls history)

---

# Quick Notes and Diagram

![Memento Pattern Diagram](MementoPattern.java)

The diagram illustrates the complete Memento Pattern architecture:

**Components Shown:**

1. **Database (Originator)**
   - `map<String,String> mp` - stores current state
   - `createMemento()` - creates backup snapshot
   - `restore()` - restores from backup
   - Knows internal structure completely

2. **Memento (Snapshot)**
   - `map<String,String> mp` - stores state snapshot
   - `getState()` - provides access to snapshot
   - Independent copy of database state
   - Immutable after creation

3. **CareTaker (History Manager)**
   - `memento m` - stores memento reference
   - `begin()` - starts transaction (saves snapshot)
   - `commit()` - confirms transaction (discards backup)
   - `Rollback()` - reverts transaction (restores snapshot)
   - Manages snapshots but doesn't know internals

**Relationships Shown:**

- Database HAS-A Memento (creates snapshots)
- CareTaker HAS-A Memento (stores/manages snapshots)
- Client interacts with Database and CareTaker

**Transaction Flow Illustrated:**

1. **begin()** - CareTaker calls Database.createMemento()
2. **commit()** - CareTaker discards memento
3. **Rollback()** - CareTaker calls Database.restore(memento)

**Key Insight from Diagram:**

- Database isolated from client (no direct state access)
- Memento serves as intermediary container
- CareTaker orchestrates transactions
- Encapsulation maintained throughout
- Clear separation: Originator (create), Memento (contain), Caretaker (manage)

---

## Key Interview Takeaways

1. **Purpose**: Save/restore state while preserving encapsulation
2. **Key Components**: Originator (create/restore), Memento (snapshot), Caretaker (manage)
3. **Deep Copy**: CRITICAL - prevent shared references in snapshots
4. **Three Roles**: Clear responsibility separation
5. **Undo/Redo**: Combine with stack for powerful history
6. **Memory**: Trade-off between history size and memory usage
7. **Immutability**: Mementos should be immutable
8. **ACID**: Perfect for database transactions and rollback
9. **Limitations**: Memory overhead, performance cost, state structure coupling
10. **Combinations**: Works well with Command Pattern for powerful undo/redo

---

## When to Mention in Interview

✅ **Mention Memento when:**
- Asked about undo/redo implementation
- Discussing transaction management
- Designing state recovery systems
- Talking about "checkpoint" or "savepoint"
- Need to preserve encapsulation with state copies
- Designing game save/load systems

❌ **Don't over-complicate with Memento:**
- Few snapshots needed
- Memory is critical constraint
- Simple state (just use serialization)
- No history management needed
- Frequent structure changes expected

