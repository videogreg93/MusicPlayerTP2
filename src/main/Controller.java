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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
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
    public HBox currentlyPlaying;
    public Button playButton;
    public Button stopButton;

    // Services
    ExampleService exampleService = new ExampleService();

    // Handle music playing
    MediaPlayer mediaPlayer;


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
                // Play Button
                Button playSongButton = new Button("Play");
                playSongButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        playSong(song);
                    }
                });
                hbox.getChildren().addAll(imageView, seperator, new Label(song.getTitle()), addToPlaylist, playSongButton);
                songResultsList.getItems().add(hbox);
            }
        }
    }

    private void playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(song.getMusic());
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                // Setup hbox to show data
                currentlyPlaying.getChildren().clear();
                ImageView imageView = new ImageView(song.getImage());
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                String title = song.getTitle();
                Region seperator = new Region();
                HBox.setHgrow(seperator, Priority.ALWAYS);
                // get song time
                double millis = mediaPlayer.getTotalDuration().toMillis();
                int minutes = (int) ((millis / 1000)  / 60);
                int seconds = (int) ((millis / 1000) % 60);
                String time = "00:00  /  " + minutes + ":" + seconds;
                Label timeLabel = new Label(time);
                currentlyPlaying.getChildren().addAll(imageView
                        ,seperator,new Label(title), timeLabel);
                // Set listener to update time
                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                    @Override
                    public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                        //TODO better formating
                        double currentMillis = newValue.toMillis();
                        int currentMinutes = (int) ((currentMillis / 1000)  / 60);
                        int currentSeconds = (int) ((currentMillis / 1000) % 60);
                        String total = currentMinutes + ":" + currentSeconds + "  /  " + minutes + ":" + seconds;
                        timeLabel.setText(total);
                    }
                });
                mediaPlayer.play();

                // Update play button
                playButton.setText("||");
            }
        });



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

    /**
     * Method handling when a user clicks on a song from search results
     * @param mouseEvent
     */
    public void onListItemClicked(MouseEvent mouseEvent) {

    }

    /**
     * On stop button pressed, we stop the current song
     * @param actionEvent
     */
    public void onStopPressed(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        playButton.setText(">");
    }

    public void onPlayPressed(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playButton.setText(">");
            }
            else {
                mediaPlayer.play();
                playButton.setText("||");
            }

        }
    }
}
