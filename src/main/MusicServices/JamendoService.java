package main.MusicServices;

import main.Song.Song;

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
        //songs.add(new Song("People are Strange"));
        //songs.add(new Song("Sweet Child O' Mine"));
        //songs.add(new Song("Let it Be"));

        //SongBuilder b = new SongBuilder();

        /*
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

                */
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
        String limit = "100";

        URLConnection connection = null;
        InputStream response = null;

        String temp = null;
        String streamURL = null;
        ArrayList<Song> results = new ArrayList<Song>();

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

            /*
            event = parser.next(); // START_OBJECT
            event = parser.next();       // KEY_NAME
            event = parser.next();       // VALUE_STRING
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next();
            event = parser.next(); temp = parser.getString();
            event = parser.next(); temp = parser.getString(); // first track id
            event = parser.next(); temp = parser.getString();
            */

        } catch (IOException e) {

            e.printStackTrace();
        }

        parseResponse(response);

        return songs;
    }


    private void parseResponse(InputStream response) {

        String temp = null;
        JsonParser parser = Json.createParser(response);
        JsonParser.Event event;

        event = parser.next(); // START_OBJECT
        temp = parser.getString();

        // to do...

    }



}
