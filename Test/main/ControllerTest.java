package main;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MusicServices.ServiceStub;
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
    Controller controller;


    @Override public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));;
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
    public void searchButtonPressed() {
        TextField searchBar = (TextField) root.lookup("#searchBarTextField");
        searchBar.setText("dance");
        ((CheckBox)root.lookup("#jamendoCheckbox")).setSelected(true);
        clickOn("#searchButton");
        JFXListView list = ((JFXListView)root.lookup("#playlistListView"));
        assertFalse(list.getItems().isEmpty());
    }

    @Test
    public void onStopPressed() throws Exception {
    }

    @Test(timeout=10000)
    public void onPlayPressed() throws Exception {
        ServiceStub stub = new ServiceStub();
        controller.addResultsToListView(stub.getSongs(""));
        sleep(300);
        JFXListView list = ((JFXListView)root.lookup("#playlistListView"));
        Button playButton = (Button) root.lookup(".play");
        sleep(300);
        clickOn(playButton);
        sleep(1000);
        assertTrue(SoundManager.isPlaying());
        Button play = (Button) root.lookup("#playButton");
        assertTrue(play.getText().equals("||"));
    }

}