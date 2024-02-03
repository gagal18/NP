//package labs.labs_7;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;


class TermFrequency{
    private TreeMap<String, Integer> occurences;
    private int total;

    public TermFrequency(InputStream is, String[] noIncluded) {
        this.total = 0;
        this.occurences = new TreeMap<>();
        Scanner sc = new Scanner(is);
        List<String> wordsToJump = Arrays.asList(noIncluded);
        while(sc.hasNext()){
            String currWord = sc.next();
            currWord = trimAndRemoveSpecialChars(currWord);
            if(currWord.isEmpty() || wordsToJump.contains(currWord)){
                continue;
            }
            int prevOcc = occurences.computeIfAbsent(currWord,x-> 0);
            occurences.put(currWord, ++prevOcc);
            total++;
        }
    }
    public int countTotal(){
        return total;
    }
    public int countDistinct(){
        return occurences.size();
    }

    public List<String> mostOften(int k) {
        return occurences.keySet().stream().sorted(Comparator.comparing(x -> occurences.get(x)).reversed()).limit(k).collect(Collectors.toList());
    }
    public static String trimAndRemoveSpecialChars(String input) {
        String result = input.replaceAll("[,.]", "");
        result = result.toLowerCase();
        return result;
    }
}
public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
// vasiot kod ovde
