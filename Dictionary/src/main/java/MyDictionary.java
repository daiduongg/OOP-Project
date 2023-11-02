import java.util.List;

public class MyDictionary {
    private Trie wordsTrie;

    public MyDictionary() {
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
        MyDictionary myDictionary = new MyDictionary();
        myDictionary.insert("Â", "a fruit");
        myDictionary.insert("Dương ơi là dương", "a yellow fruit");
        myDictionary.insert("Ă", "an electronic device");
        myDictionary.insert("duong", "the process of writing code");
        myDictionary.insert("đ", "a means of communication");
        myDictionary.insert("BOOK", "a written or printed work");
        myDictionary.insert("coffee", "a popular beverage");
        myDictionary.insert("island", "a piece of land surrounded by water");
        myDictionary.insert("sun", "the star at the center of our solar system");
        myDictionary.insert("flower", "a colorful plant part");
        myDictionary.insert("tree", "a tall plant with branches and leaves");
        myDictionary.insert("truck", "a motor vehicle for transporting goods");
        myDictionary.insert("train", "a connected series of railroad cars");
        myDictionary.insert("triangle", "a polygon with three sides");
        myDictionary.insert("true", "in accordance with fact or reality");
        myDictionary.insert("try", "to make an attempt");
        myDictionary.insert("trophy", "a prize or award");
        myDictionary.insert("trouble", "problems or difficulties");
        myDictionary.insert("track", "a path or course for movement");
        myDictionary.insert("transparent", "able to be seen through");
        //myDictionary.removeWord("Dương ơi là dương");
        System.out.println(myDictionary.getWordData("DUONG"));

        //for (Word word : myDictionary.getWordsHasPrefix("")) {
          //  System.out.println(word.getWord_target() + "\t" + word.getWord_explain());
        //}
    }
}
