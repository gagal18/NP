//package labs.labs_8;


import java.util.ArrayList;
import java.util.List;

class Song{
    String songName;
    String artist;

    public Song(String songName, String artist) {
        this.songName = songName;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{title=" + songName+
                ", artist=" + artist + '}';
    }
}

class MP3Player {
    private List<Song> listSong;
    private int currentSongIndex;
    private PlayerState playerState;

    public MP3Player(List<Song> listSong) {
        this.listSong = listSong;
        this.currentSongIndex = 0;
        this.playerState = PlayerState.STOPPED;
    }
    void printCurrentSong() {
        System.out.println(listSong.get(currentSongIndex).toString());
    }
    void pressPlay() {
        switch (playerState) {
            case STOPPED:
                System.out.println("Song " + currentSongIndex + " is playing");
                playerState = PlayerState.PLAYING;
                break;
            case PAUSED:
                System.out.println("Resuming song " + currentSongIndex);
                playerState = PlayerState.PLAYING;
                break;
            case PLAYING:
                System.out.println("Song is already playing");
                break;
        }
    }

    void pressStop() {
        switch (playerState) {
            case PLAYING:
                System.out.println("Song " + currentSongIndex + " is stopped");
                playerState = PlayerState.STOPPED;
                break;
            case PAUSED:
                System.out.println("Song " + currentSongIndex + " is stopped");
                playerState = PlayerState.STOPPED;
                break;
            case STOPPED:
                System.out.println("Songs are already stopped");
                break;
        }
    }

    void pressFWD() {
        System.out.println("Forward...");
        currentSongIndex = (currentSongIndex + 1) % listSong.size();
        if (playerState == PlayerState.PLAYING)
            System.out.println("Song " + currentSongIndex + " is playing");
    }

    void pressREW() {
        System.out.println("Rewind...");
        currentSongIndex = (currentSongIndex - 1 + listSong.size()) % listSong.size();
        if (playerState == PlayerState.PLAYING)
            System.out.println("Song " + currentSongIndex + " is playing");
    }

    @Override
    public String toString() {
        return "MP3Player{" +
                "currentSong=" + currentSongIndex +
                ", playerState=" + playerState +
                ", songList=" + listSong +
                '}';
    }
}

enum PlayerState {
    PLAYING,
    PAUSED,
    STOPPED
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde

