package main.MusicServices;

import main.Song.Song;
import main.Song.SongBuilder;

import java.util.ArrayList;

public class ExampleService implements ServiceInterface {
    ArrayList<Song> songs;
    boolean isConnected = false;

    public ExampleService() {
        songs = new ArrayList<Song>();
        //songs.add(new Song("People are Strange"));
        //songs.add(new Song("Sweet Child O' Mine"));
        //songs.add(new Song("Let it Be"));
        SongBuilder b = new SongBuilder();
        songs.add(
                b.title("People are strange")
                        .imageUrl("/images/sample1.jpg")
                        .addMusicUri("/songs/song5.wav")
                        .build());
        songs.add(b
                .title("Sweet Child O' Mine")
                .imageUrl("/images/sample2.jpg")
                .addMusicUri("/songs/song2.mp3")
                .build());
        songs.add(b
                .title("Let it Be")
                .imageUrl("/images/sample3.jpg")
                .addMusicUri("/songs/song3.mp3")
                .build());

    }
    @Override
    public void connect() {
        System.out.println("Connected!");
        isConnected = true;
    }

    @Override
    public boolean authenticate() {
        System.out.println("Authenticated!");
        return true;
    }

    @Override
    public void disconnect() {
        System.out.println("Disconnected!");
        isConnected = false;
    }

    @Override
    public ArrayList<Song> getSongs(String query) {
        // Example fetching of songs selon query
        if (!isConnected) {
            System.out.println("Must be connected");
            return null;
        }
        query = query.toLowerCase();
        ArrayList<Song> results = new ArrayList<Song>();
        for (Song song : songs) {
            if (song.getTitle().toLowerCase().contains(query))
                results.add(song);
        }
        return results;
    }
}
