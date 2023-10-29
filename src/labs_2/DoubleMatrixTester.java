package labs_2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.DoubleStream;

class InsufficientElementsException extends Exception {
    public InsufficientElementsException(String message) {
        super(message);
    }
}

class InvalidRowNumberException  extends Exception {
    public InvalidRowNumberException (String message) {
        super(message);
    }
}
class InvalidColumnNumberException   extends Exception {
    public InvalidColumnNumberException  (String message) {
        super(message);
    }
}
class DoubleMatrix{
    private int COLUMNS;
    private int ROWS;

    private double[][] matrix;

    public DoubleMatrix(double a[], int ROWS, int COLUMNS) throws InsufficientElementsException {
        this.COLUMNS = COLUMNS;
        this.ROWS = ROWS;
        this.matrix = new double[ROWS][COLUMNS];
        if(a.length < COLUMNS*ROWS){
            throw new InsufficientElementsException("Insufficient number of elements");
        }
        if(a.length == COLUMNS*ROWS){
            int arrCount = 0;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    matrix[i][j] = a[arrCount];
                    arrCount++;
                }
            }
        }else{
            int arrCount = a.length - COLUMNS*ROWS;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    matrix[i][j] = a[arrCount];
                    arrCount++;
                }
            }
        }

    }
    public String getDimensions() {
        return String.format("[%d x %d]", ROWS, COLUMNS);
    }

    public int rows() {
        return ROWS;
    }

    public int columns() {
        return COLUMNS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return COLUMNS == that.COLUMNS && ROWS == that.ROWS && Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(COLUMNS, ROWS);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                sb.append(String.format("%.2f", matrix[i][j]));
                if (j != COLUMNS - 1) {
                    sb.append("\t");
                }
            }
            if (i != ROWS - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        double[] col = new double[ROWS];
        if(!(column >= 1 && column <= COLUMNS)){
            throw new InvalidColumnNumberException ("Invalid column number");
        }
        for (int i = 0; i < ROWS; i++) {
            col[i] =  matrix[i][column-1];
        }
        return Arrays.stream(col).max().getAsDouble();
    }
    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(!(row >= 1 && row <= ROWS)){
            throw new InvalidRowNumberException ("Invalid row number");
        }
        double[] rows = new double[COLUMNS];
        for (int i = 0; i < COLUMNS; i++) {
            rows[i] =  matrix[row-1][i];
        }
        return Arrays.stream(rows).max().getAsDouble();
    }

    public double sum(){
        double[] arr = toArray();
        return Arrays.stream(arr).sum();
    }

    public double[] toArray(){
        return Arrays.stream(Arrays.stream(matrix).flatMapToDouble(DoubleStream::of).toArray()).sorted().toArray();
    }

    public double[] toSortedArray(){
         double[] arr = toArray();
         double[] sorted = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            sorted[i] = arr[arr.length-i-1];
        }
        return sorted;
    }
}

class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner scanner = new Scanner(input);

        int ROWS = scanner.nextInt();
        int COLUMNS = scanner.nextInt();
        int size = ROWS*COLUMNS;
        double[] array = new double[size];

        for (int i = 0; i < size; i++) {
            array[i] = scanner.nextDouble();
        }

        return new DoubleMatrix(array, ROWS, COLUMNS);
    }

}
public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
