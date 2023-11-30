package labs.labs_3;

import java.util.*;
import java.io.*;

class InvalidNumberException extends Exception {
    public InvalidNumberException() {
        super();
    }
}
class InvalidNameException extends Exception {
    public String name;

    public InvalidNameException(String name) {
        super();
        this.name = name;
    }
}
class MaximumSizeExceddedException extends Exception {
    public MaximumSizeExceddedException() {
        super();
    }
}
class InvalidFormatException extends Exception {
    public InvalidFormatException() {
        super();
    }
}
@SuppressWarnings("unchecked")
class Contact implements Comparable<Contact>{
    private String name;
    private List<String> phone_numbers;

    public Contact(String name, String... phone_numbers) throws InvalidNameException, MaximumSizeExceddedException, InvalidNumberException {
        if( matchName(name)){
            this.name = name;
        }else{
            throw new InvalidNameException(name);
        }
        if(phone_numbers.length > 5){
            throw new MaximumSizeExceddedException();
        }
        if(!Arrays.stream(phone_numbers).allMatch(this::matchPhoneNumber)){
            throw new InvalidNumberException();
        }
        this.phone_numbers = new ArrayList<>(List.of(phone_numbers));
    }
    public void addNumber(String phonenumber) throws MaximumSizeExceddedException, InvalidFormatException {
        if(phone_numbers.size() == 5){
            throw new MaximumSizeExceddedException();
        }
        if(!matchPhoneNumber(phonenumber)){
            throw new InvalidFormatException();
        }
        phone_numbers.add(phonenumber);

    }
    public boolean matchName(String str){
        return str.matches("[a-zA-Z0-9]{5,10}");
    }
    public boolean matchPhoneNumber(String str){
        return str.matches("07[0125678][0-9]{6}");
    }

    public String getName() {
        return name;
    }

    public String[] getNumbers() {
        return phone_numbers.stream().sorted().map(i -> i.toString()).toArray(String[]::new);
    }
    public boolean hasNumber(String s) {
        return Arrays.stream(phone_numbers.toArray()).map(i -> i.toString()).anyMatch(i -> i.startsWith(s));
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(name)
                .append("\n")
                .append(phone_numbers.size())
                .append("\n");

        Arrays.stream(phone_numbers.toArray()).sorted().forEach(i -> sb.append(i).append("\n"));

        return sb.toString();
    }
    @Override
    public int compareTo(Contact o) {
        return name.compareTo(o.name);
    }
}

@SuppressWarnings("unchecked")
class PhoneBook{
    private List<Contact> contacts;

    public PhoneBook() {
        this.contacts = new ArrayList();
    }
    public void addContact(Contact c) throws MaximumSizeExceddedException, InvalidNameException {
        if(contacts.size() > 250){
            throw new MaximumSizeExceddedException();
        }
        if(contactExists(c)){
            throw new InvalidNameException(c.getName());
        }
        contacts.add(c);
    }
    private boolean contactExists(Contact c){
        return contacts.stream().anyMatch(i -> i.getName().equals(c.getName()));
    }
    public Contact getContactForName(String name){
        return contacts.stream().filter(i -> i.getName().equals(name)).findFirst().orElse(null);
    }

    public Contact[] getContacts() {
        return contacts.stream().sorted(Comparator.comparing(Contact::getName)).toArray(Contact[]::new);
    }

    public boolean removeContact(String name){
        try{
            contacts.remove(getContactForName(name));
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public static boolean saveAsTextFile(PhoneBook pb, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(pb);
            oos.close();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
    public static PhoneBook loadFromTextFile(String file) throws InvalidFormatException {
        PhoneBook pb = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            pb = (PhoneBook) ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new InvalidFormatException();
        }

        return pb;
    }
    public int numberOfContacts() {
        return contacts.size();
    }

    public Contact[] getContactsForNumber(String number) {
        return contacts.stream().filter(i -> i.hasNumber(number)).toArray(Contact[]::new);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(contacts.toArray()).sorted().forEach(i -> sb.append(i).append("\n"));

        return sb.toString();
    }


}
public class PhonebookTester {

    public static void main(String[] args) throws Exception {
        Scanner jin = new Scanner(System.in);
        String line = jin.nextLine();
        switch( line ) {
            case "test_contact":
                testContact(jin);
                break;
            case "test_phonebook_exceptions":
                testPhonebookExceptions(jin);
                break;
            case "test_usage":
                testUsage(jin);
                break;
        }
    }

    private static void testFile(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() )
            phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
        String text_file = "phonebook.txt";
        PhoneBook.saveAsTextFile(phonebook,text_file);
        PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
        else System.out.println("Your file saving and loading works great. Good job!");
    }

    private static void testUsage(Scanner jin) throws Exception {
        PhoneBook phonebook = new PhoneBook();
        while ( jin.hasNextLine() ) {
            String command = jin.nextLine();
            switch ( command ) {
                case "add":
                    phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
                    break;
                case "remove":
                    phonebook.removeContact(jin.nextLine());
                    break;
                case "print":
                    System.out.println(phonebook.numberOfContacts());
                    System.out.println(Arrays.toString(phonebook.getContacts()));
                    System.out.println(phonebook.toString());
                    break;
                case "get_name":
                    System.out.println(phonebook.getContactForName(jin.nextLine()));
                    break;
                case "get_number":
                    System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
                    break;
            }
        }
    }

    private static void testPhonebookExceptions(Scanner jin) {
        PhoneBook phonebook = new PhoneBook();
        boolean exception_thrown = false;
        try {
            while ( jin.hasNextLine() ) {
                phonebook.addContact(new Contact(jin.nextLine()));
            }
        }
        catch ( InvalidNameException e ) {
            System.out.println(e.name);
            exception_thrown = true;
        }
        catch ( Exception e ) {}
        if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
    }

    private static void testContact(Scanner jin) throws Exception {
        boolean exception_thrown = true;
        String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
        for ( String name : names_to_test ) {
            try {
                new Contact(name);
                exception_thrown = false;
            } catch (InvalidNameException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
        }
        String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
        for ( String number : numbers_to_test ) {
            try {
                new Contact("Andrej",number);
                exception_thrown = false;
            } catch (InvalidNumberException e) {
                exception_thrown = true;
            }
            if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
        }
        String nums[] = new String[10];
        for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
        try {
            new Contact("Andrej",nums);
            exception_thrown = false;
        } catch (MaximumSizeExceddedException e) {
            exception_thrown = true;
        }
        if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
        Random rnd = new Random(5);
        Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
        System.out.println(contact.getName());
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
        contact.addNumber(getRandomLegitNumber(rnd));
        System.out.println(Arrays.toString(contact.getNumbers()));
        System.out.println(contact.toString());
    }

    static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
    static Random rnd = new Random();

    private static String getRandomLegitNumber() {
        return getRandomLegitNumber(rnd);
    }

    private static String getRandomLegitNumber(Random rnd) {
        StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
        for ( int i = 3 ; i < 9 ; ++i )
            sb.append(rnd.nextInt(10));
        return sb.toString();
    }


}
