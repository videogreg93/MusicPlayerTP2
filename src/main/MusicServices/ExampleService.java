package main.MusicServices;

import java.util.ArrayList;

public class ExampleService implements ServiceInterface {
    ArrayList<String> songs;
    boolean isConnected = false;

    public ExampleService() {
        songs = new ArrayList<String>();
        songs.add("People are Strange");
        songs.add("Sweet Child O' Mine");
        songs.add("Let it Be");
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
    public ArrayList<String> getSongs(String query) {
        // Example fetching of songs selon query
        if (!isConnected) {
            System.out.println("Must be connected");
            return null;
        }
        query = query.toLowerCase();
        ArrayList<String> results = new ArrayList<String>();
        for (String song : songs) {
            if (song.toLowerCase().contains(query))
                results.add(song);
        }
        return results;
    }
}
