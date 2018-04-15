package main.MusicServices;

import main.Song.Song;

import java.util.ArrayList;

public class ServiceFacade {

    private JamendoService jamendoService  = new JamendoService();
    private DeezerService deezerService = new DeezerService();
    private SpotifyService spotifyService = new SpotifyService();

    public ArrayList<Song> getAllSongs(String query, boolean services[]) {


        ArrayList<Song> results = new ArrayList<>();

        if (services[0])
            results.addAll(jamendoService.getSongs(query));
        if (services[1])
            results.addAll(deezerService.getSongs(query));
        if (services[2])
            results.addAll(spotifyService.getSongs(query));

        return results;

    }

    public void connectServices() {

        spotifyService.connect();

    }



}
