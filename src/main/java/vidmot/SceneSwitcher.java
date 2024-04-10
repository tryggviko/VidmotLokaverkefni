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

    /*
    Svipuð aðgerð til að skipta á milli scene nema þessi er fyrir playlistana
    nottar nokkra hluti úr playlistcontroller klasanum til þess að geta opnað scene
    hjá hverjum og einum playlista sér
     */
    public void switchScenePlaylists(ActionEvent event, String fxmlFileName, Playlist selectedPlaylist, String playlistFilePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        PlaylistController playlistController = loader.getController();
        playlistController.setPlaylist(selectedPlaylist);
        playlistController.setPlaylistFilePath(playlistFilePath);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Switch to the lag (media player) scene
    public void switchToLag(ActionEvent event, Playlist playlist) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lag-view.fxml"));
        Parent root = loader.load();

        LagController lagController = loader.getController();
        lagController.setPlaylist(playlist, playlist.getSongPaths());

        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
