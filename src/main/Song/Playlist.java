package main.Song;

import java.util.ArrayList;

public class Playlist {
    ArrayList<Song> allSongs;


    /**
     * Append song to end of playlist
     * @param song
     */
    public void addSong(Song song) {
        allSongs.add(song);
    }

    /**
     *
     * @param song
     * @param atPosition
     */
    public void addSong(Song song, int atPosition) {
        if (atPosition > allSongs.size()) {
            System.out.println("Error: Index out of bounds");
        }
        allSongs.add(atPosition, song);
    }

    /**
     *
     * @param atPosition index of song to get
     * @return Song at position atPosition
     */
    public Song getSong(int atPosition) {
        if (atPosition > allSongs.size()) {
            System.out.println("Error: Index out of bounds");
        }
        return allSongs.get(atPosition);
    }

    // Getters and setters

    public ArrayList<Song> getAllSongs() {
        return allSongs;
    }

    public void setAllSongs(ArrayList<Song> allSongs) {
        this.allSongs = allSongs;
    }

}
