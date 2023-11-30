package kolokvium_1.k_02;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class IrregularCanvasException extends Exception {
    IrregularCanvasException(String id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, maxArea));
    }
}
enum SHAPE{
    CIRCLE,
    SQUARE
}
abstract class Shape implements Comparable<Shape> {
    protected SHAPE shape_type;
    protected int size_side;

    public Shape(SHAPE shape_type, int size_side) {
        this.shape_type = shape_type;
        this.size_side = size_side;
    }

    abstract double area();

    public SHAPE getShapeType() {
        return shape_type;
    }

    @Override
    public int compareTo(Shape o) {
        return Double.compare(area(), o.area());
    }
}
class Circle extends Shape{
    public Circle(int size_side) {
        super(SHAPE.CIRCLE, size_side);
    }

    @Override
    double area() {
        return Math.pow(size_side,2) * Math.PI;
    }
}
class Square extends Shape{
    public Square(int size_side) {
        super(SHAPE.SQUARE, size_side);
    }

    @Override
    double area() {
        return Math.pow(size_side,2);
    }
}
class Canvas implements Comparable<Canvas>{
    private String canvasId;
    private ArrayList<Shape> shapes;

    Canvas(String canvasId, ArrayList<Shape> shapes) {
        this.canvasId = canvasId;
        this.shapes = shapes;
    }

    public Canvas(String canvasId) {
        this.canvasId = canvasId;
        this.shapes = new ArrayList<>();
    }

    public void addShape(Shape s){
        shapes.add(s);
    }
    public String getCanvasId() {
        return canvasId;
    }
    public double getMaxArea(){
        return Collections.max(shapes).area();
    }
    public double getMinArea(){
        return Collections.min(shapes).area();
    }
    public double getTotalArea(){
        return shapes.stream().mapToDouble(i -> i.area()).sum();
    }
    public double getAverageArea(){
        return getTotalArea()/shapes.size();
    }
    @Override
    public int compareTo(Canvas o) {
        return Double.compare(getTotalArea(), o.getTotalArea());
    }
    public long getSquares() {
        return shapes.stream().filter(x -> x.getShapeType().equals(SHAPE.SQUARE)).count();
    }

    public long getCircles() {
        return shapes.stream().filter(x -> x.getShapeType().equals(SHAPE.CIRCLE)).count();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d %d %.2f %.2f %.2f", canvasId, shapes.size(), getCircles(), getSquares(), getMinArea(), getMaxArea(), getAverageArea());
    }
}
class ShapesApplication{
    private double max_area;
    private ArrayList<Canvas> canvas_list;
    public ShapesApplication(double max_area) {
        this.max_area = max_area;
        this.canvas_list = new ArrayList<>();
    }
    void addCanvas(Canvas c) throws IrregularCanvasException {
        if(c.getMaxArea() > max_area) {
            throw new IrregularCanvasException(c.getCanvasId(), max_area);
        }
        canvas_list.add(c);
    }
    public int readCanvases(InputStream is) {
        Scanner sc = new Scanner(is);
        int count =  0;
        while(sc.hasNextLine()){
            String line[] = sc.nextLine().split(" ");
            String canvas_id = line[0];
            Canvas c = new Canvas(canvas_id);
            for (int i = 1; i < line.length; i+=2) {
                if(line[i].equals("C")){
                    Shape s = new Circle(Integer.parseInt(line[i+1]));
                    c.addShape(s);
                }else{
                    Shape s = new Square(Integer.parseInt(line[i+1]));
                    c.addShape(s);
                }
            }
            try {
                addCanvas(c);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return count;
    }
    public void printCanvases(OutputStream os) {
        PrintWriter writer = new PrintWriter(os);

        ArrayList<Canvas> srt = canvas_list;
        srt.sort(Collections.reverseOrder());

        for(Canvas c : canvas_list) {
            writer.println(c);
        }

        writer.flush();
    }
}
public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}