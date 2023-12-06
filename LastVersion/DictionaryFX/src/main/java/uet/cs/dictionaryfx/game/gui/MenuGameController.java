package uet.cs.dictionaryfx.game.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.cs.dictionaryfx.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuGameController implements Initializable {

    @FXML
    private Button newGameButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button musicButton;
    @FXML
    private Pane loadingPane;
    @FXML
    private ImageView musicImage;
    @FXML
    private AnchorPane helpPane;
    @FXML
    private Pane darkPane;
    @FXML
    private Button exitHelpButton;
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
        String mp3Path = MenuGameController.class.getResource("Assets/menu_music.mp3").toExternalForm();
        Media media = new Media(mp3Path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }
    public void handleNewGameButton(ActionEvent event) {
        new Thread(() -> {
            clearMusic();
            Platform.runLater(() -> loadingPane.setVisible(true));
        }).start();

        new Thread(() -> {
            SceneManager.loadRootGame();
            OpenGameEvent openGameEvent = new OpenGameEvent();
            try {
                Thread.sleep(3000); // Tạm dừng luồng trong 2 giây
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadingPane.setVisible(false);
            Platform.runLater(() -> newGameButton.fireEvent(openGameEvent));
        }).start();
    }


    public void handleHelpButton(ActionEvent event) {
        darkPane.setVisible(true);
        helpPane.setVisible(true);
    }

    public void handleExitHelpButton(ActionEvent event) {
        darkPane.setVisible(false);
        helpPane.setVisible(false);
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

    public static void clearMusic () {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}
