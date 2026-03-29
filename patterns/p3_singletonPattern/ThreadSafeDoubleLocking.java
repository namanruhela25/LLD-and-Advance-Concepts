package patterns.p3_singletonPattern;

public class ThreadSafeDoubleLocking {
    private static ThreadSafeDoubleLocking instance = null;

    private ThreadSafeDoubleLocking () {
        System.out.println("Thread safe single locking");
    }

    public static ThreadSafeDoubleLocking getInstance() {
        if(instance == null) { // double locking 
            synchronized (ThreadSafeDoubleLocking.class) {
                if(instance == null) {
                    instance = new ThreadSafeDoubleLocking();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {
        ThreadSafeDoubleLocking t1 = ThreadSafeDoubleLocking.getInstance();
        ThreadSafeDoubleLocking t2 = ThreadSafeDoubleLocking.getInstance();
        System.out.println("Both are sanem : " + (t1 == t2));
    }

}
