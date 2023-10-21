import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class DictionaryTrie {
    public class Node {
        Map<Character, Node> children;
        boolean isEndOfWord;
        String meaning;

        Node() {
            children = new HashMap<>();
            isEndOfWord = false;
            meaning = null;
        }
    }

    private Node root;

    public DictionaryTrie() {
        root = new Node();
    }

    public void insert(Word word) {
        String word_target = word.getWord_target().toLowerCase();
        String word_explain = word.getWord_explain().toLowerCase();
        Node cur = root;
        for (char c : word_target.toCharArray()) {
            cur.children.putIfAbsent(c, new Node());
            cur = cur.children.get(c);
        }
        cur.isEndOfWord = true;
        cur.meaning = word_explain;
    }

    private Node search(String word_target) {
        word_target = word_target.toLowerCase();
        Node cur = root;
        for (char c : word_target.toCharArray()) {
            if (!cur.children.containsKey(c)) {
                return null;
            }
            cur = cur.children.get(c);
        }
        return cur;
    }

    public boolean containsWord(String word_target) {
        word_target = word_target.toLowerCase();
        return search(word_target) != null;
    }

    public boolean startsWith(String prefix) {
        prefix = prefix.toLowerCase();
        return search(prefix) != null;
    }

    private boolean delete(Node cur, String word_target, int index) {
        word_target = word_target.toLowerCase();
        if (index == word_target.length()) {
            if (!cur.isEndOfWord) {
                return false;
            }
            cur.isEndOfWord = false;
            return cur.children.isEmpty();
        }

        char c = word_target.charAt(index);
        if (!cur.children.containsKey(c)) {
            return false;
        }

        boolean shouldDelete = delete(cur.children.get(c), word_target, index + 1);

        if (shouldDelete) {
            cur.children.remove(c);
            return cur.children.isEmpty();
        }

        return false;
    }

    public void delete(String word_target) {
        word_target = word_target.toLowerCase();
        delete(root, word_target, 0);
    }

    public String getMeaning(String word_target) {
        word_target = word_target.toLowerCase();
        Word word = new Word(word_target, null);
        Node cur = search(word_target);
        if (cur != null) {
            return cur.meaning;
        }
        return null;
    }

    //inorder traversal
    private void getSortedWords(Node cur, String cur_target_word, List<Word> sortedWords) {
        if (cur == null) {
            return;
        }

        if (cur.isEndOfWord) {
            sortedWords.add(new Word(cur_target_word, cur.meaning));
            System.out.println(sortedWords.size());
        }

        for (char c : cur.children.keySet()) {
            getSortedWords(cur.children.get(c), cur_target_word + c, sortedWords);
        }
    }

    public List<Word> getSortedWords() {
        List<Word> sortedWords = new ArrayList<>();
        getSortedWords(root, "", sortedWords);
        return sortedWords;
    }

    public static void main(String[] args) {
        DictionaryTrie dictionary = new DictionaryTrie();

        dictionary.insert(new Word("Hello", "Xin chao"));
        dictionary.insert(new Word("Bye", "Xin cut"));
        dictionary.insert(new Word("Birthday", "Sinh nhat"));
        dictionary.delete("Birthday");

        System.out.println(dictionary.getMeaning("HELLO"));
    }
}
