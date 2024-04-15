package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import vinnsla.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LagController {

    @FXML
    private ImageView image;

    @FXML
    private Label songName;

    @FXML
    private ProgressBar songProgress;

    private Playlist currentPlaylist;
    private List<String> songPaths;
    @FXML
    private Slider volumeSlider;
    private int currentSongIndex = 0;

    private Media media;
    private MediaPlayer mediaPlayer;



    public void setPlaylist(Playlist playlist, List<String> songPaths) {
        this.currentPlaylist = playlist;
        this.songPaths = songPaths;
        playSong();
    }

    @FXML
    private void changeVolume(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volumeSlider.getValue());
        }
    }

    private void playSong() {
        if (currentSongIndex < songPaths.size()) {
            String songPath = songPaths.get(currentSongIndex);
            String songFileName = new File(songPath).getName();
            songName.setText(songFileName);

            media = new Media(new File(songPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
            mediaPlayer.play();
            startTimer();
        }
    }

    @FXML
    private void playPause(ActionEvent event) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    @FXML
    private void nextSong(ActionEvent event) {
        if (mediaPlayer != null){
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                mediaPlayer.stop();
            }
        }
        if (currentSongIndex < songPaths.size() - 1) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0;
        }
        playSong();
    }

    @FXML
    private void prevSong(ActionEvent event) {
        if (mediaPlayer != null){
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING){
                mediaPlayer.stop();
            }
        }
        if (currentSongIndex > 0) {
            currentSongIndex--;
        } else {
            currentSongIndex = songPaths.size() - 1;
        }
        playSong();
    }

    private void startTimer() {
        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                songProgress.setProgress(newValue.toSeconds() / media.getDuration().toSeconds());
            });
        }
    }

    @FXML
    private void switchToHome(ActionEvent event) {
        try {
            mediaPlayer.stop();
            SceneSwitcher sceneSwitcher = new SceneSwitcher();
            sceneSwitcher.switchScene(event, "home-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
