package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import vinnsla.Playlist;
import vinnsla.PlaylistManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    @FXML
    private VBox songsVBox;
    private Playlist currentPlaylist;


    //Playlist mapan sem mun geyma playlistana
    private static final String PLAYLISTS = "playlists";
    private String playlistFileName;
    private String playlistFilePath;

    private SceneSwitcher sceneSwitcher;

    public PlaylistController() {
        this.sceneSwitcher = new SceneSwitcher();
    }

    // setur slóðina á current playlista
    public void setPlaylistFilePath(String playlistFilePath) {
        this.playlistFilePath = playlistFilePath;
    }

    public void initialize() {
        loadPlaylistFromFile();
    }


    // eventið fyrir lögin
    private void songButtonClick(ActionEvent event, String songPath) {
        try {
            sceneSwitcher.switchToLag(event, currentPlaylist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hleður gögnunum frá núverandi playlista (lögum)
    private void loadPlaylistFromFile() {

        List<Playlist> playlists = PlaylistManager.loadPlaylists();
        if (!playlists.isEmpty()) {
            currentPlaylist = playlists.get(0);
            updateSongsList();
        }
    }

    // ef nafn files er til staðar þá hleður gögnum frá því skajli
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

    /*
    mikilvægt fall sem hleður inn lögunum í playlistanum
    og nær nafni skjalf frá absolute path,
    svo nafn lags sjáist
     */
    private void updateSongsList() {
        songsVBox.getChildren().clear();
        for (String songPath : currentPlaylist.getSongPaths()) {
            String fileName = new File(songPath).getName();
            Button songButton = new Button(fileName);
            songButton.setOnAction(event -> songButtonClick(event, songPath));
            songsVBox.getChildren().add(songButton);
        }
    }

    // nær í lögin frá pathinu þeirra
    private void loadPlaylistDataFromFile(String playlistFileName) {
        List<String> songPaths = new ArrayList<>();
        File playlistFile = new File(PLAYLISTS, playlistFileName);
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

    // Switch to home (úr sceneswithcer)
    @FXML
    public void switchToHome(ActionEvent event) {
        try {
            SceneSwitcher sceneSwitcher = new SceneSwitcher();
            sceneSwitcher.switchScene(event, "home-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    fallið sem sér um að setja lögin inn í playlistana
    inniheldur bara filchooser til að notandi geti náð í lögin í tölvuna sína
    síðan helur fallið laginu inn sem hnappi í playlistan
     */
    @FXML
    public void addSongs() {
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
            updateSongsList();
        }
    }



    private void savePlaylistToFile(Playlist playlist) {
        List<Playlist> playlists = new ArrayList<>();
        playlists.add(playlist);
        PlaylistManager.savePlaylists(playlists);
    }


    private void addSongToVBox(String songName) {
        Button songButton = new Button(songName);
        songsVBox.getChildren().add(songButton);
    }

    private void alert(){

    }

    //aðferin sem sér um að deleta playlistanum
    @FXML
    public void deletePlaylist(ActionEvent event) {
        if (currentPlaylist == null) {
            System.out.println("No playlist selected to delete!");
            return;
        }

        //Alert notað áður en eytt er playlistanum
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("delete Playlist");
        alert.setHeaderText("do you want to delete the playlist?");
        alert.setContentText("will be deleted permanently");
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // mapan með playlistanum og current playlist fundin
                File playlistFile = new File(PlaylistManager.PLAYLISTSFOLDER, currentPlaylist.getName() + ".ser");
                if (playlistFile.exists()) {
                    playlistFile.delete();
                }

                PlaylistManager.removePlaylist(currentPlaylist);

                // eit og skipt um scene
                songsVBox.getChildren().clear();
                currentPlaylist = null;
                switchToHome(event);
            }
        });
    }
}
