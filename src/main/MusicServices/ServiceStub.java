package main.MusicServices;

import main.Song.Song;
import main.Song.SongBuilder;

import java.util.ArrayList;

public class ServiceStub implements ServiceInterface {
    /*

    Used for tests
     */

    @Override
    public void connect() {

    }

    @Override
    public boolean authenticate() {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public ArrayList<Song> getSongs(String query) {
        SongBuilder builder = new SongBuilder();
        Song s = builder.title("title").addMusicUri("http://www.freesfx.co.uk/rx2/mp3s/4/16412_1460641277.mp3").build();
        ArrayList<Song> results = new ArrayList<>();
        results.add(s);
        return results;
    }
}
