package main.Song;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
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


    @Override public void start(Stage stage) {
        try {
            root = FXMLLoader.load(getClass().getResource("../sample.fxml"));
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

        TextField searchBar = (TextField) root.lookup("#searchBarTextField");
        searchBar.setText("dance");
        ((CheckBox)root.lookup("#jamendoCheckbox")).setSelected(true);
        clickOn("#searchButton");
        JFXListView list = ((JFXListView)root.lookup("#playlistListView"));
        Button addToPlaylistButton = (Button) root.lookup(".addToPlaylist");
        sleep(1500);
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
                System.out.println("DELETE");
            }
        });

    }

}