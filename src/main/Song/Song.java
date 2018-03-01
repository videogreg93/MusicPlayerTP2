package main.Song;


import javafx.scene.image.Image;

import java.util.HashMap;

public class Song {
    Image image;
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

    // Getters

    public String getTitle() {
        return getMetadataValue("title");
    }

    public Image getImage() {
        return image;
    }

    public String getMetadataValue(String key) {
        return metadata.getOrDefault(key, "no " + key + " found");
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
}
