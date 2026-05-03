package patterns.p3_singletonPattern;

class Singleton {
    private static Singleton instance;

    private Singleton() {
        // Private constructor to prevent instantiation
        System.out.println("Singleton instance created");
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

public class SingletonPattern {
    public static void main(String[] args) {
        Singleton singleton1 = Singleton.getInstance();
        Singleton singleton2 = Singleton.getInstance();
        Singleton singleton3 = Singleton.getInstance();

        System.out.println("Singleton 1: " + singleton1);
        System.out.println("Singleton 2: " + singleton2);
        System.out.println("Singleton 3: " + singleton3);

        System.out.println("Are all instances the same? " + (singleton1 == singleton2 && singleton2 == singleton3));
    }
}
