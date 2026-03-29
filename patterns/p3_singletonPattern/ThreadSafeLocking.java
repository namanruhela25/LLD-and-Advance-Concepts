package patterns.p3_singletonPattern;

public class ThreadSafeLocking {
    private static ThreadSafeLocking instance = null;

    private ThreadSafeLocking () {
        System.out.println("Thread safe single locking");
    }

    public static ThreadSafeLocking getInstance() {
        synchronized (ThreadSafeLocking.class) {
            if(instance == null) {
                instance = new ThreadSafeLocking();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        ThreadSafeLocking t1 = ThreadSafeLocking.getInstance();
        ThreadSafeLocking t2 = ThreadSafeLocking.getInstance();

        System.out.println("Both are sanem : " + (t1 == t2));

    }
    
}
