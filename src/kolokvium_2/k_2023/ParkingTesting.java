//package kolokvium_2.k_2023;

import com.sun.source.tree.Tree;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}
class ParkingSpot{
    String registration;
    String spot;
    LocalDateTime timestamp_enter;
    LocalDateTime timestamp_leave;

    boolean isParked;

    public ParkingSpot(String registration, String spot, LocalDateTime timestamp, boolean isParked) {
        this.registration = registration;
        this.spot = spot;
        if(isParked){
            this.timestamp_enter = timestamp;
        }else{
            this.timestamp_leave = timestamp;
        }
        this.isParked = isParked;
    }

    public LocalDateTime getTimestamp_enter() {
        return timestamp_enter;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Registration number: ").append(registration).append(" Spot: ").append(spot);
        sb.append(" Start timestamp: ").append(timestamp_enter);
        if(timestamp_leave != null){
            long duration = ChronoUnit.MINUTES.between(timestamp_enter, timestamp_leave);
            sb.append(" End timestamp: ").append(timestamp_leave);
            sb.append(" Duration in minutes: ").append(duration);
        }
        return sb.toString();
    }
}

class Parking{
    private int capacity;

    private Map<String, List<ParkingSpot>> parkingSpots;

    public Parking(int capacity) {
        this.capacity = capacity;
        parkingSpots = new TreeMap<String, List<ParkingSpot>>();
    }

    public void update(String registration, String spot, LocalDateTime timestamp, boolean entry){
        ParkingSpot pSpot = new ParkingSpot(registration, spot, timestamp, entry);
        if(parkingSpots.containsKey(spot)){
            parkingSpots.get(spot).add(pSpot);
        }else{
            List<ParkingSpot> newSpots =  new ArrayList<ParkingSpot>();
            newSpots.add(pSpot);
            parkingSpots.put(spot, newSpots);
        }
    }
    void currentState(){
        Map<LocalDateTime, ParkingSpot> filtered = new TreeMap<LocalDateTime, ParkingSpot>(Collections.reverseOrder());
        for (Map.Entry<String, List<ParkingSpot>> entry: parkingSpots.entrySet()) {
            List<ParkingSpot> currParked = entry.getValue().stream()
                    .filter(parkingSpot -> parkingSpot.isParked).collect(Collectors.toList());
            filtered.put(currParked.get(0).timestamp_enter,currParked.get(0));
        }
        double perc = (double) 100 / (capacity / filtered.size());

        System.out.printf("Capacity filled: %.2f%%\n", perc);

        for (Map.Entry<LocalDateTime, ParkingSpot> entry: filtered.entrySet()) {
            System.out.println(entry.getValue());
        }

    }
    void history(){
        System.out.println("null");
    }
    public Map<String, Integer> carStatistics(){
        return null;
    }
    public Map<String,Double> spotOccupancy (LocalDateTime start, LocalDateTime end){
        return null;
    }
}

public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                parking.update(registration, spot, timestamp, entrance);
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}
