
package oop.polymorphism.methodOverriding;

/**
 * Method Overriding is a way to implement Runtime (Dynamic) Polymorphism
 * 
 * A subclass provides a specific implementation of a method that is already defined
 * in its parent class. The method signature (name and parameters) MUST be the same.
 * 
 * Rules for Method Overriding:
 * 1. Method name must be the same
 * 2. Method parameters must be the same
 * 3. Return type must be the same or a subtype (Covariant Return Type)
 * 4. Method cannot be less accessible than the parent class method
 * 5. Method cannot throw new or broader checked exceptions
 * 6. Parent class method should not be final
 * 7. parent class should not be final
 * 8. Used with inheritance (extends/implements)
 */

// Parent Class (Superclass)
abstract class Shape {
    protected String name;
    protected String color;

    public Shape(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // Abstract method to be overridden
    abstract void draw();
    
    abstract double getArea();

    // Concrete method (not overridden)
    public void displayInfo() {
        System.out.println("Shape Name: " + name + ", Color: " + color);
    }
}

// Child Class 1: Circle
class Circle extends Shape {
    private double radius;

    public Circle(String name, String color, double radius) {
        super(name, color);
        this.radius = radius;
    }

    // Overriding abstract method draw()
    @Override
    void draw() {
        System.out.println("Drawing a " + color + " Circle named " + name);
    }

    // Overriding abstract method getArea()
    @Override
    double getArea() {
        return Math.PI * radius * radius;
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Radius: " + radius);
    }
}

// Child Class 2: Rectangle
class Rectangle extends Shape {
    private double length;
    private double width;

    public Rectangle(String name, String color, double length, double width) {
        super(name, color);
        this.length = length;
        this.width = width;
    }

    // Overriding abstract method draw()
    @Override
    void draw() {
        System.out.println("Drawing a " + color + " Rectangle named " + name);
    }

    // Overriding abstract method getArea()
    @Override
    double getArea() {
        return length * width;
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Length: " + length + ", Width: " + width);
    }
}

// Child Class 3: Triangle
class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(String name, String color, double base, double height) {
        super(name, color);
        this.base = base;
        this.height = height;
    }

    // Overriding abstract method draw()
    @Override
    void draw() {
        System.out.println("Drawing a " + color + " Triangle named " + name);
    }

    // Overriding abstract method getArea()
    @Override
    double getArea() {
        return 0.5 * base * height;
    }

    public void displayInfo() {
        super.displayInfo();
        System.out.println("Base: " + base + ", Height: " + height);
    }
}

public class MethodOverriding {
    public static void main(String[] args) {
        System.out.println("=== METHOD OVERRIDING DEMONSTRATION ===\n");

        // Creating objects of different child classes
        Circle circle = new Circle("MyCircle", "Red", 5);
        Rectangle rectangle = new Rectangle("MyRectangle", "Blue", 10, 8);
        Triangle triangle = new Triangle("MyTriangle", "Green", 6, 4);

        System.out.println("--- Example 1: Direct Object References ---\n");

        // Calling methods using direct references
        circle.displayInfo();
        circle.draw();
        System.out.println("Area: " + circle.getArea());
        System.out.println();

        rectangle.displayInfo();
        rectangle.draw();
        System.out.println("Area: " + rectangle.getArea());
        System.out.println();

        triangle.displayInfo();
        triangle.draw();
        System.out.println("Area: " + triangle.getArea());
        System.out.println();

        // This demonstrates POLYMORPHISM
        System.out.println("--- Example 2: Using Parent Class Reference (Polymorphism) ---\n");

        // Creating parent class references pointing to child class objects
        Shape shape1 = new Circle("CircleShape", "Yellow", 3);
        Shape shape2 = new Rectangle("RectangleShape", "Pink", 12, 6);
        Shape shape3 = new Triangle("TriangleShape", "Purple", 8, 5);

        // Array of parent class type can hold child class objects
        Shape[] shapes = {shape1, shape2, shape3};

        System.out.println("--- Iterating through Shape array ---\n");

        // This demonstrates RUNTIME POLYMORPHISM
        // The actual method called depends on the ACTUAL OBJECT TYPE at runtime
        // NOT the reference type (Shape)
        for (Shape shape : shapes) {
            System.out.println("=== Processing shape ===");
            shape.displayInfo();
            shape.draw();  // Runtime polymorphism - Different method executed
            System.out.println("Area: " + shape.getArea());  // Runtime polymorphism
            System.out.println();
        }

        System.out.println("--- Example 3: Method Overriding with Inheritance Chain ---\n");

        // Demonstrating that the correct overridden method is called at runtime
        System.out.println("Shape reference pointing to Circle object:");
        Shape circleRef = new Circle("CircleRef", "Orange", 4);
        circleRef.draw();  // Calls Circle's draw() method
        System.out.println();

        System.out.println("Shape reference pointing to Rectangle object:");
        Shape rectRef = new Rectangle("RectRef", "Brown", 5, 7);
        rectRef.draw();  // Calls Rectangle's draw() method
        System.out.println();

        System.out.println("Shape reference pointing to Triangle object:");
        Shape triRef = new Triangle("TriRef", "Cyan", 10, 12);
        triRef.draw();  // Calls Triangle's draw() method
        System.out.println();

        System.out.println("--- Example 4: Calculating Total Area ---\n");

        // Real-world example: Processing multiple shapes
        double totalArea = 0;
        for (Shape shape : shapes) {
            totalArea += shape.getArea();  // Polymorphic call to getArea()
        }
        System.out.println("Total Area of all shapes: " + totalArea);
    }
}
