package labs_3;

import java.util.*;
import java.util.stream.Collectors;


// EXCEPTIONS
class EmptyOrder extends Exception {
    public EmptyOrder() {
        super("EmptyOrder");
    }
}
class InvalidExtraTypeException extends Exception{
    public InvalidExtraTypeException() {
        super("InvalidExtraTypeException");
    }
}
class InvalidPizzaTypeException extends Exception{
    public InvalidPizzaTypeException() {
        super("InvalidPizzaTypeException");
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(int i) {
        super(String.format("%d", i));
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
        super("OrderLockedException");
    }
}

// END EXCEPTIONS
// ENUMS
enum EXTRA_ITEM_ENUM{
    Coke(5), Ketchup(3);

    public int getPrice() {
        return price;
    }

    private final int price;

    EXTRA_ITEM_ENUM(int price) {
        this.price = price;
    }

    public static boolean doesExsist(String str){
        return Arrays.stream(EXTRA_ITEM_ENUM.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(str);
    }
}

enum PIZZA_ITEM_ENUM{
    Standard(10), Pepperoni(12), Vegetarian(8);

    public int getPrice() {
        return price;
    }

    private final int price;

    PIZZA_ITEM_ENUM(int price) {
        this.price = price;
    }

    public static boolean doesExsist(String str){
        return Arrays.stream(PIZZA_ITEM_ENUM.values()).map(Enum::name)
                .collect(Collectors.toList()).contains(str);
    }
}

//INTERFACE

interface Item{
    int getPrice();
    String getType();
}

class PizzaItem implements Item{
    String type;

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if(PIZZA_ITEM_ENUM.doesExsist(type)){
            this.type = type;
        }else{
            throw new InvalidPizzaTypeException();
        }
    }


    @Override
    public int getPrice() {
        return PIZZA_ITEM_ENUM.valueOf(type).getPrice();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PizzaItem pizzaItem = (PizzaItem) o;
        return Objects.equals(type, pizzaItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class ExtraItem implements Item{
    String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if(EXTRA_ITEM_ENUM.doesExsist(type)){
            this.type = type;
        }else{
            throw new InvalidExtraTypeException();
        }
    }

    @Override
    public int getPrice() {
        return EXTRA_ITEM_ENUM.valueOf(type).getPrice();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtraItem extraItem = (ExtraItem) o;
        return Objects.equals(type, extraItem.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}

class ItemOrder{
    Item item;
    int count;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ItemOrder(Item item, int count) {
        this.item = item;
        this.count = count;

    }
}

class Order{
    private List<ItemOrder> order_list;
    private boolean locked;

    Order(){
        order_list = new ArrayList<>();
        locked = false;
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (count > 10) {
            throw new ItemOutOfStockException(count);
        }

        if (locked) {
            throw new OrderLockedException();
        }
        for (ItemOrder iO : order_list) {
            if (iO.getItem().equals(item)) {
                iO.setCount(count);
                return;
            }
        }
        ItemOrder nI = new ItemOrder(item, count);
        order_list.add(nI);
    }

    public void removeItem(int idx) throws OrderLockedException {
        if (locked) {
            throw new OrderLockedException();
        }

        if(idx < 0 || idx >= order_list.size()){
            throw new ArrayIndexOutOfBoundsException(idx);
        }
        order_list.remove(idx);

    }

    public int getPrice() {
        return order_list.stream().mapToInt(i -> i.getItem().getPrice() * i.getCount()).sum();
    }

    public void displayOrder() {
        StringBuilder sb = new StringBuilder();

        order_list.stream().forEach(i -> {
            sb.append(String.format("  %d.", order_list.indexOf(i)+1));
            sb.append(String.format("%-15s", i.getItem().getType()));
            sb.append(String.format("x%2d",i.getCount()));
            sb.append(String.format("%5d$", i.getItem().getPrice() * i.getCount()));
            sb.append(String.format("\n"));
        });
        sb.append(String.format("%-22s%5d$", "Total:", getPrice()));

        System.out.println(sb.toString());
    }

    public void lock() throws EmptyOrder {
        if (order_list.size() == 0) {
            throw new EmptyOrder();
        } else {
            locked = true;
        }
    }
}


public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
