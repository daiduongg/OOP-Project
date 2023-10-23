import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class Trie {
    public static final String LOWER_CASE_ALPHABET = "aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz -";
    public static final String UPPER_CASE_ALPHABET = "AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ -";

    public class Node {
        Map<Character, Node> children;
        boolean isEndOfWord;
        String word_data;

        Node() {
            children = new HashMap<>();
            isEndOfWord = false;
            word_data = null;
        }
    }

    private Node root;

    private int charToIndex(char c) {
        return LOWER_CASE_ALPHABET.indexOf(c);
    }

    private char indexToChar(int index) {
        return UPPER_CASE_ALPHABET.charAt(index);
    }

    private String toLowerCase(String input) {
        StringBuilder result = new StringBuilder(input);

        for (int i = 0; i < input.length(); i++) {
            char cur = input.charAt(i);
            int index = UPPER_CASE_ALPHABET.indexOf(cur);
            if (index != -1) {
                result.setCharAt(i, LOWER_CASE_ALPHABET.charAt(index));
            }
        }

        return result.toString();
    }

    private Node search(String word_name) {
        word_name = toLowerCase(word_name);
        Node cur = root;
        for (char c : word_name.toCharArray()) {
            if (!cur.children.containsKey(c)) {
                return null;
            }
            cur = cur.children.get(c);
        }
        return cur;
    }


    public Trie() {
        root = new Node();
    }

    public void insert(String word_name, String word_data) {
        word_name = toLowerCase(word_name);
        word_data = toLowerCase(word_data);
        Node cur = root;
        for (char c : word_name.toCharArray()) {
            cur.children.putIfAbsent(c, new Node());
            cur = cur.children.get(c);
        }
        cur.isEndOfWord = true;
        cur.word_data = word_data;
    }

    public boolean containsWord(String word_name) {
        Node cur = search(word_name);
        if (cur == null) return false;
        return search(word_name).isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return search(prefix) != null;
    }

    private boolean delete(Node cur, String word_name, int index) {
        if (index == word_name.length()) {
            if (!cur.isEndOfWord) {
                return false;
            }
            cur.isEndOfWord = false;
            return cur.children.isEmpty();
        }

        char c = word_name.charAt(index);
        if (!cur.children.containsKey(c)) {
            return false;
        }

        boolean shouldDelete = delete(cur.children.get(c), word_name, index + 1);

        if (shouldDelete) {
            cur.children.remove(c);
            return cur.children.isEmpty();
        }

        return false;
    }

    public void delete(String word_name) {
        word_name = toLowerCase(word_name);
        delete(root, word_name, 0);
    }

    public String getWordData(String word_name) {
        word_name = toLowerCase(word_name);
        Node cur = search(word_name);
        if (cur != null) {
            return cur.word_data;
        }
        return null;
    }

    //inorder traversal
    private void getSortedWords(Node cur, String cur_word, List<Word> sortedWords) {
        if (cur == null) {
            return;
        }

        if (cur.isEndOfWord) {
            sortedWords.add(new Word(cur_word, cur.word_data));
        }

        for (char c : LOWER_CASE_ALPHABET.toCharArray()) {
            if (cur.children.get(c) != null) {
                getSortedWords(cur.children.get(c), cur_word + c, sortedWords);
            }
        }
    }

    public List<Word> getAllWords() {
        List<Word> sortedWords = new ArrayList<>();
        getSortedWords(root, "", sortedWords);
        return sortedWords;
    }

    public List<Word> getWordsHasPrefix(String prefix) {
        prefix = toLowerCase(prefix);
        Node cur = search(prefix);
        List<Word> sortedWords = new ArrayList<>();
        if (cur != null) {
            getSortedWords(cur, prefix, sortedWords);
        }
        return sortedWords;
    }

    public boolean editWord(String word_name, String word_data) {
        word_name = word_name.toLowerCase();
        word_data = toLowerCase(word_data);
        Node cur = search(word_name);
        if (cur != null) {
            cur.word_data = word_data;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Trie wordTrie = new Trie();
        wordTrie.insert("apple", "a fruit");
    }
}
