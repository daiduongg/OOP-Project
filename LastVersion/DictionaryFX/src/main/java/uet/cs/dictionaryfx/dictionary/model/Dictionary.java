package uet.cs.dictionaryfx.dictionary.model;

import java.io.IOException;
import java.util.*;

public class Dictionary {
    private Trie wordsTrie;
    private DictionaryLoader dictionaryLoader;
    private Set<String> historyWord = new LinkedHashSet<>();
    private Set<String> favoriteWord = new TreeSet<>();
    private MODE mode;

    public static enum MODE {
        ENGLISH,
        VIETNAMESE
    }

    public Dictionary(MODE mode_) {
        mode = mode_;
        wordsTrie = new Trie();
        dictionaryLoader = new DictionaryLoader(this);
        if (mode == MODE.ENGLISH) {
            dictionaryLoader.loadEnViFromDB();
            dictionaryLoader.loadEnViFromFile();
        } else if (mode == MODE.VIETNAMESE) {
            dictionaryLoader.loadViEnFromDB();
            dictionaryLoader.loadViEnFromFile();

        } else {
            System.out.println("error syntax");
        }
    }

    public List<String> getAllWords() {
        List<String> list = new ArrayList<>();
        for (Word word : wordsTrie.getAllWords()) {
            list.add(word.getWordName());
        }
        return list;
    }

    public List<Word> getWordsHasPrefix(String prefix) {
        return wordsTrie.getWordsHasPrefix(prefix);
    }

    public String getWordData(String wordName) {
        return wordsTrie.getWordData(wordName);
    }

    public boolean isLoadedWordAudio(String word) {
        try {
            if (mode == MODE.ENGLISH) {
                String url = dictionaryLoader.getEnAudioURL(word);
                return dictionaryLoader.downloadEnAudio(url);
            } else {
                return dictionaryLoader.downloadViAudio(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getHistoryWordList() {
        List<String> result = new ArrayList<>();
        for (String word : historyWord) {
            result.add(word);
        }
        return result;
    }

    public List<String> getFavoriteWordList() {
        List<String> result = new ArrayList<>();
        for (String word : favoriteWord) {
            result.add(word);
        }
        return result;
    }

    public void addFavoriteWordFromDB(String wordName) {
        favoriteWord.add(wordName);
    }

    public boolean isExistInFavoriteList(String wordName) {;
        return favoriteWord.contains(wordName);
    }
    public boolean addFavoriteWord(String wordName) {
        if (isExistInFavoriteList(wordName)) {
            return false;
        }

        if (mode == MODE.ENGLISH) {
            dictionaryLoader.insertEnViWordToFavoriteDB(wordName);
        } else {
            dictionaryLoader.insertViEnWordToFavoriteDB(wordName);
        }

        favoriteWord.add(wordName);
        return true;
    }

    public boolean isExistInHistoryList(String wordName) {
        return historyWord.contains(wordName);
    }

    public void addHistoryWord(String wordName) {
        if (wordName != null && wordsTrie.containsWord(wordName)) {
            if (historyWord.contains(wordName)) {
                historyWord.remove(wordName);
            } else {
                if (mode == MODE.ENGLISH) {
                    dictionaryLoader.insertEnViWordToHistoryDB(wordName);
                } else {
                    dictionaryLoader.insertViEnWordToHistoryDB(wordName);
                }
            }
            historyWord.add(wordName);
        }
    }

    public void addHistoryWordFromDB(String wordName) {
        historyWord.add(wordName);
    }

    public void removeFavoriteWord(String wordName) {
        if (favoriteWord.contains(wordName)) {
            favoriteWord.remove(wordName);
        } else {
            return;
        }

        if (mode == MODE.ENGLISH) {
            dictionaryLoader.removeEnViWordFromFavoriteDB(wordName);
        } else {
            dictionaryLoader.removeViEnWordFromFavoriteDB(wordName);
        }
    }

    public void removeHistoryWord(String wordName) {
        if (historyWord.contains(wordName)) {
            historyWord.remove(wordName);
            if (mode == MODE.ENGLISH) {
                dictionaryLoader.removeEnViWordFromHistoryDB(wordName);
            } else {
                dictionaryLoader.removeViEnWordFromHistoryDB(wordName);
            }
        }
    }

    public boolean editWordData(String wordName, String new_wordData) {
        if (mode == MODE.ENGLISH) {
            dictionaryLoader.updateEnViWordFromDB(wordName, new_wordData);
        } else {
            dictionaryLoader.updateViEnWordFromDB(wordName, new_wordData);
        }

        return wordsTrie.editWord(wordName, new_wordData);
    }

    public boolean insertWord(String wordName, String wordData) {
        if (wordName == null || wordData == null) {
            return false;
        }

        try {
            if (wordsTrie.containsWord(wordName)) {
                throw new Exception("This word already exists in the dictionary: " + wordName);
            }

            if (mode == MODE.ENGLISH) {
                dictionaryLoader.insertEnViWordToDB(new Word(wordName, wordData));
            } else {
                dictionaryLoader.insertViEnWordToDB(new Word(wordName, wordData));
            }

            wordsTrie.insert(wordName, wordData);
            //System.out.println("/" + wordName + "/" + " has been inserted from file!");
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean insertWordFromDB(String wordName, String wordData) {
        if (wordName == null || wordData == null) {
            return false;
        }

        try {
            if (wordsTrie.containsWord(wordName)) {
                //throw new Exception("This word already exists in the dictionary: " + wordName);
            } else {
                wordsTrie.insert(wordName, wordData);
                //System.out.println("/" + wordName + "/" + " has been inserted from file!");
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
            if (mode == MODE.ENGLISH) {
                dictionaryLoader.removeEnViWordFromDB(wordName);
                dictionaryLoader.removeEnViWordFromFavoriteDB(wordName);
                dictionaryLoader.removeEnViWordFromHistoryDB(wordName);
            } else {
                dictionaryLoader.removeViEnWordFromDB(wordName);
                dictionaryLoader.removeViEnWordFromFavoriteDB(wordName);
                dictionaryLoader.removeEnViWordFromHistoryDB(wordName);
            }
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
}
