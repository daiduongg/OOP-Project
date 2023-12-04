package uet.cs.dictionaryfx.game.gui;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameMenuController implements Initializable {
    @FXML
    private Button newGameButton;
    @FXML
    private Button helpButton;
    //@FXML
    //private Button highScoreButton;
    @FXML
    private Button musicButton;
    @FXML
    private ImageView loadingImage;
    @FXML
    private ImageView musicImage;
    @FXML
    private AnchorPane helpPane;
    @FXML
    private Pane darkPane;
    @FXML
    private Button exitHelpButton;
    private Parent newGameRoot;
    private static MediaPlayer mediaPlayer;

    public class OpenGameEvent extends Event {
        public static final EventType<OpenGameEvent> OPEN_GAME_EVENT_TYPE = new EventType<>(Event.ANY, "OPEN_GAME");

        public OpenGameEvent() {
            super(OPEN_GAME_EVENT_TYPE);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public static void startMusic() {
        String mp3Path = GameMenuController.class.getResource("Assets/menu_music.mp3").toExternalForm();
        Media media = new Media(mp3Path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
        System.out.println(-1);
    }
    public void handleNewGameButton(ActionEvent event) {

        //clear();
        //StageManager.showStage(new Scene(newGameRoot));
        OpenGameEvent openGameEvent = new OpenGameEvent();
        newGameButton.fireEvent(openGameEvent);
    }


    public void handleHelpButton(ActionEvent event) {
        darkPane.setVisible(true);
        helpPane.setVisible(true);
    }

    public void handleExitHelpButton(ActionEvent event) {
        darkPane.setVisible(false);
        helpPane.setVisible(false);
    }
    public void handleHighScoreButton(ActionEvent event) {

    }

    public void handleMusicButton(ActionEvent event) {
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            mediaPlayer.pause();
            Image image = new Image(getClass().getResourceAsStream("Assets/mute.png"));
            musicImage.setImage(image);
        } else if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            mediaPlayer.play();
            Image image = new Image(getClass().getResourceAsStream("Assets/unmute.png"));
            musicImage.setImage(image);
        }
    }

    public void clear() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
        loadingImage.setImage(null);
        musicImage.setImage(null);

        // Xóa các style của các button nếu cần thiết
        newGameButton.setStyle("");
        helpButton.setStyle("");
        //highScoreButton.setStyle("");
        musicButton.setStyle("");
    }
}
