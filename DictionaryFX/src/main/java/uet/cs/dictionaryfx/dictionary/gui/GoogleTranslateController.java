package uet.cs.dictionaryfx.dictionary.gui;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import uet.cs.dictionaryfx.dictionary.model.GoogleTranslateAPI;

import java.net.URL;
import java.util.ResourceBundle;

public class GoogleTranslateController implements Initializable {
    @FXML
    private ImageView inputFlagImage;
    @FXML
    private ImageView outputFlagImage;
    @FXML
    private Label inputLabel;
    @FXML
    private Label outputLabel;
    @FXML
    private Button convertButton;
    @FXML
    private TextArea inputField;
    @FXML
    private TextArea outputField;
    private PauseTransition pause;
    private MODE mode;

    private enum MODE {
        ENGLISH,
        VIETNAMESE
    }

    private final Image VIETNAMESE_FLAG = new Image(getClass().getResourceAsStream("Assets/vi-flag.png"));
    private final Image ENGLISH_FLAG = new Image(getClass().getResourceAsStream("Assets/en-flag.png"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        turnOnEnglishMode();
        pause = new PauseTransition(Duration.millis(700));
        pause.setOnFinished(event -> handleInputFieldChange());
        inputField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            pause.playFromStart();
        }));
    }

    private void turnOnEnglishMode() {
        mode = MODE.ENGLISH;
        inputFlagImage.setImage(ENGLISH_FLAG);
        inputLabel.setText("English");
        outputLabel.setText("Vietnamese");
        outputFlagImage.setImage(VIETNAMESE_FLAG);
    }

    private void turnOnVietnameseMode() {
        mode = MODE.VIETNAMESE;
        inputFlagImage.setImage(VIETNAMESE_FLAG);
        inputLabel.setText("Vietnamese");
        outputLabel.setText("English");
        outputFlagImage.setImage(ENGLISH_FLAG);
    }

    public void handleConvertButton(ActionEvent event ) {
        if (mode == MODE.ENGLISH) {
           turnOnVietnameseMode();
        } else {
            turnOnEnglishMode();
        }
    }

    private void handleInputFieldChange() {
        new Thread(() -> {
            String output;
            if (mode == MODE.ENGLISH) {
                output = GoogleTranslateAPI.translate("en", "vi", inputField.getText());
            } else {
                output = GoogleTranslateAPI.translate("vi", "en", inputField.getText());
            }
            outputField.setText(output);
        }).start();

    }
}
