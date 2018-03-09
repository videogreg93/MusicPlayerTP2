package main.Song;

import java.util.ArrayList;

public class Playlist {
    String name;
    ArrayList<Song> allSongs;

    public Playlist(String name) {
        this.name = name;
        allSongs = new ArrayList<>();
    }


    /**
     * Append song to end of playlist
     * @param song
     */
    public void addSong(Song song) {
        // TODO check if song isnt already present
        allSongs.add(song);
        PlaylistManager.refreshPlaylistView();
    }

    /**
     *
     * @param song
     * @param atPosition
     */
    public void addSong(Song song, int atPosition) {
        if (atPosition > allSongs.size()) {
            System.out.println("Error: Index out of bounds");
        } else {
            allSongs.add(atPosition, song);
            PlaylistManager.refreshPlaylistView();
        }

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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
