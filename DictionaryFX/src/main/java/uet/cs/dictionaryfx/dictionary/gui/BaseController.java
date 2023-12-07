package uet.cs.dictionaryfx.dictionary.gui;

import javafx.event.ActionEvent;

public interface BaseController {
    void turnOnViEnMode();
    void searchAndShow(String wordName);
    void handleSuggestionBox();
    void turnOnEnViMode();
}
