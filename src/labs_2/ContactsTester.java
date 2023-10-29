package labs_2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


class getTypeOfContacts{
    static Contact[] getType(ArrayList<Contact> contacts, String str) {
        ArrayList<Contact> clone = new ArrayList<>();
        for (Contact c: contacts) {
            if(c.getType().equals(str)){
                clone.add(c);
            }
        }
        int n = clone.size();
        Contact[] contactsRet = new Contact[n];
        for (int i = 0; i < n; i++) {
            contactsRet[i] = clone.get(i);
        }
        return contactsRet;
    }
}

class Date{
    int y;
    int m;
    int d;

    public Date(int y, int m, int d) {
        this.y = y;
        this.m = m;
        this.d = d;
    }
    //Expected YYYY-MM-DD
    public Date(String date){
        String[] d = date.split("-");
        this.y = Integer.parseInt(d[0]);
        this.m = Integer.parseInt(d[1]);
        this.d = Integer.parseInt(d[2]);
    }
    public boolean isAfter(Date oth) {
        if (y > oth.y) return true;
        if (y < oth.y) return false;
        if (m > oth.m) return true;
        if (m < oth.m) return false;
        if (d > oth.d) return true;
        return false;
    }
}
enum Operator {
    VIP, ONE, TMOBILE
}
abstract class Contact{
    private String created_at;

    public Contact(String created_at) {
        this.created_at = created_at;
    }

    public boolean isNewerThan(Contact c){
        Date d1 = new Date(created_at);
        Date d2 = new Date(c.created_at);
        return d1.isAfter(d2);
    };
    public abstract String getType();
}

class EmailContact extends Contact{
    private String email;

    public EmailContact(String created_at, String email) {
        super(created_at);
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String toString() {
        return "\"" + email + "\"";
    }
}

class PhoneContact extends Contact{
    private String phone;
    private Operator operator;

    public PhoneContact(String created_at, String phone) {
        super(created_at);
        this.phone = phone;
        int op = Integer.parseInt(String.valueOf(phone.charAt(2)));
        switch (op){
            case 0:
            case 1:
            case 2:
                this.operator = Operator.TMOBILE;
                break;
            case 5:
            case 6:
                this.operator = Operator.ONE;
                break;
            case 7:
            case 8:
                this.operator = Operator.VIP;
                break;
        }
    }


    @Override
    public String getType() {
        return "Phone";
    }

    @Override
    public String toString() {
        return "\"" + phone + "\"";
    }

    public String getPhone() {
        return phone;
    }

    public Operator getOperator() {
        return operator;
    }
}

class Student{
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
    }

    public String getCity() {
        return city;
    }

    public long getIndex() {
        return index;
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "{\"ime\":\"" +
                firstName +
                "\", \"prezime\":\"" +
                lastName +
                "\", \"vozrast\":" +
                age +
                ", \"grad\":\"" +
                city +
                "\", \"indeks\":" +
                index +
                ", \"telefonskiKontakti\":" +
                Arrays.toString(getPhoneContacts()) +
                ", \"emailKontakti\":" +
                Arrays.toString(getEmailContacts()) +
                "}";
    }

    public void addEmailContact(String date, String email){
        Contact emC = new EmailContact(date, email);
        contacts.add(emC);
    }

    public void addPhoneContact(String date, String phone){
        Contact phC = new PhoneContact(date, phone);
        contacts.add(phC);
    }

    public Contact[] getEmailContacts(){
        return getTypeOfContacts.getType(contacts, "Email");
    }

    public Contact[] getPhoneContacts(){
        return getTypeOfContacts.getType(contacts, "Phone");
    }

    public Contact getLatestContact(){
        Contact latest = contacts.get(0);
        for (Contact c: contacts) {
            if(c.isNewerThan(latest)){
                latest = c;
            }
        }
        return latest;
    }

    public int getNumberOfContacts(){
        return contacts.size();
    }
}

class Faculty{
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = Arrays.copyOf(students, students.length);
    }

    public int countStudentsFromCity(String cityName){
        int count = 0;
        for (Student s: students) {
            if(s.getCity().equals(cityName)){
                count++;
            }
        }
        return count;
    }
    public Student getStudent(long index){
        for (Student s: students) {
            if(s.getIndex() == index){
                return s;
            }
        }
        return null;
    }

    public Student getStudentWithMostContacts(){
        Student maxContacts = students[0];
        for (Student s: students) {
            if(s.getNumberOfContacts() > maxContacts.getNumberOfContacts() || (s.getNumberOfContacts() == maxContacts.getNumberOfContacts() && s.getIndex() >= maxContacts.getIndex())){
                maxContacts = s;
            }
        }
        return maxContacts;
    }

    public double getAverageNumberOfContacts(){
        int totalContacts = 0;
        for (Student s: students) {
            totalContacts += s.getNumberOfContacts();
        }
        return totalContacts/(double)students.length;
    }

    @Override
    public String toString() {
        return "{\"fakultet\":\"" +
                name +
                "\", \"studenti\":" +
                Arrays.toString(students) +
                "}";
    }
}
public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
