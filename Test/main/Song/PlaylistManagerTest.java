package main.Song;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.Controller;
import main.MusicServices.ServiceStub;
import main.SoundManager;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Gregory on 4/12/2018.
 */
public class PlaylistManagerTest extends ApplicationTest {
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
    public void createNewPlaylistAndRefresh() throws Exception {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PlaylistManager.createNewPlaylist();
                type(KeyCode.K, 5);
                sleep(1000);
                press(KeyCode.ENTER);
                assertTrue(PlaylistManager.allPlaylists.size() == 1);
                Playlist p = PlaylistManager.allPlaylists.get(0);
                assertTrue(p.getName().equals("kkkkk"));
                assertTrue(PlaylistManager.playlistListView.getItems().size() == 1);
                PlaylistManager.deletePlaylist(p);
            }
        });


    }

    @Test
    public void playPlaylist() throws Exception {
        // make sure a playlist exits
        //createNewPlaylistAndRefresh();
        type(KeyCode.ENTER);
        Playlist p = PlaylistManager.allPlaylists.get(0);
        PlaylistManager.playPlaylist(p);
        sleep(1000);
        assertTrue(SoundManager.isPlaying());
    }

    @Test
    public void addSongToPlaylist() throws Exception {
        Playlist p = PlaylistManager.allPlaylists.get(0);
        int soungCount = p.getAllSongs().size();
        ServiceStub serviceStub = new ServiceStub();
        controller.addResultsToListView(serviceStub.getSongs(""));
        sleep(300);
        Button addToPlaylistButton = (Button) root.lookup(".addToPlaylist");
        clickOn(addToPlaylistButton);
        press(KeyCode.ENTER);
        assertTrue(soungCount + 1 == p.allSongs.size());
    }

    @Test
    public void deletePlaylist() throws Exception {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PlaylistManager.createNewPlaylist();

                sleep(150);

                type(KeyCode.ENTER);

                int playlistAmount = PlaylistManager.allPlaylists.size();
                Playlist p = PlaylistManager.allPlaylists.get(0);
                PlaylistManager.deletePlaylist(p);

                sleep(150);
                type(KeyCode.ENTER);

                assertTrue(playlistAmount -1==PlaylistManager.allPlaylists.size());

            }
        });

    }

}