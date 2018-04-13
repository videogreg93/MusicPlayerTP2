package main.MusicServices;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Controller;
import main.Song.Song;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class serviceTests extends ApplicationTest {
    Parent root;
    Controller controller;


    @Override public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample.fxml"));;
            root = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
            stage.setTitle("Music Player");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done setup");
    }

    @Test
    public void JamendoGetSongs() throws Exception {
        JamendoService jamendoService = new JamendoService();
        ArrayList<Song> results = jamendoService.getSongs("The Beetles");
        assertFalse(results.size() == 0);
        Song s = results.get(0);
        assertTrue(s.getTitle().equals("We Got The Love"));
    }

    @Test
    public void DeezerGetSongs() throws Exception {
        DeezerService deezerService = new DeezerService();
        ArrayList<Song> results = deezerService.getSongs("The Beetles");
        assertFalse(results.size() == 0);
        Song s = results.get(0);
        assertTrue(s.getTitle().equals("Beatles (George Harrison) (Original Album Track)"));
    }

    @Test
    public void SpotifyGetSongs() throws Exception {
        SpotifyService spotifyService = new SpotifyService();
        ArrayList<Song> results = spotifyService.getSongs("song");
        assertFalse(results.size() == 0);
        Song s = results.get(0);
        assertTrue(s.getTitle().equals("Your Song"));
    }

}