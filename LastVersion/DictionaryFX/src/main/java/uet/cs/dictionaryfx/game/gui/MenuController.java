package uet.cs.dictionaryfx.game.gui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
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


    private static MediaPlayer mediaPlayer;

    public class LoadingTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            // Thực hiện công việc nền ở đây, ví dụ như load dữ liệu hoặc chuẩn bị các tài nguyên cần thiết
            darkPane.setVisible(true);
            loadingImage.setVisible(true);

            Thread.sleep(500); // Giả sử công việc nền mất 2 giây
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String mp3Path = getClass().getResource("Assets/menu_music.mp3").toExternalForm();
        Media media = new Media(mp3Path);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }

    public void handleNewGameButton(ActionEvent event) {
        LoadingTask loadingTask = new LoadingTask();
        loadingTask.setOnSucceeded(workerStateEvent -> {
            Platform.runLater(() -> {
                clear();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("gameGUI.fxml"));
                    Parent root = loader.load();
                    StageManager.showStage(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        new Thread(loadingTask).start();
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
