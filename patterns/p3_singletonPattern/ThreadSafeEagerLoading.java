package patterns.p3_singletonPattern;

public class ThreadSafeEagerLoading {
    // we initialize single instance in head when class loads by OS
    private static ThreadSafeEagerLoading instance = new ThreadSafeEagerLoading();

    private ThreadSafeEagerLoading() {
        System.out.println("Thread safe eager loading..");
    }

    public static ThreadSafeEagerLoading getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        ThreadSafeEagerLoading t1 = new ThreadSafeEagerLoading();
        ThreadSafeEagerLoading t2 = new ThreadSafeEagerLoading();
        System.out.println("Both instance are same : " + (t1 == t2));
    }
}