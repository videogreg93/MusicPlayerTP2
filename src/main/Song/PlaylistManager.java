package main.Song;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import main.SoundManager;
import main.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class PlaylistManager {
    public static ArrayList<Playlist> allPlaylists;

    public static JFXListView playlistListView;
    private static JFXListView queueListView;

    public static void init(JFXListView view, JFXListView queue) {
        allPlaylists = new ArrayList<>();
        playlistListView = view;
        queueListView = queue;
        loadPlaylists();
        refreshPlaylistView();
    }

    /**
     * Method to create a new playlist
     */
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

    /**
     * Refresh the playlist view with current data
     */
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
            // Delete playlist button
            Button deletePlaylistButton = new Button("Delete");
            deletePlaylistButton.setStyle(" -fx-base: #F31431;");
            deletePlaylistButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PlaylistManager.deletePlaylist(playlist);
                }
            });
            // Play playlist button
            Button playButton = new Button("play all");
            playButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    playPlaylist(playlist);
                }
            });
            hbox.getChildren().addAll(new Label(playlist.getName()),seperator, new Label(songCount), playButton, deletePlaylistButton);
            playlistListView.getItems().add(hbox);


        }
        savePlaylists();
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

    /**
     * Deletes the playlist
     * @param playlist the playlist to be deleted
     */
    public static void deletePlaylist(Playlist playlist) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Playlist");
        alert.setHeaderText(playlist.getName());
        alert.setContentText("Are you sure you want to delete this playlist?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            allPlaylists.remove(playlist);
            refreshPlaylistView();
        }
    }

    /**
     * Called upon startup, loads the playlists from file
     */
    public static void loadPlaylists() {
        File file = new File("./playlists.json");
        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                LineNumberReader lnr = new LineNumberReader(fr);
                JSONObject playlists = new JSONObject(lnr.readLine());
                for (String key: playlists.keySet()) {
                    JSONArray allSongs = playlists.getJSONArray(key);
                    Playlist newPlaylist = new Playlist(key);
                    for (Object songObject : allSongs) {
                        JSONObject song = (JSONObject) songObject;
                        SongBuilder builder = new SongBuilder();
                        builder.title(song.getString("title"))
                                .addMusicUriFullPath(song.getString("music"))
                                .imageUrl(song.getString("image"));
                        for (String data: song.keySet()) {
                            builder.addMetadata(data, song.getString(data));
                        }
                        newPlaylist.addSong(builder.build());
                    }
                    allPlaylists.add(newPlaylist);
                }
                refreshPlaylistView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current playlists to disk. Called everytime the
     * list of playlists is modified
     */
    public static void savePlaylists() {
       // if (allPlaylists.isEmpty())
         //   return;
        JSONObject fileObject = new JSONObject();
        for (Playlist playlist : allPlaylists) {
            JSONArray allSongs = new JSONArray();
            for (Song song : playlist.allSongs) {
                JSONObject object = new JSONObject();
                // Add image and picture url
                object.put("image", song.getImage().impl_getUrl());
                object.put("music", song.getMusic().getSource());
                for (String key: song.metadata.keySet()) {
                    object.put(key, song.getMetadataValue(key));
                }
                allSongs.put(object);
            }
            fileObject.put(playlist.name, allSongs );
        }
        // Save to disk
        File file = new File("./playlists.json");
        try {
            PrintWriter writer = new PrintWriter(new FileOutputStream(file, false));
            writer.write(fileObject.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(file.toString());
    }
}
