package gamedemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    @FXML
    private Button backMenuButton;

    @FXML
    public void handleBackMenuButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Menu newMenu = new Menu();
        newMenu.start(stage);
    }
}