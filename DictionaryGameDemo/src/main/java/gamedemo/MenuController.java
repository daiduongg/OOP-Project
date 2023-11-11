package gamedemo;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.IOException;
import javafx.scene.media.MediaPlayer.Status;

public class MenuController {
    @FXML
    private Button playButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button musicButton;


    @FXML
    public void handlePlayButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Game newGame = new Game();
        newGame.start(stage);
    }

    @FXML
    public void handleHelpButton() {
        // Xử lý sự kiện khi người chơi nhấn nút "Help"
        System.out.println("Help button clicked");
    }

    public void handleMusicButton(ActionEvent event) {
        String mp3File = "file:/C:/Users/daidu/OneDrive%20-%20vnu.edu.vn/Desktop/BP%20OOP/DictionaryGameDemo/src/main/resources/gamedemo/back_music.mp3";
        Media media = new Media(mp3File);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        Status status = mediaPlayer.getStatus();

        if (status == Status.PLAYING) {
            mediaPlayer.pause(); // Tạm dừng phát âm thanh nếu đang chạy
        } else if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
            mediaPlayer.play(); // Phát âm thanh nếu đang tạm dừng hoặc dừng
        }
    }
}
