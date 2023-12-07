package uet.cs.dictionaryfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uet.cs.dictionaryfx.dictionary.gui.MenuController;
import uet.cs.dictionaryfx.dictionary.gui.SearchController;

public class DictionaryApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("menuGUI.fxml"));
        Parent root = loader.load();
        primaryStage.setResizable(false);
        primaryStage.setTitle("Đại Tá");
        primaryStage.getIcons().add(new Image(SearchController.class.getResourceAsStream("Assets/book.png")));
        StageManager.setPrimaryStage(primaryStage);
        StageManager.showStage(new Scene(root));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

