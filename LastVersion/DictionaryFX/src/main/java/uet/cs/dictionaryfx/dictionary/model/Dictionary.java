package uet.cs.dictionaryfx.dictionary.model;

import java.util.List;

public class Dictionary {
    private Trie wordsTrie;

    public Dictionary() {
        wordsTrie = new Trie();
    }

    public boolean insert(String wordName, String wordData) {
        if (wordName == null || wordData == null) {
            return false;
        }

        try {
            if (wordsTrie.containsWord(wordName)) {
                throw new Exception("This word already exists in the dictionary: " + wordName);
            } else {
                wordsTrie.insert(wordName, wordData);
                System.out.println("/" + wordName + "/" + " has been inserted from file!");
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean removeWord(String wordName) {
        if (wordName == null) {
            return false;
        }
        try {
            if (!wordsTrie.containsWord(wordName)) {
                throw new Exception("This word does not exist in the dictionary!");
            } else {
                wordsTrie.delete(wordName);
                System.out.println("/" + wordName + "/" + " has been removed!");
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public List<Word> getAllWords() {
        return wordsTrie.getAllWords();
    }

    public List<Word> getWordsHasPrefix(String prefix) {
        return wordsTrie.getWordsHasPrefix(prefix);
    }

    public boolean editWordData(String wordName, String new_wordData) {
        return wordsTrie.editWord(wordName, new_wordData);
    }

    public String getWordData(String wordName) {
        return wordsTrie.getWordData(wordName);
    }
}
