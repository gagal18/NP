package kolokvium_2.k_21;

import java.util.*;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String message) {
        super("Duplicate number: " + message);
    }
}
class PhoneBook {
    Map<String, ArrayList<String>> map;

    PhoneBook() {
        map = new HashMap<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        if (map.containsKey(name)) {
            ArrayList<String> numbers = map.get(name);

            if (numbers.contains(number)) {
                throw new DuplicateNumberException(number);
            } else {
                numbers.add(number);
                numbers.sort((s1, s2) -> {
                    String firstThree1 = s1.substring(0, Math.min(s1.length(), 3));
                    String firstThree2 = s2.substring(0, Math.min(s2.length(), 3));

                    int compareFirstThree = firstThree1.compareTo(firstThree2);

                    if (compareFirstThree == 0) {
                        return s1.substring(3).compareTo(s2.substring(3));
                    }

                    return compareFirstThree;
                });

            }
        } else {
            ArrayList<String> arr = new ArrayList<>();
            arr.add(number);
            map.put(name, arr);
        }
    }
    void contactsByNumber(String number) {
        Map<String, String> numbersFound = new HashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            String name = entry.getKey();
            ArrayList<String> numbers = entry.getValue();

            for (String num : numbers) {
                if (num.contains(number)) {
                    numbersFound.put(num, name);
                }
            }
        }

        if (numbersFound.isEmpty()) {
            System.out.println("NOT FOUND");
            return;
        }

        List<Map.Entry<String, String>> entryList = new ArrayList<>(numbersFound.entrySet());

        Collections.sort(entryList, Comparator.comparing(Map.Entry::getValue));

        for (Map.Entry<String, String> entry : entryList) {
            System.out.println(entry.getValue() + " " + entry.getKey());
        }
    }

    void contactsByName(String name) {
        if (!map.containsKey(name)) {
            System.out.println("NOT FOUND");
            return;
        }
        ArrayList<String> numbers = map.get(name);
        if (numbers != null) {
            for (String number : numbers) {
                System.out.println(name + " " + number);
            }
        }
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }
}
