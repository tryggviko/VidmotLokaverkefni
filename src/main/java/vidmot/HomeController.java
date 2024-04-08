package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import java.io.IOException;


public class HomeController {

    @FXML
    private VBox playlistContainer;



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

    public void toPlaylists(ActionEvent event){
        try {
            sceneSwitcher.switchScene(event, "playlist-view.fxml");
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public void createPlaylist(ActionEvent event) {

    }
}