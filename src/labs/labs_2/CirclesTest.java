//package labs.labs_2;

import java.util.ArrayList;
import java.util.Scanner;

//EXCEPTIONS
class ObjectCanNotBeMovedException extends Exception {
    ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {
    MovableObjectNotFittableException(String msg) {
        super(msg);
    }
}
enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}
interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
}

class MovablePoint implements Movable{
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y < 0 || y + ySpeed > MovablesCollection.Y_MAX) {
            throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        }
        this.y = y + ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y - ySpeed < 0 || y > MovablesCollection.Y_MAX) {
            throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        }
        this.y = y - ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x < 0 || x + xSpeed > MovablesCollection.X_MAX) {
            throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        }
        this.x = x + xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x - xSpeed < 0 || x > MovablesCollection.X_MAX) {
            throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        }
        this.x = x - xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x +","+ y +")";
    }
}

class MovableCircle implements Movable{
    private int radius;
    private MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }
    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + center.getCurrentXPosition() +","+ center.getCurrentYPosition() +") and radius " + radius;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if(y < 0 || y + radius > MovablesCollection.Y_MAX) {
            throw new ObjectCanNotBeMovedException(x, y);
        }
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if(y - radius < 0 || y > MovablesCollection.Y_MAX) {
            throw new ObjectCanNotBeMovedException(x, y);
        }
        center.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if(x < 0 || x + radius> MovablesCollection.X_MAX) {
            throw new ObjectCanNotBeMovedException(x, y);
        }
        center.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition(), y = getCurrentYPosition();
        if(x - radius < 0 || x > MovablesCollection.X_MAX) {
            throw new ObjectCanNotBeMovedException(x, y);
        }
        center.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    public int getRadius() {
        return radius;
    }
}
class MovablesCollection{
    private ArrayList<Movable> moveable;

    public static int X_MAX;
    public static int Y_MAX;

    MovablesCollection(int x_max, int y_max) {
        moveable = new ArrayList<Movable>();
        X_MAX = x_max;
        Y_MAX = y_max;
    }

    public static void setxMax(int x_max) {
        X_MAX = x_max;
    }

    public static void setyMax(int y_max) {
        Y_MAX = y_max;
    }

    void addMovableObject(Movable obj) throws MovableObjectNotFittableException {
        int objX = obj.getCurrentXPosition();
        int objY = obj.getCurrentYPosition();
        MovablePoint mP = obj instanceof MovablePoint ? ((MovablePoint) obj) : null;
        MovableCircle mC = obj instanceof MovableCircle ? ((MovableCircle) obj) : null;
        if(mP != null){
            if(objX < 0 || objX > X_MAX || objY < 0 || objY > Y_MAX ){
                throw new MovableObjectNotFittableException(obj + " can not be fitted into the collection");
            }
        }else if(mC != null){
            int objR = mC.getRadius();
            if((objX - objR < 0 || objX + objR > X_MAX) || (objY - objR < 0 || objY + objR > Y_MAX) ){
                throw new MovableObjectNotFittableException("Movable circle with center (" + objX + "," + objY + ") and radius " + objR + " can not be fitted into the collection");
            }
        }
        moveable.add(obj);
    }
    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION dir){
        for (Movable m: moveable) {
            MovablePoint movablePoint = m instanceof MovablePoint ? ((MovablePoint) m) : null;
            MovableCircle movableCircle = m instanceof MovableCircle ? ((MovableCircle) m) : null;

            if((type == TYPE.POINT && movablePoint == null) || (type == TYPE.CIRCLE && movableCircle == null)) {
                continue;
            }

            try {
                switch (dir) {
                    case UP:
                        m.moveUp();
                        break;
                    case DOWN:
                        m.moveDown();
                        break;
                    case LEFT:
                        m.moveLeft();
                        break;
                    case RIGHT:
                        m.moveRight();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Collection of movable objects with size ").append(moveable.size()).append(":\n");
        for(Movable m : moveable) {
            str.append(m).append("\n");
        }
        return str.toString();
    }

}

public class CirclesTest {

    public static void main(String[] args) throws MovableObjectNotFittableException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());


    }


}
