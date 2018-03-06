package main;

import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
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
                Region seperator = new Region();
                HBox.setHgrow(seperator, Priority.ALWAYS);
                // get song time
                double millis = mediaPlayer.getTotalDuration().toMillis();
                int minutes = (int) ((millis / 1000)  / 60);
                int seconds = (int) ((millis / 1000) % 60);
                String time = "00:00  /  " + minutes + ":" + seconds;
                Label timeLabel = new Label(time);
                currentlyPlayingView.getChildren().addAll(imageView
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
        // TODO actually play songs
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
            queueListView.getItems().add(new Label(song.getTitle()));
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
