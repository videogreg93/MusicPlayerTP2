package main;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import main.MusicServices.ExampleService;
import main.Song.PlaylistManager;
import main.Song.Song;

import java.util.ArrayList;

public class Controller {

    // JFX fields
    public JFXTextField searchBarTextField;
    public Button searchButton;
    public JFXListView songResultsList;
    public TabPane tabPane;
    public JFXListView playlistListView;

    // Services
    ExampleService exampleService = new ExampleService();


    @FXML
    public void initialize() {
        // Set listener of text change
        searchBarTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                searchButton.setDisable(searchBarTextField.getText().isEmpty());
            }
        });


        PlaylistManager.init(playlistListView);

        // Connect Services
        exampleService.connect();
        exampleService.authenticate();

    }


    /**
     * Method handling when the search button is pressed
     * @param actionEvent the event
     */
    public void searchButtonPressed(ActionEvent actionEvent) {
        // Get search query
        String query = searchBarTextField.getText();
        searchBarTextField.clear();
        ArrayList<Song> results = exampleService.getSongs(query);
        songResultsList.getItems().clear();
        if (results != null)  {
            if (results.isEmpty()) {
                Utils.ShowError("No songs found");
            }

            for (Song song : results) {
                // Create an hbox with an image, song name, artist name, etc.
                HBox hbox = new HBox(8); // spacing = 8
                // Image
                Image image = song.getImage();
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                // Seperator
                Region seperator = new Region();
                HBox.setHgrow(seperator, Priority.ALWAYS);
                // Add to playlist Button
                Button addToPlaylist = new Button("Add to playlist");
                addToPlaylist.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        PlaylistManager.addSongToPlaylist(song);
                    }
                });
                hbox.getChildren().addAll(imageView, seperator, new Label(song.getTitle()), addToPlaylist);
                songResultsList.getItems().add(hbox);
            }
        }
    }

    /**
     * Method handling when enter is pressed in the search bar
     * @param actionEvent the event
     */
    public void onSearchBarEnterPressed(ActionEvent actionEvent) {
        // Same thing as search button pressed, as long as its not disabled
        if (!searchButton.isDisabled())
            searchButtonPressed(actionEvent);
    }
}
