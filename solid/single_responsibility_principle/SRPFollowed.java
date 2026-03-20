package solid.single_responsibility_principle;

import java.util.ArrayList;
import java.util.List;

/*
SRP Followed: Each class has a single responsibility and reason to change.
1. ShoppingCart: Only responsible for managing products and calculating total.
2. InvoicePrinter: Only responsible for printing the invoice.
3. ShoppingCartStorage: Only responsible for saving cart to DB.
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

    public List<Product> getProducts() {
        return products;
    }

    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public void checkout() {
        System.out.println("Checking out the Cart with total: " + calculateTotal());
    }
}


class InvoicePrinter {
    private ShoppingCart shoppingCart; // reference taken for further use

    public InvoicePrinter(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void printInvoice() {
        System.out.println("Shopping Cart Invoice:");
        for(Product p : shoppingCart.getProducts()) {
            System.out.println(p.getName() + " - Rs " + p.getPrice());
        }
        System.out.println("Total: Rs " + shoppingCart.calculateTotal());
    }
}


// 3. ShoppingCartStorage: Only responsible for saving cart to DB
class ShoppingCartStorage {
    private ShoppingCart shoppingCart; // reference taken for further use

    public ShoppingCartStorage(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void saveToDatabase(ShoppingCart shoppingCart) {
        System.out.println("Saving shopping cart to database...");
    }
}


public class SRPFollowed {
    public static void main(String[] args) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addProduct(new Product("Laptop", 50000));
        shoppingCart.addProduct(new Product("Mouse", 1500));
        shoppingCart.checkout();

        InvoicePrinter invoicePrinter = new InvoicePrinter(shoppingCart);
        invoicePrinter.printInvoice();

        ShoppingCartStorage shoppingCartStorage = new ShoppingCartStorage(shoppingCart);
        shoppingCartStorage.saveToDatabase(shoppingCart);

    }
}
