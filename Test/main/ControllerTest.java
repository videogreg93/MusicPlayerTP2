package main;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;


/**
 * Created by Gregory on 4/12/2018.
 */
public class ControllerTest extends ApplicationTest {
    Parent root;


    @Override public void start(Stage stage) {
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            stage.setTitle("Music Player");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done setup");
    }

    @Test
    public void searchButtonPressed() {
        TextField searchBar = (TextField) root.lookup("#searchBarTextField");
        searchBar.setText("dance");
        ((CheckBox)root.lookup("#jamendoCheckbox")).setSelected(true);
        clickOn("#searchButton");
       // wait(3000);
        JFXListView list = ((JFXListView)root.lookup("#playlistListView"));
        assertFalse(list.getItems().isEmpty());
    }

    @Test
    public void onStopPressed() throws Exception {
    }

    @Test(timeout=10000)
    public void onPlayPressed() throws Exception {
        TextField searchBar = (TextField) root.lookup("#searchBarTextField");
        searchBar.setText("dance");
        ((CheckBox)root.lookup("#jamendoCheckbox")).setSelected(true);
        clickOn("#searchButton");
        JFXListView list = ((JFXListView)root.lookup("#playlistListView"));
        Button playButton = (Button) root.lookup(".play");
        sleep(2000);
        clickOn(playButton);
        sleep(2000);
        assertTrue(SoundManager.isPlaying());
        Button play = (Button) root.lookup("#playButton");
        assertTrue(play.getText().equals("||"));
        clickOn(play);
        assertTrue(play.getText().equals(">"));
        assertFalse(SoundManager.isPlaying());


    }

}