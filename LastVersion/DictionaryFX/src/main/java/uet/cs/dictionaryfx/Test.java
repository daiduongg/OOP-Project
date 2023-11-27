package uet.cs.dictionaryfx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Group group = new Group();
        Label label = new Label("asdasasd");
        group.getChildren().add(label);
        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
