package main.MusicServices;

import java.util.ArrayList;

public interface ServiceInterface {
    void connect();
    boolean authenticate();
    void disconnect();
    // TODO replace <String> with <Song>
    ArrayList<String> getSongs(String query);
}
