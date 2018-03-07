package main.Song;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import main.SoundManager;
import main.Utils;

import java.util.ArrayList;
import java.util.Optional;

public class PlaylistManager {
    public static ArrayList<Playlist> allPlaylists;

    private static JFXListView playlistListView;
    private static JFXListView queueListView;

    public static void init(JFXListView view, JFXListView queue) {
        allPlaylists = new ArrayList<>();
        playlistListView = view;
        queueListView = queue;
        refreshPlaylistView();
    }

    public static void createNewPlaylist() {
        String defaultName = "newPlaylist" + (allPlaylists.size()+1);
        TextInputDialog dialog = new TextInputDialog(defaultName);
        dialog.setTitle("Create new Playlist");
        dialog.setHeaderText(null);
        dialog.setContentText("Name: ");

        Optional<String> result = dialog.showAndWait();
        String name = defaultName;
        if (result.isPresent()){
            name = result.get();
            // Make sure a playlist doesnt already exist with this name
            boolean isValid = true;
            if (name.isEmpty())
                isValid = false;
            //TODO this whole part is kinda wonky
            for (Playlist playlist : allPlaylists) {
                if (playlist.getName().equals(name))
                    isValid = false;
            }
            if (isValid) {
                allPlaylists.add(new Playlist(name));
                PlaylistManager.refreshPlaylistView();
            } else {
                Utils.ShowError("A playlist with that name already exists, please choose a new name");
                createNewPlaylist();
            }
        }


    }

    public static void refreshPlaylistView() {
        playlistListView.getItems().clear();
        // Add a new playlist button to the top of the playlist list
        Button newPlaylistButton = new Button("Create new playlist");
        newPlaylistButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PlaylistManager.createNewPlaylist();
            }
        });
        playlistListView.getItems().add(0,newPlaylistButton);
        for (Playlist playlist : allPlaylists) {
            HBox hbox = new HBox(8);
            String songCount = playlist.getAllSongs().size() + " songs";
            Region seperator = new Region();
            HBox.setHgrow(seperator, Priority.ALWAYS);
            // Play playlist button
            Button playButton = new Button("play all");
            playButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    playPlaylist(playlist);
                }
            });
            hbox.getChildren().addAll(new Label(playlist.getName()),seperator, new Label(songCount), playButton);
            playlistListView.getItems().add(hbox);
        }
    }

    /**
     * Takes a playlist and adds it to the play queue. We can also use this as a central point to play any song,
     * we just have to make a playlist with a single song on the fly.
     * @param playlist the playlist to be played
     */
    public static void playPlaylist(Playlist playlist) {
        SoundManager.playPlaylist(playlist);
    }

    public static void addSongToPlaylist(Song song) {
        // TODO add the option to directly create one right here
        if (allPlaylists.isEmpty())
            Utils.ShowError("You don't have any playlists, start by creating one");
        else {
            ChoiceDialog<Playlist> dialog = new ChoiceDialog<>(allPlaylists.get(0), allPlaylists);
            dialog.setTitle("Add to which playlist?");
            dialog.setHeaderText(null);
            dialog.setContentText("Choose your playlist:");

            Optional<Playlist> result = dialog.showAndWait();
            if (result.isPresent()){
                result.get().addSong(song);
                refreshPlaylistView();
            }
        }
    }

    public static void deletePlaylist() {

    }

    public static void loadPlaylists() {

    }

    public static void savePlaylists() {

    }
}
