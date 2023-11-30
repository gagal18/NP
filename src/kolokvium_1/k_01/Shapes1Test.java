package kolokvium_1.k_01;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Canvas{
    private String id;
    private int[] sides;
    private int canvas_size;
    private int sides_count;

    public Canvas(String id, int[] sides, int sides_count) {
        this.id = id;
        this.sides = sides;
        this.sides_count = sides_count;
        canvas_size = Arrays.stream(sides).sum() * 4;
    }

    @Override
    public String toString() {
        return id + " " + sides_count + " " +canvas_size;
    }

    public int getCanvasSize() {
        return canvas_size;
    }
}
class ShapesApplication{
    private ArrayList<Canvas> canvases;

    public ShapesApplication() {
        canvases = new ArrayList<>();
    }
    public int readCanvases(InputStream is){
        Scanner sc = new Scanner(is);
        int count =  0;
        while(sc.hasNextLine()){
            String line[] = sc.nextLine().split(" ");
            String canvas_id = line[0];
            int[] sides = Arrays.stream(Arrays.copyOfRange(line, 1, line.length))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            Canvas canvas = new Canvas(canvas_id, sides, sides.length);
            canvases.add(canvas);
            count+= sides.length;
        }
        return count;
    }
    void printLargestCanvasTo(OutputStream os){
        PrintWriter writer = new PrintWriter (os) ;
        Canvas canvas = canvases.stream().max((a,b) -> a.getCanvasSize() - b.getCanvasSize()).get();
        writer.println(canvas);
        writer.flush();
        writer.close();
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}