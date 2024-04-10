package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import vidmot.SceneSwitcher;
import vinnsla.Playlist;
import vinnsla.PlaylistManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static vinnsla.PlaylistManager.PLAYLISTS_DIRECTORY;

public class HomeController {

    @FXML
    private VBox playlistContainer;

    @FXML
    private TextField playlistTextField;

    private final SceneSwitcher sceneSwitcher = new SceneSwitcher();

    public void initialize() {
        updatePlaylistsUI();
    }

    @FXML
    public void createPlaylist() {
        String playlistName = playlistTextField.getText().trim();
        if (!playlistName.isEmpty()) {
            List<Playlist> playlists = PlaylistManager.loadPlaylists();
            playlists.add(new Playlist(playlistName));
            PlaylistManager.savePlaylists(playlists);
            updatePlaylistsUI();
            playlistTextField.clear();
        } else {
            playlistTextField.setText("Enter a valid playlist name");
        }
    }

    private void updatePlaylistsUI() {
        playlistContainer.getChildren().clear();
        List<Playlist> playlists = PlaylistManager.loadPlaylists();
        for (Playlist playlist : playlists) {
            Button playlistButton = new Button(playlist.getName());
            playlistButton.setOnAction(event -> handlePlaylistClick(event, playlist)); // Set event handler
            playlistContainer.getChildren().add(playlistButton);
        }
    }

    private void handlePlaylistClick(ActionEvent event, Playlist playlist) {
        try {
            String playlistFilePath = PLAYLISTS_DIRECTORY + File.separator + playlist.getName() + ".ser";
            sceneSwitcher.switchScenePlaylists(event, "playlist-view.fxml", playlist, playlistFilePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void toLogInScreen(ActionEvent event) {
    }
}
