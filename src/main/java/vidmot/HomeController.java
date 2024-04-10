package vidmot;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import vinnsla.Playlist;
import vinnsla.PlaylistManager;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static vinnsla.PlaylistManager.*;


public class HomeController {

    @FXML
    private VBox playlistContainer;

    @FXML
    private TextField playlistTextField;



    public void initialize() {
        loadAndShowPlaylists();
    }


    //aðferðin frá SceneSwitcher
    SceneSwitcher sceneSwitcher = new SceneSwitcher();


    @FXML
    public void onHelloButtonClick(ActionEvent event) {
        try {
            sceneSwitcher.switchScene(event, "lag-view.fxml");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void toLogInScreen(ActionEvent event){
        try {
            sceneSwitcher.switchScene(event, "askrifandi-view.fxml");
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void createPlaylistFxml(String playlistName) {
        try {
            // Get the resource URL for the FXML file
            URL resourceUrl = getClass().getResource("playlist-view.fxml");
            if (resourceUrl == null) {
                System.err.println("Error: FXML resource not found.");
                return; // Handle the error appropriately
            }

            // Create a file from the resource URL
            File templateFile = new File(resourceUrl.toURI());

            // Construct the output file name
            String fileName = playlistName + ".fxml";

            // Read from the template file and write to the output file
            try (FileReader reader = new FileReader(templateFile);
                 FileWriter writer = new FileWriter(fileName)) {
                int character;
                while ((character = reader.read()) != -1) {
                    writer.write(character);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception (e.g., log it or show an error message)
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log it or show an error message)
        }
    }


    public void createPlaylist() {

        String playlistName = playlistTextField.getText();
        if (!playlistName.isEmpty()) {
            List<Playlist> playlists = PlaylistManager.loadPlaylists();
            playlists.add(new Playlist(playlistName));
            savePlaylists(playlists);
            createPlaylistFxml(playlistName);
            updatePlaylistsUI();
            playlistTextField.clear();
        } else {
            playlistTextField.setText("legal name pls");
        }
    }


    private void updatePlaylistsUI() {
        playlistContainer.getChildren().clear();
        List<Playlist> playlists = PlaylistManager.loadPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("Create a new playlist!");

        } else {
            for (Playlist playlist : playlists) {
                Button playlistButton = new Button(playlist.getName());
                playlistButton.setOnAction(event -> handlePlaylistClick(event, playlist)); // Set event handler
                playlistContainer.getChildren().add(playlistButton);
            }
        }
    }

    public void loadAndShowPlaylists() {
        updatePlaylistsUI();
    }

    private void handlePlaylistClick(ActionEvent event, Playlist playlist) {
        try {
            String playlistFilePath = playlist.getName() + ".dat"; // Construct playlist file path
            sceneSwitcher.switchScenePlaylists(event, "playlist-view.fxml", playlist, playlistFilePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}