// Step 1: Define interface
interface Logger {
    void log(String message);
}

// Step 2: Real implementation
class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}

// Step 3: Null Object — does nothing, no NPE risk
class NullLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("No logging in service 2 ...");
        // Do nothing — intentionally empty
    }
}

// Step 4: Usage — no null checks needed
class Service {
    private Logger logger;

    Service(Logger logger) {
        this.logger = logger; // inject NullLogger if no logging needed
    }

    void doWork() {
        logger.log("Working..."); // safe — never NPE
    }
}

// Main
public class NullObjectPattern {
    public static void main(String[] args) {
        // With real logger
        Service s1 = new Service(new ConsoleLogger());
        s1.doWork(); // prints: LOG: Working...

        // With null logger — no NPE, no if(logger != null)
        Service s2 = new Service(new NullLogger());
        s2.doWork(); // silent, no crash
    }
}