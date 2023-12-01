package uet.cs.dictionaryfx.game.gui;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void showStage(Scene scene) {
        //primaryStage.setScene(null);
        //primaryStage.close();
        //primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /*
    public static void showStage(Scene scene) {
        primaryStage.setTitle("Game");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                // Thực hiện công việc nặng nhọc tại đây (nếu có)
                // Ví dụ: load dữ liệu, xử lý logic phức tạp, ...

                return null;
            }
        };

        task.setOnSucceeded(event -> {
            // Cập nhật UI trên luồng chính
            primaryStage.setScene(scene);
            primaryStage.show();
        });

        new Thread(task).start();
    }

     */
}
