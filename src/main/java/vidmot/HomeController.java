package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import vinnsla.Playlist;
import vinnsla.PlaylistManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static vinnsla.PlaylistManager.PLAYLISTSFOLDER;

public class HomeController {

    @FXML
    private VBox playlistContainer;

    @FXML
    private TextField playlistTextField;

    @FXML
    private Button profileButton;

    @FXML
    private ImageView profileImage;

    @FXML
    private Label usernamLabel;

    private final SceneSwitcher sceneSwitcher = new SceneSwitcher();

    private String username;

    public void initialize() {
        updatePlaylistsContain();
    }

    public void setUserName(String username) {
        this.username = username;
        usernamLabel.setText(username); // Update the label with the username
    }


    @FXML
    private void selectProfilePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImage.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            profileButton.setVisible(false);
            profileImage.setImage(image);
        }
    }


    //aðferðin sem býr til playlistan
    @FXML
    public void createPlaylist() {
        String playlistName = playlistTextField.getText().trim();
        if (!playlistName.isEmpty()) {
            List<Playlist> playlists = PlaylistManager.loadPlaylists();
            playlists.add(new Playlist(playlistName));
            PlaylistManager.savePlaylists(playlists);
            updatePlaylistsContain();
            playlistTextField.clear();
        } else {
            playlistTextField.setText("Enter a valid playlist name");
        }
    }

    //Þetta er aðferðin sem updaetar containerinn með playlistönum svo þeir byrtist
    private void updatePlaylistsContain() {
        playlistContainer.getChildren().clear();
        List<Playlist> playlists = PlaylistManager.loadPlaylists();
        for (Playlist playlist : playlists) {
            Button playlistButton = new Button(playlist.getName());
            playlistButton.setOnAction(event -> playlistClick(event, playlist)); // Set event handler
            playlistContainer.getChildren().add(playlistButton);
        }
    }

    //aðferðin svo hægt sé að ítta á playlista hnappana
    private void playlistClick(ActionEvent event, Playlist playlist) {
        try {
            String playlistFilePath = PLAYLISTSFOLDER + File.separator + playlist.getName() + ".ser";
            sceneSwitcher.switchScenePlaylists(event, "playlist-view.fxml", playlist, playlistFilePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //eftir að tengja og forrita
    public void toLogInScreen(ActionEvent event) {
        try {
            sceneSwitcher.switchScene(event, "askrifandi-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
