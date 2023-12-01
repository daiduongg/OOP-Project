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
import uet.cs.dictionaryfx.dictionary.model.Dictionary;
import uet.cs.dictionaryfx.dictionary.model.DictionaryManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable, Dictionary.DictionaryLoadListener {
    @FXML
    private ImageView loadingImage;
    @FXML
    private Button joinButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Dictionary enDictionary = new Dictionary(Dictionary.MODE.ENGLISH);
        Dictionary viDictionary = new Dictionary(Dictionary.MODE.VIETNAMESE);

        enDictionary.addLoadListener(this);

        DictionaryManager.setEnDictionary(enDictionary);
        DictionaryManager.setViDictionary(viDictionary);
        joinButton.setVisible(false);

        /*
        // Tạo một hẹn giờ với khoảng thời gian là 1 giây
        Duration duration = Duration.seconds(3);
        KeyFrame keyFrame = new KeyFrame(duration, event -> {
            // Đặt trạng thái của joinButton thành true sau 1 giây
            joinButton.setVisible(true);
            loadingImage.setVisible(false);
        });

        // Tạo timeline với keyFrame và bắt đầu nó
        Timeline timeline = new Timeline(keyFrame);
        timeline.play();

         */
    }

    @Override
    public void onDictionaryLoadComplete() {
        // Cập nhật giao diện người dùng khi dữ liệu đã được tải xong
        Platform.runLater(() -> {
            loadingImage.setVisible(false);
            joinButton.setVisible(true);
        });
    }

    public void handleJoinButton(ActionEvent event)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frameGUI.fxml"));
            Parent root = loader.load();
            StageManager.showStage(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
