package uet.cs.dictionaryfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import uet.cs.dictionaryfx.game.gui.GameController;
import uet.cs.dictionaryfx.game.gui.GameMenuController;

import java.net.URL;
import java.util.ResourceBundle;


public class FrameController implements Initializable {
    @FXML
    private Button searchButton;
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
    private GameMenuController menuGameController;
    private GameController gameController;

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
        setCenterToSearch();
        status = STATUS.HOME;
        mainBP.addEventHandler(GameController.OpenGameMenuEvent.OPEN_GAME_MENU_EVENT_TYPE, event -> {
            setCenterToGameMenu();
        });
        mainBP.addEventHandler(GameMenuController.OpenGameEvent.OPEN_GAME_EVENT_TYPE, event -> {
            setCenterToGame();
            new Thread(() -> {
                SceneManager.loadRootGame();
                System.out.println("done");
            }).start();
        });
    }

    public void setDefaultButtonsColor() {
        searchButton.setStyle("-fx-background-color: #375575; -fx-background-radius: 100");
        googleAPIButton.setStyle("-fx-background-color: #375575; -fx-background-radius: 100");
        favoriteButton.setStyle("-fx-background-color: #375575; -fx-background-radius: 100");
        gameButton.setStyle("-fx-background-color: #375575; -fx-background-radius: 100");
    }

    public void handleSearchButton(ActionEvent event) {
        if (status != STATUS.HOME) {
            setCenterToSearch();
            status = STATUS.HOME;
        }
    }

    public void handleGoogleAPIButton(ActionEvent event) {
        if (status != STATUS.GOOGLEAPI) {
            setCenterToGoogleTranslate();
            status = STATUS.GOOGLEAPI;
        }
    }

    public void handleFavoriteButton(ActionEvent event) {

    }

    public void handleGameButton(ActionEvent event) {
        if (status != STATUS.GAME) {
            setCenterToGameMenu();
            status = STATUS.GAME;

        }
    }

    public void setCenterToSearch() {
        mainBP.setCenter(SceneManager.getRootSearch());
        setDefaultButtonsColor();
        searchButton.setStyle("-fx-background-color: #00CCEA; -fx-background-radius: 100");
    }

    public void setCenterToGame() {
        mainBP.setCenter(SceneManager.getRootGame());
    }

    public void setCenterToGameMenu() {
        mainBP.setCenter(SceneManager.getRootGameMenu());
        setDefaultButtonsColor();
        gameButton.setStyle("-fx-background-color: #00CCEA; -fx-background-radius: 100");
    }

    public void setCenterToGoogleTranslate() {
        mainBP.setCenter(SceneManager.getRootGoogleTranslate());
        setDefaultButtonsColor();
        googleAPIButton.setStyle("-fx-background-color: #00CCEA; -fx-background-radius: 100");
    }
}
