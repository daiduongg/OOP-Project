package uet.cs.dictionaryfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import uet.cs.dictionaryfx.dictionary.gui.ChatBotController;
import uet.cs.dictionaryfx.dictionary.gui.FavoriteController;
import uet.cs.dictionaryfx.dictionary.gui.GoogleTranslateController;
import uet.cs.dictionaryfx.dictionary.gui.SearchController;
import uet.cs.dictionaryfx.game.gui.GameController;
import uet.cs.dictionaryfx.game.gui.MenuGameController;

import java.io.*;

public class SceneManager {
    private static Parent rootSearch;
    private static StackPane rootGame;
    private static Parent rootFrame;
    private static Parent rootGameMenu;
    private static Parent rootGoogleTranslate;
    private static Parent rootChatBot;
    private static Parent rootFavorite;


    public static Parent getRootSearch() {
        return rootSearch;
    }

    public static void setRootSearch(Parent rootSearch) {
        SceneManager.rootSearch = rootSearch;
    }

    public static Parent getRootFrame() {
        return rootFrame;
    }

    public static void setRootGame(StackPane rootGame) {
        SceneManager.rootGame = rootGame;
    }

    public static Parent getRootGame() {
        StackPane copy = new StackPane();
        copy.getChildren().addAll(rootGame.getChildren());
        return copy;
    }

    public static void setRootFrame(Parent rootFrame) {
        SceneManager.rootFrame = rootFrame;
    }

    public static Parent getRootGameMenu() {
        return rootGameMenu;
    }

    public static void setRootGameMenu(Parent rootGameMenu) {
        SceneManager.rootGameMenu = rootGameMenu;
    }

    public static Parent getRootGoogleTranslate() {
        return rootGoogleTranslate;
    }

    public static Parent getRootChatBot() {
        return rootChatBot;
    }

    public static void setRootChatBot(Parent rootChatBot) {
        SceneManager.rootChatBot = rootChatBot;
    }

    public static void setRootGoogleTranslate(Parent rootGoogleTranslate) {
        SceneManager.rootGoogleTranslate = rootGoogleTranslate;
    }

    public static Parent getRootFavorite() {
        return rootFavorite;
    }

    public static void setRootFavorite(Parent rootFavorite) {
        SceneManager.rootFavorite = rootFavorite;
    }

    public static void loadRootSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(SearchController.class.getResource("searchGUI.fxml"));
            rootSearch = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRootFrame() {
        try {
            FXMLLoader loader = new FXMLLoader(FrameController.class.getResource("frameGUI.fxml"));
            rootFrame = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRootGame() {
        try {
            FXMLLoader loader = new FXMLLoader(GameController.class.getResource("gameGUI.fxml"));
            rootGame = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRootGameMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuGameController.class.getResource("gameMenuGUI.fxml"));
            rootGameMenu = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRootGoogleTranslate() {
        try {
            FXMLLoader loader = new FXMLLoader(GoogleTranslateController.class.getResource("googleTranslateGUI.fxml"));
            rootGoogleTranslate = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRootChatBot() {
        try {
            FXMLLoader loader = new FXMLLoader(ChatBotController.class.getResource("chatBotGUI.fxml"));
            rootChatBot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRootFavorite() {
        try {
            FXMLLoader loader = new FXMLLoader(FavoriteController.class.getResource("favoriteGUI.fxml"));
            rootFavorite = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
