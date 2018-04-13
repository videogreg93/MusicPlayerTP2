package main.MusicServices;

import main.Song.Song;
import main.Song.SongBuilder;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class JamendoService implements ServiceInterface {

    ArrayList<Song> songs;
    boolean isConnected = false;
    static final String client_id = "687772c1";

    public JamendoService() {

        songs = new ArrayList<Song>();
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

        String call =  "https://api.jamendo.com/v3.0/tracks/";
        String callArguments = null;
        String fullCall = null;
        String format = "jsonpretty";
        String limit = "30";

        URLConnection connection = null;
        InputStream response = null;

        songs.clear();

        try {
            callArguments = String.format("client_id=%s&format=%s&limit=%s&namesearch=%s",
                    URLEncoder.encode(client_id,"UTF-8"), URLEncoder.encode(format,"UTF-8"),
                    URLEncoder.encode(limit,"UTF-8"), URLEncoder.encode(query,"UTF-8"));

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        try {

            fullCall = call + "?" + callArguments;
            connection = new URL(fullCall).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            response = connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        parseResponse(response);
        return songs;
    }


    private void parseResponse(InputStream response) {

        String temp = "";
        String title = "";
        String imageUrl = "";
        String songUri = "";
        String artist = "";

        JsonParser parser = Json.createParser(response);
        JsonParser.Event event;
        SongBuilder b = new SongBuilder();

        int count = 0;
        int i = 0;

        //Get the results count
        while (!temp.equals("results_count")){
            event = parser.next();
            if(event == JsonParser.Event.KEY_NAME)
                temp = parser.getString();
        }
        parser.next();
        count = parser.getInt();

        if(count == 0)
            return;

        //Build a song for each result
        while (i < count){

            //Get the title
            while (!temp.equals("name")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            title = parser.getString();

            //Get the artist
            while (!temp.equals("artist_name")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            artist = parser.getString();

            //Get the album cover url
            while (!temp.equals("album_image")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            imageUrl = parser.getString();

            //Get the file uri
            while (!temp.equals("audio")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            songUri = parser.getString();

            //Build the song and add it
            songs.add(b.title(title).imageUrl(imageUrl).addMetadata("artist", artist).addMusicUri(songUri).build());

            //Move on to next song
            i++;
        }


    }

}
