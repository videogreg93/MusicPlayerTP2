package main;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.MusicServices.ExampleService;
import main.Song.Song;

import java.util.ArrayList;

public class Controller {

    // JFX fields
    public JFXTextField searchBarTextField;
    public Button searchButton;
    public JFXListView songResultsList;

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
            // TODO should be a dialog
            if (results.isEmpty())
                songResultsList.getItems().add(new Label("No results found"));
            for (Song song : results) {
                songResultsList.getItems().add(new Label(song.getTitle()));
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
