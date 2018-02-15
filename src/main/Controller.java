package main;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;

public class Controller {

    public JFXTextField searchBarTextField;
    public Button searchButton;

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
    }


    /**
     * Method handling when the search button is pressed
     * @param actionEvent the event
     */
    public void searchButtonPressed(ActionEvent actionEvent) {
        // Get search query
        String query = searchBarTextField.getText();
        System.out.println(query);
    }

    public void searchFieldTextChange(InputMethodEvent inputMethodEvent) {
        searchButton.setDisable(searchBarTextField.getText().isEmpty());
    }
}
