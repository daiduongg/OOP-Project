package uet.cs.dictionaryfx.dictionary.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.cs.dictionaryfx.dictionary.model.Dictionary;
import uet.cs.dictionaryfx.dictionary.model.DictionaryManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;



public class FavoriteController implements Initializable, BaseController {
    private final Image FAVORITE_IMAGE = new Image(getClass().getResourceAsStream("Assets/favorite.png"));
    private final Image UNFAVORITE_IMAGE = new Image(getClass().getResourceAsStream("Assets/unfavorite.png"));
    private final Image VIETNAMESE_FLAG = new Image(getClass().getResourceAsStream("Assets/vi-flag.png"));
    private final Image ENGLISH_FLAG = new Image(getClass().getResourceAsStream("Assets/en-flag.png"));
    private final Image HEART_IMAGE = new Image(getClass().getResourceAsStream("Assets/qfavorite.png"));
    private Dictionary dictionary;
    private String lastWordSearch;
    private MediaPlayer mediaPlayer;
    private boolean isWordAudioReady = false;
    @FXML
    private TextArea searchField;
    @FXML
    private VBox suggestionBox;
    @FXML
    private TextArea explainField;
    @FXML
    private MODE mode;
    @FXML
    private Button speechButton;
    @FXML
    private Button favoriteButton;
    @FXML
    private ImageView favoriteWordImage;
    @FXML
    private Button enViButton;
    @FXML
    private Button viEnButton;


    public enum MODE {
        ENGLISH,
        VIETNAMESE
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        explainField.setStyle("-fx-text-fill: rgb(0, 51, 50); -fx-font-size: 20; -fx-font-family: 'Arial';");
        searchField.setStyle("-fx-text-fill: rgb(0, 51, 50); -fx-font-size: 17; -fx-font-family: 'Arial'; -fx-background-color: white;" +
                "-fx-background-radius: 30");
        turnOnEnViMode();
        handleSuggestionBox();
    }

    public void handleEnViButton(ActionEvent event) {
        if (mode == null || mode == MODE.VIETNAMESE) {
            turnOnEnViMode();
        }
    }

    public void handleViEnButton(ActionEvent event) {
        if (mode == null || mode == MODE.ENGLISH) {
            turnOnViEnMode();
        }
    }

    @Override
    public void turnOnEnViMode() {
        enViButton.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: yellow");
        viEnButton.setStyle("-fx-background-color: rgba(0,0,0,0)");
        lastWordSearch = null;
        mode = MODE.ENGLISH;
        if (dictionary != null) {
            DictionaryManager.setViDictionary(dictionary);
        }
        searchField.setText(null);
        explainField.setText(null);
        dictionary = DictionaryManager.getEnDictionary();
        suggestionBox.getChildren().clear();
        handleSuggestionBox();
    }

    @Override
    public void turnOnViEnMode() {
        viEnButton.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: yellow");
        enViButton.setStyle("-fx-background-color: rgba(0,0,0,0)");
        lastWordSearch = null;
        mode = MODE.VIETNAMESE;
        searchField.setText(null);
        explainField.setText(null);
        if (dictionary != null) {
            DictionaryManager.setEnDictionary(dictionary);
        }
        dictionary = DictionaryManager.getViDictionary();
        suggestionBox.getChildren().clear();
        handleSuggestionBox();
    }

    public void searchAndShow(String wordName) {
        if (wordName == null) {
            String error = "Your search terms did not match any entries.";
            isWordAudioReady = false;
            return;
        }

        if (dictionary.isExistInFavoriteList(wordName)) {
            favoriteWordImage.setImage(FAVORITE_IMAGE);
        } else {
            favoriteWordImage.setImage(UNFAVORITE_IMAGE);
        }

        lastWordSearch = wordName;
        String wordData;
        wordData = dictionary.getWordData(wordName);
        if (wordData != null) {
            new Thread(() -> {
                isWordAudioReady = dictionary.isLoadedWordAudio(wordName);
            }).start();
        }

        if (wordData != null) {
            explainField.setText(wordData);
            dictionary.addHistoryWord(wordName);
        } else {
            String error = new String("Your search terms did not match any entries.");
            explainField.setText(error);
            isWordAudioReady = false;
        }

        Platform.runLater(() -> {
            handleSuggestionBox();
        });
    }

    @Override
    public void handleSuggestionBox() {
        List<String> wordslist = dictionary.getFavoriteWordList();
        ObservableList<String> suggestedKeyWords = FXCollections.observableList(wordslist);
        FilteredList<String> filteredSuggestions = new FilteredList<>(suggestedKeyWords, s -> true);
        ListView<String> suggestionListView = new ListView<>(filteredSuggestions);
        suggestionListView.setStyle("-fx-font-size: 20;\n" +
                "-fx-font-family: Arial;\n" +
                "-fx-fill: #1d2a57;");

        suggestionListView.setPrefHeight(suggestionBox.getPrefHeight());

        suggestionListView.setCellFactory(param -> new ListCell<>() {
            private final HBox graphicContainer = new HBox(5);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ImageView heartIcon = new ImageView(HEART_IMAGE);
                    heartIcon.setFitWidth(20);
                    heartIcon.setFitHeight(20);
                    Label wordLabel = new Label(item);

                    graphicContainer.getChildren().setAll(heartIcon, wordLabel);
                    setGraphic(graphicContainer);
                }
            }
        });

        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (!newValue.isEmpty()) {
                    filteredSuggestions.setPredicate(keyword ->
                            keyword.toLowerCase().startsWith(newValue.toLowerCase()));

                    if (!filteredSuggestions.isEmpty()) {
                        suggestionBox.getChildren().clear();
                        suggestionBox.getChildren().add(suggestionListView);
                    }
                } else {
                    filteredSuggestions.setPredicate(s -> true);
                    suggestionBox.getChildren().clear();
                    suggestionBox.getChildren().add(suggestionListView);
                }
            }
        }));

        suggestionListView.setOnMouseClicked(mouseEvent -> {
            String word = suggestionListView.getSelectionModel().getSelectedItem();
            searchField.setText(word);
            searchAndShow(word);
        });
    }

    public void handleSpeechButton(ActionEvent event) {
        if (lastWordSearch == null || explainField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please choose a word !");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            if (isWordAudioReady) {
                try {
                    if (mediaPlayer == null || mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.dispose();
                        }
                        String fileName;
                        if (mode == MODE.ENGLISH) {
                            fileName = "en-word-audio.mp3";
                        } else {
                            fileName = "vi-word-audio.wav";
                        }
                        File file = new File(fileName);
                        String mediaPath = file.toURI().toURL().toString();
                        System.out.println(mediaPath);
                        Media media = new Media(mediaPath);
                        mediaPlayer = new MediaPlayer(media);
                        mediaPlayer.play();
                        mediaPlayer.setOnEndOfMedia(() -> {
                            System.out.println("End of media");
                            mediaPlayer.stop();
                            //mediaPlayer.dispose();
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void handleFavoriteButton(ActionEvent event) {
        if (lastWordSearch == null || explainField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please choose a word !");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            if (dictionary.addFavoriteWord(lastWordSearch)) {
                favoriteWordImage.setImage(FAVORITE_IMAGE);
            } else {
                favoriteWordImage.setImage(UNFAVORITE_IMAGE);
                dictionary.removeFavoriteWord(lastWordSearch);
            }
        }
    }
}
