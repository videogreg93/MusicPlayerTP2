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

public class DeezerService implements ServiceInterface {

    ArrayList<Song> songs;
    boolean isConnected = false;
    //static final String client_id = "687772c1";

    public DeezerService() {

        songs = new ArrayList<Song>();

        //SongBuilder b = new SongBuilder();

        /*
        songs.add(
                b.title("People are strange")
                        .imageUrl("/images/sample1.jpg")
                        .addMusicUri("/songs/song5.wav")
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

        String call = "https://api.deezer.com/search";
        String callArguments = null;
        String fullCall = null;
        String format = "JSONP";
        String limit = "30";

        URLConnection connection = null;
        InputStream response = null;

        songs.clear();

        try {
            callArguments = String.format("q=track:%s&limit=%s&format=%s", URLEncoder.encode(query,"UTF-8"),
                    URLEncoder.encode(limit,"UTF-8"),URLEncoder.encode(format,"UTF-8"));

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

        //Build a song for each result
        while (parser.hasNext()){

            //Get the title
            while (!temp.equals("title") && parser.hasNext()){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }

            if(!parser.hasNext())
                break;
            parser.next();
            title = parser.getString();

            //Get the file uri
            while (!temp.equals("preview")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            songUri = parser.getString();

            //Get the artist name
            while (!temp.equals("name")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            artist = parser.getString();

            //Get the album cover url
            while (!temp.equals("cover_medium")){
                event = parser.next();
                if(event == JsonParser.Event.KEY_NAME)
                    temp = parser.getString();
            }
            parser.next();
            imageUrl = parser.getString();

            //Build the song and add it
            songs.add(b.title(title).imageUrl(imageUrl).addMetadata("Artist", artist).addMusicUri(songUri).build());

        }

    }

}
