package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vinnsla.Playlist;

import java.io.IOException;
import java.util.Objects;

public class SceneSwitcher {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Aðferð til þess að skipta á milli scene
    public void switchScene(ActionEvent event, String fxmlFileName) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFileName)));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchScenePlaylists(ActionEvent event, String fxmlFileName, Playlist selectedPlaylist, String playlistFilePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load(); // Load the FXML file

        Scene scene = new Scene(root);

        // Get the PlaylistController instance from the FXMLLoader (moved here)
        PlaylistController playlistController = loader.getController();

        // Set the current playlist in the PlaylistController
        playlistController.setPlaylist(selectedPlaylist);
        playlistController.setPlaylistFilePath(playlistFilePath);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
