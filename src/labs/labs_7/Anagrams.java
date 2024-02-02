package labs.labs_7;

import java.io.InputStream;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        Map<Integer, TreeSet<String>> words = new TreeMap<>();
        while(sc.hasNextLine()) {
            String word = sc.nextLine();
            char [] wordHash = word.toCharArray();
            Arrays.sort(wordHash);
            int hash = new String(wordHash).hashCode();

            if(words.containsKey(hash)){
                words.get(hash).add(word);
            }else{
                words.put(hash, new TreeSet<>());
                words.get(hash).add(word);
            }
        }
        words.values().stream()
                .filter(x -> x.size() >= 5)
                .sorted(Comparator.comparing(TreeSet::first))
                .forEach(x -> System.out.println(String.join(" ", x)));
        // Vasiod kod ovde
    }
}
