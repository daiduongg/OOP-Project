module game.gamefxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires com.almasb.fxgl.all;

    opens game.pirate.gui to javafx.fxml;
    exports game.pirate.gui;
}