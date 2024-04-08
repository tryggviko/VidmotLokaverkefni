package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LagController implements Initializable {

    @FXML
    private Button homeButton,nextButtton,playButtong,playlistButton,prevButton;

    @FXML
    private Label songName;

    @FXML
    private ProgressBar songProgress;

    @FXML
    private ImageView image;


    private Media media;
    private MediaPlayer mediaPlayer;


    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNum;

    private Timer time;

    private TimerTask timerTask;

    private boolean running;





    //aðferðin frá SceneSwitcher
    SceneSwitcher sceneSwitcher = new SceneSwitcher();
    @FXML
    public void switchToHome(ActionEvent event) {
        try {
            sceneSwitcher.switchScene(event, "home-view.fxml");
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<File>();

        directory = new File("songs");

        files = directory.listFiles();

        if (files != null){
            for (File file:files) {
                songs.add(file);
                System.out.println(file);
            }
        }
        media = new Media(songs.get(songNum).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songName.setText(songs.get(songNum).getName()  );
        mediaPlayer.play();
        startTimer();
    }

    public void prevSong(){

        if (songNum > 0){
            songNum--;
            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songName.setText(songs.get(songNum).getName()  );
        }
        else {
            songNum = songs.size() - 1;
            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songName.setText(songs.get(songNum).getName()  );
        }
        mediaPlayer.play();
        startTimer();
    }

    public void nextSong(){

        if (songNum < songs.size() - 1){
            songNum++;
            mediaPlayer.stop();

            if (running){
                stopTimer();
            }


            media = new Media(songs.get(songNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songName.setText(songs.get(songNum).getName()  );
        }
        else {
            songNum = 0;
            mediaPlayer.stop();

            if (running){
                stopTimer();
            }

            media = new Media(songs.get(songNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songName.setText(songs.get(songNum).getName()  );
        }
        mediaPlayer.play();
        startTimer();
    }

    public void playPause(){
        //mediaPlayer.play();
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.pause();
            playButtong.setText("pause");
        }
        else if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED)){
            mediaPlayer.play();
            playButtong.setText("play");
        }
        startTimer();

    }

    public void startTimer(){
        time = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                running  = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgress.setProgress(current/end);

                if(current/end == 1){
                    stopTimer();
                }
            }
        };

        time.scheduleAtFixedRate(timerTask,1000,1000);



    }

    public  void stopTimer(){
        running = false;
        time.cancel();

    }

}
