package kolokvium_2.k_01;//package k2;

import java.util.*;

class Audition {
    // grad -> code -> users
    private class User implements Comparable<User> {
        protected String city;
        protected String code;
        protected String name;
        protected int age;

        public User(String city, String code, String name, int age) {
            this.city = city;
            this.code = code;
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s",code,name,age);
        }


        @Override
        public int compareTo(User o) {
            if(name.equals(o.name)){
                return Integer.compare(this.age , o.age);
            }
            return name.compareTo(o.name);
        }
    }

    Map<String, Map<String, User>> dic = new HashMap<>();
    /**
     * {@link Audition city->code->users od grad}
     * */
    void addParticpant(String city, String code, String name, int age) {
        User user = new User(city, code, name, age);
        HashMap<String, User> newUserMap = new HashMap<>();
        newUserMap.put(code, user);

//        if(dic.get(city) != null){
//            dic.get(city).put(code, user)
//        }
//        dic.putIfAbsent(city,newUserMap);
        // key -> null


        if(dic.isEmpty() || dic.get(city) == null ) {
            dic.put(city, newUserMap);
        }else
            (dic.get(city)).putIfAbsent(code,user);

    }


    void listByCity(String city) {
        dic.get(city)
                .values()
                .stream()
                .sorted()
                .forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}
