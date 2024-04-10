package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vinnsla.Playlist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    @FXML
    private VBox songsVBox;

    private Playlist currentPlaylist;

    private static final String PLAYLIST_FOLDER = "playlists";
    private String playlistFilePath;

    // Set the file path for the current playlist
    public void setPlaylistFilePath(String playlistFilePath) {
        this.playlistFilePath = playlistFilePath;
    }

    // Set the playlist and load its data if the file path is provided
    public void setPlaylist(Playlist playlist) {
        if (playlist == null) {
            this.currentPlaylist = new Playlist("New Playlist");
        } else {
            this.currentPlaylist = playlist;
            if (playlistFilePath != null && !playlistFilePath.isEmpty()) {
                loadPlaylistDataFromFile(playlistFilePath);
            }
        }
        updateSongsList();
    }

    // Update the UI with the songs of the current playlist
    private void updateSongsList() {
        songsVBox.getChildren().clear();
        for (String songPath : currentPlaylist.getSongPaths()) {
            Button songButton = new Button(songPath);
            songsVBox.getChildren().add(songButton);
        }
    }

    // Load playlist data from file
    private void loadPlaylistDataFromFile(String playlistFilePath) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(playlistFilePath))) {
            // Read the list of song paths
            List<String> songPaths = (List<String>) inputStream.readObject();

            // Read the playlist object
            Playlist loadedPlaylist = (Playlist) inputStream.readObject();

            // Set the song paths to the loaded playlist
            loadedPlaylist.setSongPaths(songPaths);

            // Set the currentPlaylist to the loaded playlist
            currentPlaylist = loadedPlaylist;

            // Update UI (if needed)
            updateSongsList();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Switch to home view
    @FXML
    public void switchToHome(ActionEvent event) {
        try {
            SceneSwitcher sceneSwitcher = new SceneSwitcher();
            sceneSwitcher.switchScene(event, "home-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle adding songs to the playlist
    @FXML
    public void handleAddSongs() {
        if (currentPlaylist == null) {
            System.out.println("Please select a playlist first!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Songs");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        List<File> selectedFilesList = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFilesList != null) {
            for (File file : selectedFilesList) {
                currentPlaylist.addSong(file.getAbsolutePath());
                addSongToVBox(file.getName());
            }
            savePlaylistToFile(currentPlaylist, generatePlaylistFilePath(currentPlaylist));
        }
    }


    // Save the playlist to a file
    private void savePlaylistToFile(Playlist playlist, String fileName) {
        String completeFilePath = PLAYLIST_FOLDER + File.separator + fileName;
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(completeFilePath))) {
            // Write the playlist object
            outputStream.writeObject(playlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Generate a file path for the playlist
    private String generatePlaylistFilePath(Playlist playlist) {
        if (playlistFilePath != null && !playlistFilePath.isEmpty()) {
            return playlistFilePath;
        } else {
            return playlist.getName() + ".dat"; // Use playlist name as file name
        }
    }

    // Add a song button to the VBox
    private void addSongToVBox(String songName) {
        Button songButton = new Button(songName);
        songsVBox.getChildren().add(songButton);

    }

}
