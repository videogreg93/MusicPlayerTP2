package main.MusicServices;

import main.Song.Song;
import main.Song.SongBuilder;

import java.nio.file.Paths;
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
        Song s = builder.title("title").addMusicUriFullPath(Paths
                .get("/Users/gregoryfournier/Documents/School 2018/Winter/LOG8430/TP3/MusicPlayerTP2-master/src/songs/song2.mp3").toUri().toString()).build();
        ArrayList<Song> results = new ArrayList<>();
        results.add(s);
        return results;
    }
}
