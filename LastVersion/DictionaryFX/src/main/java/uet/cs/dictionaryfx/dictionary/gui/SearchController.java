package uet.cs.dictionaryfx.dictionary.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import uet.cs.dictionaryfx.dictionary.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchController implements Initializable {
    private Dictionary dictionary;
    private Dictionary.MODE mode;
    @FXML
    private Button enViModeButton;
    @FXML
    private Button viEnModeButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearSearchButton;
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
    private TextArea explainField;
    @FXML
    private VBox suggestionBox;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Pane darkPaneTop;
    @FXML
    private Pane darkPaneCenter;
    @FXML
    private Pane addWordPane;
    @FXML
    private Button cancelAddButton;
    @FXML
    private Button submitAddButton;
    @FXML
    private TextArea wordNameAddField;
    @FXML
    private TextArea wordDataAddField;
    @FXML
    private ImageView favoriteWordImage;
    @FXML
    private Button historyRemoveButton;
    private boolean isWordAudioReady;
    private MediaPlayer mediaPlayer;
    private String lastWordSearch;
    private final Image FAVORITE_IMAGE = new Image(getClass().getResourceAsStream("Assets/favorite.png"));
    private final Image UNFAVORITE_IMAGE = new Image(getClass().getResourceAsStream("Assets/unfavorite.png"));
    private final Image HISTORY_IMAGE = new Image(getClass().getResourceAsStream("Assets/history1.png"));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isWordAudioReady = false;
        explainField.setStyle("-fx-text-fill: rgb(0, 51, 50); -fx-font-size: 23; -fx-font-family: 'Arial';");
        cancelEditButton.setVisible(false);
        submitEditButton.setVisible(false);
        turnOnEnViMode();
        checkMouseClicked();
    }

    public void turnOnEnViMode() {
        lastWordSearch = null;
        statusMode.setText("| English");
        enViModeButton.setUnderline(true);
        viEnModeButton.setUnderline(false);
        mode = Dictionary.MODE.ENGLISH;
        if (dictionary != null) {
            DictionaryManager.setViDictionary(dictionary);
        }
        searchField.setText(null);
        explainField.setText(null);
        dictionary = DictionaryManager.getEnDictionary();
        handleSuggestionBox();
    }

    public void turnOnViEnMode() {
        lastWordSearch = null;
        statusMode.setText("| Vietnamese");
        enViModeButton.setUnderline(false);
        viEnModeButton.setUnderline(true);
        mode = Dictionary.MODE.VIETNAMESE;
        searchField.setText(null);
        explainField.setText(null);
        if (dictionary != null) {
            DictionaryManager.setEnDictionary(dictionary);
        }
        dictionary = DictionaryManager.getViDictionary();
        handleSuggestionBox();
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
        explainField.setOnMouseClicked(mouseEvent -> {
            suggestionBox.setVisible(false);
        });
        searchField.setOnMouseClicked(mouseEvent -> {
            suggestionBox.setVisible(true);
        });
    }

    public void searchAndShow(String wordName) {
        if (wordName == null) {
            String error = new String("Your search terms did not match any entries.");
            explainField.setText(error);
            isWordAudioReady = false;
            lastWordSearch = null;
            return;
        }

        if (dictionary.isExistInFavoriteList(wordName)) {
            favoriteWordImage.setImage(FAVORITE_IMAGE);
        } else {
            favoriteWordImage.setImage(UNFAVORITE_IMAGE);
        }

        if (dictionary.isExistInHistoryList(wordName)) {
            historyRemoveButton.setVisible(true);
        } else {
            historyRemoveButton.setVisible(false);
        }

        lastWordSearch = wordName;
        String wordData;
        wordData = dictionary.getWordData(wordName);
        if (wordData != null) {
            //isWordAudioReady = enViDictionary.isLoadedWordAudio(wordName);
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
        suggestionBox.setVisible(false);
        new Thread(() -> {
            handleSuggestionBox();
        }).start();
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
/*
    public void handleSuggestionBox() {
        List<String> wordslist = dictionary.getAllWords();
        List<String> historyWordList = dictionary.getHistoryWordList();
        ObservableList<String> suggestedKeyWords = FXCollections.observableList(wordslist);
        FilteredList<String> filteredSuggestions = new FilteredList<>(suggestedKeyWords, s -> true);
        ListView<String> suggestionListView = new ListView<>(filteredSuggestions);
        suggestionListView.setStyle("-fx-font-size: 20;\n" +
                                    "-fx-font-family: Arial;\n" +
                                    "-fx-fill: #1d2a57;");
        suggestionBox.setVisible(false);
        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
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
                    //suggestionBox.setVisible(false);
                    filteredSuggestions.setPredicate(keyword ->
                            historyWordList.isEmpty() || historyWordList.contains(keyword.toLowerCase()));
                }
            }
        }));

        suggestionListView.setOnMouseClicked(mouseEvent -> {
            String word = suggestionListView.getSelectionModel().getSelectedItem();
            searchField.setText(word);
            searchAndShow(word);
        });
    }

 */

    public void handleSuggestionBox() {
        List<String> wordslist = dictionary.getAllWords();
        List<String> historyWordList = dictionary.getHistoryWordList();
        ObservableList<String> suggestedKeyWords = FXCollections.observableList(wordslist);
        FilteredList<String> filteredSuggestions = new FilteredList<>(suggestedKeyWords, s -> true);
        ListView<String> suggestionListView = new ListView<>(filteredSuggestions);
        suggestionListView.setStyle("-fx-font-size: 20;\n" +
                "-fx-font-family: Arial;\n" +
                "-fx-fill: #1d2a57;");
        suggestionBox.setVisible(false);

        suggestionListView.setCellFactory(param -> new ListCell<>() {
            private final HBox graphicContainer = new HBox(5);

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ImageView historyIcon = new ImageView(dictionary.isExistInHistoryList(item) ? HISTORY_IMAGE : null);
                    historyIcon.setFitWidth(20);
                    historyIcon.setFitHeight(20);
                    Label wordLabel = new Label(item);

                    graphicContainer.getChildren().setAll(historyIcon, wordLabel);
                    setGraphic(graphicContainer);
                }
            }
        });

        filteredSuggestions.setPredicate(keyword ->
                historyWordList.isEmpty() || historyWordList.contains(keyword.toLowerCase()));
        suggestionBox.getChildren().clear();
        suggestionBox.getChildren().add(suggestionListView);
        searchField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
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
                    //suggestionBox.setVisible(false);
                    filteredSuggestions.setPredicate(keyword ->
                            historyWordList.isEmpty() || historyWordList.contains(keyword.toLowerCase()));
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
                        if (mode == Dictionary.MODE.ENGLISH) {
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

    public void handleClearWordButton(ActionEvent event) {
        searchField.setText(null);
        suggestionBox.setVisible(false);
    }

    public void handleEditButton(ActionEvent event) {
        if (lastWordSearch == null || explainField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please choose a word !");
            Optional<ButtonType> result = alert.showAndWait();
        } else if (!explainField.getText().isEmpty()) {
            submitEditButton.setVisible(true);
            cancelEditButton.setVisible(true);
            speechButton.setVisible(false);
            favoriteButton.setVisible(false);
            deleteButton.setVisible(false);
            addButton.setVisible(false);
            darkPaneTop.setVisible(true);
            explainField.setEditable(true);
        }
    }

    public void handleCancelEditButton(ActionEvent event) {
        String wordData = dictionary.getWordData(lastWordSearch);
        String newWordData = explainField.getText();
        if (!wordData.equals(newWordData)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Your changes will not be saved");
            alert.setContentText("Do you want to continue canceling?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                explainField.setText(wordData);
                submitEditButton.setVisible(false);
                cancelEditButton.setVisible(false);
                speechButton.setVisible(true);
                favoriteButton.setVisible(true);
                deleteButton.setVisible(true);
                addButton.setVisible(true);
                darkPaneTop.setVisible(false);
                explainField.setEditable(false);
            }
        }
    }

    public void handleSubmitEditButton(ActionEvent event) {
        String wordData = dictionary.getWordData(lastWordSearch);
        String newWordData = explainField.getText();
        if (!wordData.equals(newWordData)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Update successful !");
            Optional<ButtonType> result = alert.showAndWait();
        }
        dictionary.editWordData(lastWordSearch, newWordData);
        submitEditButton.setVisible(false);
        cancelEditButton.setVisible(false);
        speechButton.setVisible(true);
        favoriteButton.setVisible(true);
        deleteButton.setVisible(true);
        addButton.setVisible(true);
        darkPaneTop.setVisible(false);
        explainField.setEditable(false);
    }

    public void handleAddButton(ActionEvent event) {
        darkPaneCenter.setVisible(true);
        darkPaneTop.setVisible(true);
        addWordPane.setVisible(true);
    }

    public void handleCancelAddButton(ActionEvent event) {
        String wordName = wordNameAddField.getText();
        String wordData = wordDataAddField.getText();

        if (!wordData.equals("") || !wordName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Your changes will not be saved");
            alert.setContentText("Do you want to continue canceling?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                wordNameAddField.setText("");
                wordDataAddField.setText("");
                darkPaneTop.setVisible(false);
                darkPaneCenter.setVisible(false);
                addWordPane.setVisible(false);
            }
        } else {
            wordNameAddField.setText("");
            wordDataAddField.setText("");
            darkPaneTop.setVisible(false);
            darkPaneCenter.setVisible(false);
            addWordPane.setVisible(false);
        }
    }

    public void handleSubmitAddButton(ActionEvent event) {
        String wordName = wordNameAddField.getText();
        String wordData = wordDataAddField.getText();
        if (wordName.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Word cannot be empty !");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if (wordData.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Explanation cannot be empty !");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        if (!dictionary.insertWord(wordName, wordData)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("This word already exists in the dictionary !");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Update successful !");
        Optional<ButtonType> result = alert.showAndWait();

        new Thread(() -> {
            handleSuggestionBox();
        }).start();

        wordNameAddField.setText("");
        wordDataAddField.setText("");
        darkPaneTop.setVisible(false);
        darkPaneCenter.setVisible(false);
        addWordPane.setVisible(false);
    }

    public void handleDeleteWordButton(ActionEvent event) {
        if (lastWordSearch == null || explainField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please choose a word !");
            Optional<ButtonType> result = alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            alert.setHeaderText("This action cannot be undone");
            alert.setContentText("Do you want to continue ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                dictionary.removeWord(lastWordSearch);
                new Thread(() -> {
                    handleSuggestionBox();
                }).start();
                searchField.setText("");
                explainField.setText("");
            }
        }
    }

    public void handleHistoryRemoveButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setHeaderText("This action cannot be undone");
        alert.setContentText("Do you want to continue ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dictionary.removeHistoryWord(lastWordSearch);
            new Thread(() -> {
                handleSuggestionBox();
            }).start();
            historyRemoveButton.setVisible(false);
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
