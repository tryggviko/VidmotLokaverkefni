package vidmot;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    @FXML
    private VBox songsVBox;

    private List<String> selectedFiles = new ArrayList<>();

    @FXML
    public void handleAddSongs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Songs");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.flac"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        List<File> selectedFilesList = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFilesList != null) {
            for (File file : selectedFilesList) {
                selectedFiles.add(file.getAbsolutePath());
                addSongToVBox(file.getName());
            }
        }
    }

    private void addSongToVBox(String songName) {
        Button songButton = new Button(songName);
        songsVBox.getChildren().add(songButton);
    }
}
