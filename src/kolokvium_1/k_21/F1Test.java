//package kolokvium_1.k_21;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class ParseLap{
    static LocalTime time(String s){
        int[] parts = Arrays.stream(s.split(":")).mapToInt(i -> Integer.parseInt(i)).toArray();
        LocalTime lt = LocalTime.of(0,parts[0], parts[1], parts[2]*=1000000);
        return lt;
    }
}
class Driver implements Comparable<Driver> {
    private String name;
    private LocalTime bestLap;

    public Driver(String name, LocalTime bestLap) {
        this.name = name;
        this.bestLap = bestLap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getBestLap() {
        return bestLap;
    }

    public void setBestLap(LocalTime bestLap) {
        this.bestLap = bestLap;
    }

    @Override
    public int compareTo(Driver o) {
        return bestLap.compareTo(o.bestLap);
    }
}
class F1Race {
    private ArrayList<Driver> drivers;

    public F1Race() {
        this.drivers = new ArrayList<Driver>();
    }
    void readResults(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            String[] s = sc.nextLine().split(" ");
            String driverName = s[0];
            LocalTime maxLap = ParseLap.time(s[1]);
            for(int i = 2; i <= 3;i++){
                LocalTime currentLap = ParseLap.time(s[i]);
                if(maxLap.isAfter(currentLap)){
                    maxLap = currentLap;
                }
            }
            Driver driver = new Driver(driverName,maxLap);
            drivers.add(driver);
        }
        Collections.sort(drivers, Driver::compareTo);
    }
    void printSorted(OutputStream outputStream){
        PrintWriter writer = new PrintWriter (outputStream) ;
        for (int i = 0; i < drivers.size(); i++) {
            Driver d = drivers.get(i);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("m:ss:SSS");
            String s = String.format("%d. %-10s%10s\n", i+1, d.getName(), d.getBestLap().format(df));
            writer.print(s);
        }
        for (Driver d: drivers) {
        }
        writer.flush();
        writer.close();

    }
}