package solid.open_close_principle;


import java.util.ArrayList;
import java.util.List;

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


/*
To follow the Open/Closed Principle, we can define an interface for storage and implement it for different storage types. This way, we can add new storage types without modifying existing code.
*/
interface DBStorage {
    void save(ShoppingCart shoppingCart);
}

class SQLStorage implements DBStorage {
    @Override
    public void save(ShoppingCart shoppingCart) {
        System.out.println("Total value to be store : " + shoppingCart.calculateTotal());
        System.out.println("Saving shopping cart data to SQL database...");
    }

}

class MongoDBStorage implements DBStorage {
    @Override
    public void save(ShoppingCart shoppingCart) {
        System.out.println("Total value to be store : " + shoppingCart.calculateTotal());
        System.out.println("Saving shopping cart data to MongoDB database...");
    }
}


class FileStorage implements DBStorage {
    @Override
    public void save(ShoppingCart shoppingCart) {
        System.out.println("Saving shopping cart data to file...");
    }
}


public class OCPFollowed {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("Laptop", 1000));
        cart.addProduct(new Product("Mouse", 50));

        InvoicePrinter printer = new InvoicePrinter(cart);
        printer.printInvoice();

        DBStorage sqlStorage = new SQLStorage();
        sqlStorage.save(cart);

        DBStorage mongoDBStorage = new MongoDBStorage();
        mongoDBStorage.save(cart);

        DBStorage fileStorage = new FileStorage();
        fileStorage.save(cart);
    }
}
