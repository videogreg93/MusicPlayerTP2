package main;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Song.Playlist;
import main.Song.Song;


public class SoundManager {
    private static MediaPlayer mediaPlayer;
    private static Playlist currentQueue;
    private static int currentSongIndex = 0;
    private static boolean isPlaylist = false;

    private static HBox currentlyPlayingView;
    private static JFXListView queueListView;

    // Handle seeking
    private static boolean isSeeking = false;

    public static void initialize(HBox currently, JFXListView queue) {
        currentlyPlayingView = currently;
        queueListView = queue;
    }

    public static void playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(song.getMusic());
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                // Setup hbox to show data
                currentlyPlayingView.getChildren().clear();
                ImageView imageView = new ImageView(song.getImage());
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                String title = song.getTitle();
                //Region seperator = new Region();
                //HBox.setHgrow(seperator, Priority.ALWAYS);

                // get song time
                double millis = mediaPlayer.getTotalDuration().toMillis();
                int minutes = (int) ((millis / 1000)  / 60);
                int seconds = (int) ((millis / 1000) % 60);
                String time = "00:00  /  " + minutes + ":" + seconds;
                Label timeLabel = new Label(time);
                // Seekbar
                JFXSlider seekbar = new JFXSlider(0, mediaPlayer.getTotalDuration().toSeconds() - 2,0 );
                seekbar.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Clicked");
                        isSeeking = true;

                    }
                });
                seekbar.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Released");
                        mediaPlayer.seek(new Duration(seekbar.getValue() * 1000));
                        isSeeking = false;
                    }
                });
                HBox.setHgrow(seekbar, Priority.ALWAYS);
                currentlyPlayingView.getChildren().addAll(imageView
                        ,seekbar,new Label(title), timeLabel);
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
                        // update seekbar if we're not currently seeking a new position
                        if (!isSeeking) {
                            seekbar.setValue(newValue.toSeconds());
                        }
                    }
                });
                mediaPlayer.play();

                // If this is a playlist, set it to play the next song afterwards
                if (isPlaylist) {
                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            currentSongIndex = ((currentSongIndex + 1) % currentQueue.getAllSongs().size());
                            playSong(currentQueue.getSong(currentSongIndex));
                        }
                    });
                }

                // Highlight current item in play queue
                queueListView.getSelectionModel().clearAndSelect(currentSongIndex);




            }
        });
    }

    /**
     * Used for playing a certain song when a user clicks on an item
     * from the play queue
     * @param atIndex index of song to be played.
     */
    public static void playSong(int atIndex) {
        currentSongIndex = atIndex;
        playSong(currentQueue.getSong(atIndex));
    }

    /**
     * Like play song, but only for one song. Sets play index to 0.
     * @param song
     */
    public static void playSingleSong(Song song) {
        playSong(song);
        currentSongIndex = 0;
        isPlaylist = false;
        // Turn the song into a single song playlist and add it to the visual queue
        Playlist queue = new Playlist("queue");
        queue.addSong(song);
        currentQueue = queue;
        refreshQueueView();
    }

    /**
     * Changes current queue to new playlist and plays first song
     * @param playlist the pkaylist to be played
     */
    public static void playPlaylist(Playlist playlist) {
        currentSongIndex = 0;
        currentQueue = playlist;
        refreshQueueView();
        isPlaylist = true;
        playSong(currentQueue.getSong(0));
    }

    /**
     * Refresh visually the currently playing view
     */
    private static void refreshQueueView() {
        // Clear playlist
        queueListView.getItems().clear();
        for (Song song: currentQueue.getAllSongs()) {
            Label artist = new Label(song.getMetadataValue("artist", "unknown artist"));
            Label seperator = new Label(" - ");
            Label title = new Label(song.getTitle());
            // Seperator
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            // More info button
            MenuItem moreInfo = new MenuItem("More info");
            moreInfo.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Stage dialog = new Stage();
                    JFXListView<String> list = new JFXListView<String>();
                    for(String key : song.getMetadata().keySet()) {
                        list.getItems().add(key + ": " +  song.getMetadataValue(key));
                    }
                    dialog.setScene(new Scene(list));
                    dialog.setMinWidth(200);
                    dialog.setMinHeight(200);
                    dialog.setTitle(song.getTitle());
                    //dialog.initOwner(queueListView.getParent().getta);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.showAndWait();
                }
            });
            MenuItem removeFromQueue = new MenuItem("Remove from queue");
            removeFromQueue.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentQueue.removeSong(song);
                    refreshQueueView();
                    
                }
            });
            MenuItem moveUp = new MenuItem("Move up in queue");
            moveUp.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentQueue.moveUpSong(song);
                    refreshQueueView();
                }
            });
            MenuItem moveDown = new MenuItem("Move down in queue");
            moveDown.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentQueue.moveDownSong(song);
                    refreshQueueView();
                }
            });
            MenuButton menuButton = new MenuButton("...", null, moreInfo, removeFromQueue, moveUp, moveDown);
            HBox hBox = new HBox();
            hBox.getChildren().addAll(artist, seperator, title, region, menuButton);
            queueListView.getItems().add(hBox);
        }
    }

    public static void onStopPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static boolean onPlayPressed() {
        if (mediaPlayer != null) {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                return false;
            }
            else {
                mediaPlayer.play();
                return true;
            }
        }
        return false;
    }


}
