package main.MusicServices;

import main.Song.Song;

import java.util.ArrayList;

public interface ServiceInterface {
    void connect();
    boolean authenticate();
    void disconnect();
    ArrayList<Song> getSongs(String query);
}
