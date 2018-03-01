package main;

import javafx.scene.control.Alert;

/**
 * Created by Gregory on 3/1/2018.
 */
public class Utils {

    public static void ShowError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
