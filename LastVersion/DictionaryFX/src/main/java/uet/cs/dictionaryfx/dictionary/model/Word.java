package uet.cs.dictionaryfx.dictionary.model;

public class Word {
    private String wordName;
    private String wordData;

    public Word() {
        wordName = null;
        wordData = null;
    }

    public Word(String wordName, String wordData) {
        this.wordName = wordName;
        this.wordData = wordData;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordData() {
        return wordData;
    }

    public void setWordData(String wordData) {
        this.wordData = wordData;
    }
}

