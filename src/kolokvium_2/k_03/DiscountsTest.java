//package kolokvium_2.k_03;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */

class DiscPrice implements Comparable<DiscPrice>{
    int orginalPrice;
    int discountedPrice;
    int discountAmount;

    public DiscPrice(int orginalPrice, int discountedPrice, int discountAmount) {
        this.orginalPrice = orginalPrice;
        this.discountedPrice = discountedPrice;
        this.discountAmount = discountAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public int getOrginalPrice() {
        return orginalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    @Override
    public int compareTo(DiscPrice o) {
        return Integer.compare(discountAmount, o.discountAmount);
    }
}
class Store{
    float averageDiscount;
    int totalDiscount;
    String name;
    ArrayList<DiscPrice> discounts;

    public Store(String name) {
        this.name = name;
        averageDiscount = 0;
        totalDiscount = 0;
        discounts = new ArrayList<>();
    }
    void addDiscount(DiscPrice disc){
        discounts.add(disc);
    }

    public void setTotalDiscount(int totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public void setAverageDiscount(float averageDiscount) {
        this.averageDiscount = averageDiscount;
    }

    public float getAverageDiscount() {
        return averageDiscount;
    }

    public int getTotalDiscount() {
        return totalDiscount;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Average discount: ").append(String.format("%.1f", averageDiscount)).append("%\n");
        sb.append("Total discount: ").append(totalDiscount).append("\n");
        int lastIndex = discounts.size() - 1;
        discounts.sort(Comparator.comparingInt(DiscPrice::getDiscountAmount)
                .thenComparingInt(DiscPrice::getOrginalPrice).reversed());
        for (int i = 0; i < discounts.size(); i++) {
            DiscPrice disc = discounts.get(i);
            sb.append(String.format("%2d",disc.discountAmount)).append("% ").append(disc.discountedPrice).append("/").append(disc.orginalPrice);
            if (i != lastIndex) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
class Discounts{
    private ArrayList<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream is){
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String[] split = line.split(" ");
            Store currStore = new Store(split[0]);
            int totalDisc = 0;
            for (int i = 1; i < split.length; i++) {
                if(split[i].isEmpty()) continue;
                List<Integer> disc = Arrays.stream(split[i].split(":")).map(x -> Integer.parseInt(x)).collect(Collectors.toList());
                Integer discount = calculateDiscount(disc.get(1), disc.get(0));
                DiscPrice entry = new DiscPrice(disc.get(1), disc.get(0), discount);
                currStore.addDiscount(entry);
                totalDisc += disc.get(1) - disc.get(0);
            }
            float avgDisc = currStore.discounts.stream().map(x -> x.discountAmount).collect(Collectors.summingInt(Integer::intValue));
            currStore.setAverageDiscount(avgDisc/currStore.discounts.size());
            currStore.setTotalDiscount(totalDisc);
            stores.add(currStore);
        }
        return stores.size();
    }

    public List<Store> byTotalDiscount(){
        return stores.stream().sorted(Comparator.comparing(Store::getTotalDiscount)).limit(3).collect(Collectors.toList());
    }
    public List<Store> byAverageDiscount(){
        return stores.stream().sorted(Comparator.comparing(Store::getAverageDiscount).reversed()).limit(3).collect(Collectors.toList());
    }
    public static int calculateDiscount(double originalPrice, double discountedPrice) {
        return (int) (((originalPrice - discountedPrice) / originalPrice) * 100);
    }

}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }

}

// Vashiot kod ovde