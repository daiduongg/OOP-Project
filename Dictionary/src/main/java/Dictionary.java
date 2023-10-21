import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private DictionaryTrie words;

    public Dictionary() {
        words = new DictionaryTrie();
    }

    public DictionaryTrie getDictionary() {
        return words;
    }

    public void setDictionary(DictionaryTrie wordsTree) {
        this.words = wordsTree;
    }

    private boolean isAlphabetic(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean insertWord(String word_target, String word_explain) {
        try {
            if (words.containsWord(word_target)) {
                throw new Exception("This word already exists in the dictionary!");
            } else if (word_target == null || word_explain == null
                        || word_target == "" || word_explain == ""
                        || !isAlphabetic(word_target)) {
                throw new IllegalArgumentException();
            } else {
                Word word = new Word(word_target, word_explain);
                words.insert(word);
                System.out.println("Success!");
                return true;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid word or meaning: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean removeWord(String word_target) {
        try {
            if (!words.containsWord(word_target)) {
                throw new Exception("This word does not exist in the dictionary!");
            } else {
                words.delete(word_target);
                System.out.println("Success!");
                return true;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid word: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public List<Word> getSortedWords() {
        return words.getSortedWords();
    }

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        dictionary.insertWord("Hello", "Xin chao");
        dictionary.insertWord("Bye", "Cook");
        dictionary.insertWord("Girl", "Gai");
    }
}
