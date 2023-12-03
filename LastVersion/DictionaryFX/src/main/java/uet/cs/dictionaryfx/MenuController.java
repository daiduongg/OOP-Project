package uet.cs.dictionaryfx;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import uet.cs.dictionaryfx.dictionary.gui.SearchController;
import uet.cs.dictionaryfx.dictionary.model.Dictionary;
import uet.cs.dictionaryfx.dictionary.model.DictionaryManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private ImageView loadingImage;
    @FXML
    private Button joinButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            Dictionary enDictionary = new Dictionary(Dictionary.MODE.ENGLISH);
            DictionaryManager.setEnDictionary(enDictionary);
            loadingData();
        }).start();

        new Thread(() -> {
            Dictionary viDictionary = new Dictionary(Dictionary.MODE.VIETNAMESE);
            DictionaryManager.setViDictionary(viDictionary);
        }).start();
    }

    public void loadingData() {
        new Thread(() -> {
            SceneManager.loadRootSearch();
            SceneManager.loadRootFrame();
            onDictionaryLoadComplete();
        }).start();

        new Thread(() -> {
            SceneManager.loadRootGame();
        }).start();
    }

    //@Override
    public void onDictionaryLoadComplete() {
        Platform.runLater(() -> {
            loadingImage.setVisible(false);
            joinButton.setVisible(true);
        });
    }

    public void handleJoinButton(ActionEvent event)  {
        StageManager.showStage(new Scene(SceneManager.getRootFrame()));
    }

}
