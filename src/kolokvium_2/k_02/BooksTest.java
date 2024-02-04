//package kolokvium_2.k_02;

import java.util.*;
import java.util.stream.Collectors;


class Book implements Comparable<Book>{
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
//        return title + " (" + category +") " + price;
        return String.format("%s (%s) %.2f", title, category, price);
    }

    @Override
    public int compareTo(Book o) {
        int priceComparison = Float.compare(this.price, o.price);
        if (priceComparison != 0) {
            return priceComparison;
        }
        return this.title.compareTo(o.title);
    }

    public String getTitle() {
        return title;
    }
}

class BookCollection{
    private ArrayList<Book> books;

    public BookCollection() {
        books = new ArrayList<>();
    }

    void addBook(Book book){
        books.add(book);
    }
    void printByCategory(String category){
        List<Book> catBooks = books.stream()
                .filter(x -> x.getCategory().equals(category))
                .sorted(Comparator.comparing(Book::getTitle).thenComparing(Book::compareTo))
                .collect(Collectors.toList());
        for (Book book: catBooks) {
            System.out.println(book);
        }
    }
    public List<Book> getCheapestN(int n){
        return books.stream().sorted().limit(n).collect(Collectors.toList());
    }
}
public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}