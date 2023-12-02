package kolokvium_1.k_09;


import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

class Triple<T extends Number> {
    private List<T> args;

    Triple(T a, T b, T c) {
        this.args = new ArrayList<>();
        args.add(a);
        args.add(b);
        args.add(c);

    }

    public double max() {
         return args.stream().mapToDouble(i -> i.doubleValue()).max().orElse(0);
    }

    public double avarage() {
        return args.stream().mapToDouble(i -> i.doubleValue()).average().orElse(0);
    }

    public void sort() {
        this.args = args.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", args.get(0).doubleValue(), args.get(1).doubleValue(), args.get(2).doubleValue());
    }
}
public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple


