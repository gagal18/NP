//package labs.labs_7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class NoSuchRoomException extends Exception {
    NoSuchRoomException(String roomName) {
        super(roomName + " does not exist.");
    }
}

class NoSuchUserException extends Exception {
    NoSuchUserException(String userName) {
        super(userName + " does not exist.");
    }
}

class ChatRoom implements Comparable<ChatRoom> {
    private String roomName;
    private Set<String> users;

    public ChatRoom(String roomName) {
        this.roomName = roomName;
        this.users = new TreeSet<>();
    }

    public void addUser(String username) {
        this.users.add(username);
    }

    public void removeUser(String username) {
        this.users.remove(username);
    }

    public boolean hasUser(String username) {
        return this.users.contains(username);
    }

    public int numUsers() {
        return this.users.size();
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.roomName).append("\n");
        if (this.users.isEmpty()) {
            sb.append("EMPTY\n");
        } else {
            this.users.forEach(user -> sb.append(user).append("\n"));
        }
        return sb.toString();
    }

    @Override
    public int compareTo(ChatRoom o) {
        return Integer.compare(this.users.size(), o.users.size());
    }
}

class ChatSystem {
    private Map<String, ChatRoom> rooms;
    private Set<String> registeredUsers;

    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.registeredUsers = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void register(String userName) {
        registeredUsers.add(userName);
        rooms.values().stream()
                .min(Comparator.comparingInt(ChatRoom::numUsers)
                .thenComparing(ChatRoom::getRoomName))
                .ifPresent(x -> x.addUser(userName));
    }

    public void registerAndJoin(String userName, String roomName) {
        registeredUsers.add(userName);
        try {
            ChatRoom toAddRoom = getRoom(roomName);
            toAddRoom.addUser(userName);
        } catch (NoSuchRoomException e) {
            System.out.println(e.getMessage());
        }
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        ChatRoom room = rooms.get(roomName);
        if (room != null) {
            return room;
        } else {
            throw new NoSuchRoomException(roomName);
        }
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }


    public void joinRoom(String username, String roomname) {
        try {
            isRegistered(username);
            try {
                ChatRoom room = getRoom(roomname);
                room.addUser(username);
            } catch (NoSuchRoomException e) {
                System.out.println(e.getMessage());
            }
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        }
    }

    public void leaveRoom(String username, String roomname) {
        try {
            isRegistered(username);
            try {
                ChatRoom room = getRoom(roomname);
                room.removeUser(username);
            } catch (NoSuchRoomException e) {
                System.out.println(e.getMessage());
            }
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        }
    }

    public void followFriend(String username, String friendUsername) throws NoSuchUserException {
        if (!registeredUsers.contains(friendUsername)) {
            throw new NoSuchUserException(friendUsername);
        }
        if (!registeredUsers.contains(username)) {
            throw new NoSuchUserException(username);
        }
        for (ChatRoom room : rooms.values()) {
            if (room.hasUser(friendUsername)) {
                room.addUser(username);
            }
        }
    }

    public void isRegistered(String username) throws NoSuchUserException {
        if (!registeredUsers.contains(username)) {
            throw new NoSuchUserException(username);
        }
    }
}

public class ChatSystemTest {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,(Object[])params);
                    }
                }
            }
        }
    }

}

