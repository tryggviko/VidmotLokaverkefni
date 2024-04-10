package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vidmot.SceneSwitcher;
import vinnsla.Playlist;
import vinnsla.PlaylistManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    @FXML
    private VBox songsVBox;

    private Playlist currentPlaylist;

    private static final String PLAYLISTS_DIRECTORY = "playlists";
    private String playlistFileName;

    private String playlistFilePath;

    // Set the file name for the current playlist
    public void setPlaylistFileName(String playlistFileName) {
        this.playlistFileName = playlistFileName;
    }

    public void setPlaylistFilePath(String playlistFilePath) {
        this.playlistFilePath = playlistFilePath;
    }

    public void initialize() {
        loadPlaylistFromFile();
    }

    private void loadPlaylistFromFile() {
        // Load playlist data from file
        List<Playlist> playlists = PlaylistManager.loadPlaylists();
        if (!playlists.isEmpty()) {
            // Assuming the first playlist is the current playlist
            currentPlaylist = playlists.get(0);
            updateSongsList();
        }
    }

    // Set the playlist and load its data if the file name is provided
    public void setPlaylist(Playlist playlist) {
        if (playlist == null) {
            this.currentPlaylist = new Playlist("New Playlist");
        } else {
            this.currentPlaylist = playlist;
            if (playlistFileName != null && !playlistFileName.isEmpty()) {
                loadPlaylistDataFromFile(playlistFileName);
            }
        }
        updateSongsList();
    }

    // Update the UI with the songs of the current playlist
    private void updateSongsList() {
        songsVBox.getChildren().clear();
        for (String songPath : currentPlaylist.getSongPaths()) {
            // Extract the file name from the absolute path
            String fileName = new File(songPath).getName();
            Button songButton = new Button(fileName);
            songsVBox.getChildren().add(songButton);
        }
    }

    // Load playlist data from file
    private void loadPlaylistDataFromFile(String playlistFileName) {
        List<String> songPaths = new ArrayList<>();
        File playlistFile = new File(PLAYLISTS_DIRECTORY, playlistFileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(playlistFile))) {
            String songPath;
            while ((songPath = reader.readLine()) != null) {
                songPaths.add(songPath);
            }
            currentPlaylist.setSongPaths(songPaths);
        } catch (IOException e) {
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
            savePlaylistToFile(currentPlaylist);
        }
    }


    // Save the playlist to a file
    private void savePlaylistToFile(Playlist playlist) {
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        PlaylistManager.savePlaylists(playlists);
    }

    // Add a song button to the VBox
    private void addSongToVBox(String songName) {
        Button songButton = new Button(songName);
        songsVBox.getChildren().add(songButton);
    }
}
