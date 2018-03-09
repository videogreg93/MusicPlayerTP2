package main.Song;


import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.util.HashMap;

public class Song {
    Image image;
    Media music;
    HashMap<String,String> metadata = new HashMap<String, String>();

    public Song() {
        metadata.put("title","untitled");
        image = new Image("/images/default.png", true);
    }

    public Song(String title) {
        metadata.put("title", title);
        image = new Image("/images/default.png", true);
    }

    // Setters

    public void setMetadataValue(String key, String value) {
        metadata.put(key,value);
    }

    public void setTitle(String title) {
        setMetadataValue("title", title);
    }

    public void setImage(String url) {
        image = new Image(url, true);
    }

    public void setMusic(String musicURI) {
        try {
            this.music = new Media(getClass().getResource(musicURI).toURI().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMusicFullPath(String uri) {
        this.music = new Media(uri);
    }

    // Getters

    public String getTitle() {
        return getMetadataValue("title");
    }

    public Image getImage() {
        return image;
    }

    public Media getMusic() {
        return music;
    }

    public String getMetadataValue(String key) {
        return metadata.getOrDefault(key, "no " + key + " found");
    }

    public String getMetadataValue(String key, String defaultValue) {
        return metadata.getOrDefault(key, defaultValue);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String key : metadata.keySet()) {
            builder.append(key);
            builder.append(": ");
            builder.append(metadata.get(key));
            builder.append("\n");
        }
        return builder.toString();
    }


    public HashMap<String, String> getMetadata() {
        return metadata;
    }
}
