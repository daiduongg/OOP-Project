package game.pirate.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void showStage(Scene scene) {
        primaryStage.setTitle("Game");
        primaryStage.setScene(null);
        //primaryStage.close();
        //primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
