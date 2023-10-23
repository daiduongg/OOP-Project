import java.util.List;

public class DictionaryDataBase {
    private Trie wordsTrie;

    public DictionaryDataBase() {
        wordsTrie = new Trie();
    }

    public Trie getDictionary() {
        return wordsTrie;
    }

    public void setDictionary(Trie wordsTree) {
        this.wordsTrie = wordsTree;
    }

    public boolean insert(String word_name, String word_data) {
        try {
            if (wordsTrie.containsWord(word_name)) {
                throw new Exception("This word already exists in the dictionary: " + word_name);
            } else {
                wordsTrie.insert(word_name, word_data);
                //System.out.println("\"" + word_target + "\" has been added!");
                return true;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean removeWord(String word_name) {
        try {
            if (!wordsTrie.containsWord(word_name)) {
                throw new Exception("This word does not exist in the dictionary!");
            } else {
                wordsTrie.delete(word_name);
                System.out.println("\"" + word_name + "\" has been removed!");
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

    public boolean editWordData(String word_name, String new_word_data) {
        return wordsTrie.editWord(word_name, new_word_data);
    }

    public String getWordData(String word_name) {
        return wordsTrie.getWordData(word_name);
    }

    public static void main(String[] args) {
        DictionaryDataBase dictionaryDataBase = new DictionaryDataBase();
        dictionaryDataBase.insert("Â", "a fruit");
        dictionaryDataBase.insert("Dương ơi là dương", "a yellow fruit");
        dictionaryDataBase.insert("Ă", "an electronic device");
        dictionaryDataBase.insert("duong", "the process of writing code");
        dictionaryDataBase.insert("đ", "a means of communication");
        dictionaryDataBase.insert("BOOK", "a written or printed work");
        dictionaryDataBase.insert("coffee", "a popular beverage");
        dictionaryDataBase.insert("island", "a piece of land surrounded by water");
        dictionaryDataBase.insert("sun", "the star at the center of our solar system");
        dictionaryDataBase.insert("flower", "a colorful plant part");
        dictionaryDataBase.insert("tree", "a tall plant with branches and leaves");
        dictionaryDataBase.insert("truck", "a motor vehicle for transporting goods");
        dictionaryDataBase.insert("train", "a connected series of railroad cars");
        dictionaryDataBase.insert("triangle", "a polygon with three sides");
        dictionaryDataBase.insert("true", "in accordance with fact or reality");
        dictionaryDataBase.insert("try", "to make an attempt");
        dictionaryDataBase.insert("trophy", "a prize or award");
        dictionaryDataBase.insert("trouble", "problems or difficulties");
        dictionaryDataBase.insert("track", "a path or course for movement");
        dictionaryDataBase.insert("transparent", "able to be seen through");
        //dictionaryDataBase.removeWord("Dương ơi là dương");
        System.out.println(dictionaryDataBase.getWordData("DUONG"));

        //for (Word word : dictionaryDataBase.getWordsHasPrefix("")) {
          //  System.out.println(word.getWord_target() + "\t" + word.getWord_explain());
        //}
    }
}
