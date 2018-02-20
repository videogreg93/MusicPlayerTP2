package main;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.MusicServices.ExampleService;
import main.Song.Song;

import java.util.ArrayList;

public class Controller {

    // JFX fields
    public JFXTextField searchBarTextField;
    public Button searchButton;

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
        ArrayList<Song> results = exampleService.getSongs(query);
        if (results != null)  {
            if (results.isEmpty())
                System.out.println("No results found");
            for (Song song : results) {
                System.out.println(song);
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
