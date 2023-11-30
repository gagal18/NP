package labs.labs_6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

class IntegerList{
    private ArrayList<Integer> int_list;

    public IntegerList() {
        this.int_list = new ArrayList<Integer>();
    }

    public IntegerList(Integer... numbers) {
        int_list = new ArrayList<>(Arrays.asList(numbers));

    }

    public void add(int el, int idx){
        int size = int_list.size();
        if(idx > size){
            for (int i = 0; i < idx - size; i++) {
                int_list.add(0);
            }
            int_list.add(el);
            return;
        }
        int_list.add(idx, el);
    }
    public int remove(int idx) {
        throwErrorIfNotValid(idx);
        return int_list.remove(idx);
    }

    public  int size(){
        return int_list.size();
    }
    public int get(int idx) {
        throwErrorIfNotValid(idx);
        return int_list.get(idx);
    }

    public void set(int el, int idx) {
        throwErrorIfNotValid(idx);
        int_list.set(idx, el);
    }

    public void throwErrorIfNotValid(int idx){
        if(idx< 0 || idx >= int_list.size()){
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    public int count(int el){
        int count = 0;
        for (Integer i: int_list) {
            if(i == el) count++;
        }
        return count;
    }
    public void removeDuplicates(){
        for(int i = int_list.size() - 1; i >= 1; i--) {
            for(int j = i - 1; j >= 0; j--) {
                if(Objects.equals(int_list.get(i), int_list.get(j))) {
                    int_list.remove(j);
                    j++;
                    break;
                }
            }
        }
    }
    public int sumFirst(int k){
        if(k< 0) return 0;
        if(k > int_list.size()) k = int_list.size();
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += int_list.get(i);
        }
        return sum;
    }
    public int sumLast(int k){
        if(k< 0) return 0;
        if(k > int_list.size()) k = int_list.size();
        int sum = 0;
        for(int i = int_list.size() - 1; i > int_list.size() - 1 - k; i--) {
            sum += int_list.get(i);
        }
        return sum;
    }
    public void shiftRight(int idx, int k){
        throwErrorIfNotValid(idx);
        int newIdx = (idx + k) % int_list.size();
        Integer e = int_list.remove(idx);
        int_list.add(newIdx, e);
    }
    public void shiftLeft(int idx, int k){
        throwErrorIfNotValid(idx);
        int newIdx = (idx - k) % int_list.size();
        if(newIdx < 0) newIdx = int_list.size() + newIdx;
        Integer e = int_list.remove(idx);
        int_list.add(newIdx, e);
    }
    public IntegerList addValue(int value) {
        IntegerList added = new IntegerList();
        int c = 0;
        for (int element : int_list) {
            added.add(element + value, c);
            c++;
        }

        return added;
    }
}
public class IntegerListTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}