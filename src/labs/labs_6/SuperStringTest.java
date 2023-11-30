package labs.labs_6;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;



class SuperString {
    private LinkedList<String> string;

    SuperString() {
        string = new LinkedList<>();
    }

    public void append(String s) {
        string.add(s);
    }

    public void insert(String s) {
        string.add(0, s);
    }

    public boolean contains(String s) {
        return toString().contains(s);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String s : string) {
            str.append(s);
        }
        return str.toString();
    }

    public void reverse() {
        Collections.reverse(string);
        for (int i = 0; i < string.size(); i++) {
            String reversed = new StringBuilder(string.get(i)).reverse().toString();
            string.set(i, reversed);
        }
    }

    public void removeLast(int k) {
        for (int i = 0; i < k && !string.isEmpty(); i++) {
            string.removeLast();
        }
    }
}
public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
