package uet.cs.dictionaryfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import uet.cs.dictionaryfx.game.gui.GameMenuController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FrameController implements Initializable {
    @FXML
    private Button homeButton;
    @FXML
    private Button googleAPIButton;
    @FXML
    private Button favoriteButton;
    @FXML
    private Button gameButton;
    @FXML
    private BorderPane mainBP;
    private STATUS status;
    private static Parent rootHome;
    private static Parent rootGame;
    private enum STATUS {
        CLONE,
        HOME,
        GOOGLEAPI,
        FAVORITE,
        GAME
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            //loadRoot();
            setCenterToHome();
            status = STATUS.HOME;
    }

    private void loadRoot() {
        new Thread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("game/gui/gameMenuGUI.fxml"));
                rootGame = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dictionary/gui/searchGUI.fxml"));
            rootHome = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHomeButton(ActionEvent event) {
        if (status != STATUS.HOME) {
            setCenterToHome();
            status = STATUS.HOME;
        }
    }

    public void handleGoogleAPIButton(ActionEvent event) {

    }

    public void handleFavoriteButton(ActionEvent event) {

    }

    public void handleGameButton(ActionEvent event) {
        if (status != STATUS.GAME) {
            GameMenuController.startMusic();
            setCenterToGame();
            status = STATUS.GAME;
        }
    }


    public void setCenterToHome() {
        mainBP.setCenter(SceneManager.getRootSearch());
    }

    public void setCenterToGame() {
        mainBP.setCenter(SceneManager.getRootGame());
    }
    public void setCenterToGameMenu() {mainBP.setCenter(SceneManager.getRootGameMenu());}
}
