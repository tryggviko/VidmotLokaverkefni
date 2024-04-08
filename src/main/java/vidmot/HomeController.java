package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {

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
}