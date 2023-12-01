package uet.cs.dictionaryfx.dictionary.model;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private Trie wordsTrie;
    private DictionaryLoader dictionaryLoader;
    private TextToSpeech tts;
    private List<DictionaryLoadListener> loadListeners = new ArrayList<>();


    private DictionaryLoadListener listener;

    public static enum MODE {
        ENGLISH,
        VIETNAMESE
    }

    public interface DictionaryLoadListener {
        void onDictionaryLoadComplete();
    }

    public Dictionary(MODE mode) {
        tts = new TextToSpeech();
        wordsTrie = new Trie();
        dictionaryLoader = new DictionaryLoader(this);
        if (mode == MODE.ENGLISH) {
            new Thread(() -> {
                dictionaryLoader.loadEnViFromDB();
                notifyLoadComplete();
            }).start();
            new Thread(() -> {
                dictionaryLoader.loadEnViFromFile();
            }).start();
        } else if (mode == MODE.VIETNAMESE) {
            new Thread(() -> {
                dictionaryLoader.loadViEnFromDB();
            }).start();
            new Thread(() -> {
                dictionaryLoader.loadViEnFromFile();
                notifyLoadComplete();
            }).start();
        } else {
            System.out.println("error syntax");
        }
    }

    private void notifyLoadComplete() {
        for (DictionaryLoadListener listener : loadListeners) {
            listener.onDictionaryLoadComplete();
        }
    }

    public void addLoadListener(DictionaryLoadListener listener) {
        loadListeners.add(listener);
    }

    public void removeLoadListener(DictionaryLoadListener listener) {
        loadListeners.remove(listener);
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

    public boolean editWordData(String wordName, String new_wordData) {
        return wordsTrie.editWord(wordName, new_wordData);
    }

    public String getWordData(String wordName) {
        return wordsTrie.getWordData(wordName);
    }

    public boolean isLoadedWordAudio(String word) {
        String url = dictionaryLoader.getAudioUrl(word);
        return dictionaryLoader.downloadAudio(url, "word-audio.mp3");
    }

    public void wordSpeech(String word) {
        tts.SpeakText(word);
    }
}
