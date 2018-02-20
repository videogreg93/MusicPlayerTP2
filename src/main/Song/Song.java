package main.Song;


import java.util.HashMap;

public class Song {
    HashMap<String,String> metadata = new HashMap<String, String>();

    public Song() {metadata.put("title","untitled");}

    public Song(String title) {
        metadata.put("title", title);
    }

    public String getTitle() {
        return getMetadataValue("title");
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
