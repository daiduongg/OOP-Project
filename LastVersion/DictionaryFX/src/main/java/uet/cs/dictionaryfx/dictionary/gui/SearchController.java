package uet.cs.dictionaryfx.dictionary.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.cs.dictionaryfx.dictionary.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private static Dictionary enViDictionary;
    private static Dictionary viEnDictionary;
    private Dictionary.MODE mode;
    @FXML
    private Button enViModeButton;
    @FXML
    private Button viEnModeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button deleteWordButton;
    @FXML
    private Button speechButton;
    @FXML
    private Button favoriteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelEditButton;
    @FXML
    private Button submitEditButton;
    @FXML
    private Text statusMode;
    @FXML
    private TextField searchField;
    @FXML
    private TextArea explainBox;
    @FXML
    private VBox suggestionBox;
    private boolean isWordAudioReady;
    private MediaPlayer mediaPlayer;
    private String lastWordSearch;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isWordAudioReady = false;
        explainBox.setStyle("-fx-text-fill: rgb(0, 51, 50); -fx-font-size: 23; -fx-font-family: 'Arial';");
        enViDictionary = DictionaryManager.getEnDictionary();
        viEnDictionary = DictionaryManager.getViDictionary();
        cancelEditButton.setVisible(false);
        submitEditButton.setVisible(false);
        turnOnEnViMode();
        handleSuggestionBox();
        checkMouseClicked();
    }

    public void turnOnEnViMode() {
        statusMode.setText("| English");
        enViModeButton.setUnderline(true);
        viEnModeButton.setUnderline(false);
        mode = Dictionary.MODE.ENGLISH;
    }

    public void turnOnViEnMode() {
        statusMode.setText("| Vietnamese");
        enViModeButton.setUnderline(false);
        viEnModeButton.setUnderline(true);
        mode = Dictionary.MODE.VIETNAMESE;
    }
    public void handleEnViButton(ActionEvent event) {
        turnOnEnViMode();
    }

    public void handleViEnButton(ActionEvent event) {
        turnOnViEnMode();
    }

    public void checkMouseClicked() {
        searchField.setOnMouseExited(event -> {
            searchField.getScene().setOnMouseClicked(subevent -> {
                suggestionBox.setVisible(false);
            });
        });
    }

    public void searchAndShow(String wordName) {
        lastWordSearch = wordName;
        String wordData;
        if (mode == Dictionary.MODE.ENGLISH) {
            wordData = enViDictionary.getWordData(wordName);
            if (wordData != null) {
                //isWordAudioReady = enViDictionary.isLoadedWordAudio(wordName);
                new Thread(() -> {
                    isWordAudioReady = enViDictionary.isLoadedWordAudio(wordName);
                    // You can perform additional actions after loading audio if needed
                }).start();
            }
        } else {
            wordData = viEnDictionary.getWordData(wordName);
        }
        if (wordData != null) {
            explainBox.setText(wordData);
        } else {
            String error = new String("Your search terms did not match any entries.");
            explainBox.setText(error);
            isWordAudioReady = false;
        }
        suggestionBox.setVisible(false);
    }

    public void handleSearchButton(ActionEvent event) {
        String wordName = searchField.getText();
        if (wordName != null) {
            searchAndShow(wordName);
        }
    }

    public void handleEnterSearchField(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String wordName = searchField.getText();
            if (wordName != null) {
                searchAndShow(wordName);
            }
        }
    }

    public void handleSuggestionBox() {
        List<String> wordslist = enViDictionary.getAllWords();
        ObservableList<String> suggestedKeyWords = FXCollections.observableList(wordslist);
        FilteredList<String> filteredSuggestions = new FilteredList<>(suggestedKeyWords, s -> true);
        ListView<String> suggestionListView = new ListView<>(filteredSuggestions);
        suggestionListView.setStyle("-fx-font-size: 20;\n" +
                                    "-fx-font-family: Arial;\n" +
                                    "-fx-fill: #1d2a57;");
        suggestionBox.setVisible(false);
        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                filteredSuggestions.setPredicate(keyword ->
                        keyword.toLowerCase().startsWith(newValue.toLowerCase()));

                if (!filteredSuggestions.isEmpty()) {
                    suggestionBox.getChildren().clear();
                    suggestionBox.getChildren().add(suggestionListView);
                    suggestionBox.setVisible(true);
                } else {
                    suggestionBox.setVisible(false);
                }
            } else {
                suggestionBox.setVisible(false);
            }
        }));

        suggestionListView.setOnMouseClicked(mouseEvent -> {
            String word = suggestionListView.getSelectionModel().getSelectedItem();
            searchField.setText(word);
            searchAndShow(word);
        });
    }

    public void handleSpeechButton(ActionEvent event) {
        if (!explainBox.getText().isEmpty()) {
            if (mode == Dictionary.MODE.ENGLISH) {
                if (isWordAudioReady) {
                    try {
                        if (mediaPlayer == null || mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                            File file = new File("word-audio.mp3");
                            String mediaPath = file.toURI().toURL().toString();
                            System.out.println(mediaPath);
                            Media media = new Media(mediaPath);
                            MediaPlayer mediaPlayer = new MediaPlayer(media);
                            mediaPlayer.play();
                            mediaPlayer.setOnEndOfMedia(() -> {
                                System.out.println("End of media");
                                mediaPlayer.stop();
                            });
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else if (lastWordSearch != null) {
                    enViDictionary.wordSpeech(lastWordSearch);
                }
            }
        }
    }

    public static void close() {
        DictionaryManager.setEnDictionary(enViDictionary);
        DictionaryManager.setViDictionary(viEnDictionary);
    }
}
