package main.Song;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Controller;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

public class SongBuilderTest extends ApplicationTest {
    Parent root;
    Controller controller;
    SongBuilder songBuilder;


    @Override public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample.fxml"));;
            root = fxmlLoader.load();
            controller = (Controller)fxmlLoader.getController();
            stage.setTitle("Music Player");
            stage.setScene(new Scene(root));
            stage.show();
            songBuilder = new SongBuilder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done setup");
    }

    @Test
    public void title() throws Exception {
        Song s = songBuilder.title("title").build();
        assertTrue(s.getTitle().equals("title"));
    }

    @Test
    public void imageUrl() throws Exception {
        Song s = songBuilder.imageUrl("https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg").build();
        assertTrue(s.getImage().impl_getUrl().equals("https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg"));
    }

    @Test
    public void addMetadata() throws Exception {
        Song s = songBuilder.addMetadata("test1", "test2").build();
        assertTrue(s.getMetadataValue("test1").equals("test2"));
    }

    @Test
    public void addMusicUri() throws Exception {
        Song s = songBuilder.addMusicUri("http://www.freesfx.co.uk/rx2/mp3s/4/16412_1460641277.mp3").build();
        assertTrue(s.getMusic().getSource().equals("http://www.freesfx.co.uk/rx2/mp3s/4/16412_1460641277.mp3"));
    }


    @Test
    public void build() throws Exception {
        Song s = songBuilder.title("title").build();
        assertTrue(s != null);
    }

    @Test
    public void destroy() throws Exception {
        Song s = songBuilder.title("title").build();
        assertTrue(s != null);
        songBuilder.destroy();
        Song s2 = songBuilder.build();
        assertTrue(s != s2);
    }

}