package kolokvium_1.k_25;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}

class Product {
    private String productID;
    private String productName;
    private double productPrice;

    public Product(String productID, String productName, double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }
}

class ShoppingCartItem {
    private Product product;
    private double quantity;

    public ShoppingCartItem(Product product, double quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getProductPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }
    public double getQuantity() {
        return quantity;
    }
}

class ShoppingCart {
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(String itemData) {
        String[] parts = itemData.split(";");
        if (parts.length != 5) {
            throw new InvalidOperationException("Invalid item data format");
        }

        String type = parts[0];
        String productID = parts[1];
        String productName = parts[2];
        double productPrice = Double.parseDouble(parts[3]);
        double quantity = Double.parseDouble(parts[4]);

        if (quantity == 0) {
            throw new InvalidOperationException("Quantity cannot be 0");
        }

        Product product = new Product(productID, productName, productPrice);
        ShoppingCartItem item = new ShoppingCartItem(product, quantity);
        items.add(item);
    }

    public void printShoppingCart(OutputStream os) {
        PrintStream ps = new PrintStream(os);
        Collections.sort(items, (item1, item2) -> Double.compare(item2.getTotalPrice(), item1.getTotalPrice()));

        for (ShoppingCartItem item : items) {
            String productType = (item.getQuantity() % 1 == 0) ? "WS" : "PS";

            ps.println(productType + ";"
                    + item.getProduct().getProductID() + ";"
                    + item.getProduct().getProductName() + ";"
                    + item.getTotalPrice() + ";"
                    + item.getProduct().getProductPrice() + ";"
                    + item.getQuantity());
        }
    }

    public void blackFridayOffer(List<Integer> discountItems, OutputStream os) {
        if (discountItems.isEmpty()) {
            throw new InvalidOperationException("Discount items list is empty");
        }

        double totalSavings = 0;
        PrintStream ps = new PrintStream(os);

        for (ShoppingCartItem item : items) {
            if (discountItems.contains(Integer.parseInt(item.getProduct().getProductID()))) {
                double savings = item.getTotalPrice() * 0.10;
                totalSavings += savings;

                ps.println(item.getProduct().getProductID() + ";"
                        + item.getProduct().getProductName() + ";"
                        + savings);
            }
        }

        ps.println("Total savings: " + totalSavings);
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}