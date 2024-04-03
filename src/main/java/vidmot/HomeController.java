package vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {

    //aðferðin frá SceneSwitcher
    SceneSwitcher sceneSwitcher = new SceneSwitcher();

    @FXML
    protected void onHelloButtonClick(ActionEvent event) {
        try {
            sceneSwitcher.switchScene(event, "askrifandi-view.fxml");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}