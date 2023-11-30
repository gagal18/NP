package kolokvium_1.k_27;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Risk{
    public Risk() {}
    void processAttacksData(InputStream is){
        Scanner sc = new Scanner(is);
        while(sc.hasNextLine()){
            int attacker = 0;
            int attacked = 0;
            String line = sc.nextLine();
            int[] attackerDice = Arrays.stream(line.split(";")[0].split(" ")).mapToInt(i -> Integer.parseInt(i)).toArray();
            int[] attackedDice = Arrays.stream(line.split(";")[1].split(" ")).mapToInt(i -> Integer.parseInt(i)).toArray();
            Arrays.sort(attackedDice);
            Arrays.sort(attackerDice);
            for (int i = 0; i < 3; i++) {
                if(attackerDice[i] > attackedDice[i]){
                    attacker++;
                }else{
                    attacked++;
                }
            }
            System.out.println(String.format("%d %d", attacker, attacked));
        }

    }
}
public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
