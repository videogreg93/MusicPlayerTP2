package main.MusicServices;

import main.Song.Song;

import java.util.ArrayList;

public class ExampleService implements ServiceInterface {
    ArrayList<Song> songs;
    boolean isConnected = false;

    public ExampleService() {
        songs = new ArrayList<Song>();
        songs.add(new Song("People are Strange"));
        songs.add(new Song("Sweet Child O' Mine"));
        songs.add(new Song("Let it Be"));
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
