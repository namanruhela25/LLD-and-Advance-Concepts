package solid.single_responsibility_principle;

import java.util.ArrayList;
import java.util.List;

/*
SRP Violated: A class has multiple responsibilities and reasons to change.
1. ShoppingCart: Responsible for managing products, calculating total, printing invoice, and saving to  DB. This violates SRP as changes to invoice format or DB logic would require changes to ShoppingCart.
*/

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class ShoppingCart {
    private List<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public void checkout() {
        // Code to process payment and complete the order
        System.out.println("Checking out the Cart with total: " + calculateTotal());
    }

    // 2. Violating SRP - Prints invoice (Should be in a separate class)
    public void printInvoice() {
        System.out.println("Shopping Cart Invoice:");
        for (Product p : products) {
            System.out.println(p.getName() + " - Rs " + p.getPrice());
        }
        System.out.println("Total: Rs " + calculateTotal());
    }

    // 3. Violating SRP - Saves to DB (Should be in a separate class)
    public void saveToDatabase() {
        System.out.println("Saving shopping cart to database...");
    }
}

public class SRPViolated {
    public static void main(String[] args) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addProduct(new Product("Laptop", 50000));
        shoppingCart.addProduct(new Product("Mouse", 1500));
        shoppingCart.checkout();
        shoppingCart.printInvoice();
        shoppingCart.saveToDatabase();
    }
}
