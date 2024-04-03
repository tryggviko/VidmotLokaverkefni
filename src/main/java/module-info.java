module hi.com.vidmotlokaverkefni {
    requires javafx.controls;
    requires javafx.fxml;


    opens hi.com.vidmotlokaverkefni to javafx.fxml;
    exports hi.com.vidmotlokaverkefni;
}