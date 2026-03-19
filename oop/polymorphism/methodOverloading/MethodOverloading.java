package oop.polymorphism.methodOverloading;

/**
 * Method Overloading is a way to implement Compile-time (Static) Polymorphism
 * 
 * A class can have multiple methods with the SAME NAME but DIFFERENT:
 * 1. Number of parameters
 * 2. Type of parameters
 * 3. Order of parameters
 * 
 * Return type alone is NOT sufficient for method overloading
 */


class Calculator {

    // Method 1: Add two integers
    public int add(int a, int b) {
        System.out.println("Called: add(int, int)");
        return a + b;
    }

    // Method 2: Add three integers (Different number of parameters)
    public int add(int a, int b, int c) {
        System.out.println("Called: add(int, int, int)");
        return a + b + c;
    }

    // Method 3: Add two double values (Different type of parameters)
    public double add(double a, double b) {
        System.out.println("Called: add(double, double)");
        return a + b;
    }

    // Method 4: Add two long values (Different type of parameters)
    public long add(long a, long b) {
        System.out.println("Called: add(long, long)");
        return a + b;
    }

    // Method 5: Different order of parameters (String and int)
    public String add(String a, int b) {
        System.out.println("Called: add(String, int)");
        return a + b;
    }

    // Method 6: Different order of parameters (int and String)
    public String add(int a, String b) {
        System.out.println("Called: add(int, String)");
        return a + b;
    }

    // Method 7: Add two float values
    public float add(float a, float b) {
        System.out.println("Called: add(float, float)");
        return a + b;
    }
}
public class MethodOverloading {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        System.out.println("=== METHOD OVERLOADING DEMONSTRATION ===\n");

        // Example 1: add(int, int)
        System.out.println("Example 1: Adding two integers");
        int result1 = calculator.add(10, 20);
        System.out.println("Result: " + result1);
        System.out.println();

        // Example 2: add(int, int, int)
        System.out.println("Example 2: Adding three integers");
        int result2 = calculator.add(10, 20, 30);
        System.out.println("Result: " + result2);
        System.out.println();

        // Example 3: add(double, double)
        System.out.println("Example 3: Adding two doubles");
        double result3 = calculator.add(10.5, 20.5);
        System.out.println("Result: " + result3);
        System.out.println();

        // Example 4: add(long, long)
        System.out.println("Example 4: Adding two longs");
        long result4 = calculator.add(100L, 200L);
        System.out.println("Result: " + result4);
        System.out.println();

        // Example 5: add(float, float)
        System.out.println("Example 5: Adding two floats");
        float result5 = calculator.add(5.5f, 10.5f);
        System.out.println("Result: " + result5);
        System.out.println();

        // Example 6: add(String, int)
        System.out.println("Example 6: Concatenating String and int");
        String result6 = calculator.add("Total: ", 100);
        System.out.println("Result: " + result6);
        System.out.println();

        // Example 7: add(int, String)
        System.out.println("Example 7: Concatenating int and String");
        String result7 = calculator.add(50, " is the answer");
        System.out.println("Result: " + result7);
        System.out.println();

        // Important Interview Point: Type Promotion
        System.out.println("=== TYPE PROMOTION IN METHOD OVERLOADING ===\n");
        
        // int argument will be promoted to double
        System.out.println("Example: Passing int when double is expected");
        double result8 = calculator.add(5, 10);  // int values will be promoted to double
        System.out.println("Result: " + result8);
    }
}
