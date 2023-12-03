package uet.cs.dictionaryfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import uet.cs.dictionaryfx.dictionary.gui.SearchController;
import uet.cs.dictionaryfx.game.gui.GameController;
import uet.cs.dictionaryfx.game.gui.GameMenuController;

import java.io.IOException;

public class SceneManager {
    private static Parent rootSearch;
    private static Parent rootGame;
    private static Parent rootFrame;
    private static Parent rootGameMenu;

    public static Parent getRootSearch() {
        return rootSearch;
    }

    public static void setRootSearch(Parent rootSearch) {
        SceneManager.rootSearch = rootSearch;
    }

    public static Parent getRootGame() {
        return rootGame;
    }

    public static void setRootGame(Parent rootGame) {
        SceneManager.rootGame = rootGame;
    }

    public static Parent getRootFrame() {
        return rootFrame;
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
            FXMLLoader loader = new FXMLLoader(GameMenuController.class.getResource("gameMenuGUI.fxml"));
            rootGameMenu = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
