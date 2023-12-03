package uet.cs.dictionaryfx.dictionary.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class Trie {
    public static final String LOWER_CASE_ALPHABET = "aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz ";
    public static final String UPPER_CASE_ALPHABET = "AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ ";

    public class Node {
        Map<Character, Node> children;
        boolean isEndOfWord;
        String wordData;

        Node() {
            children = new HashMap<>();
            isEndOfWord = false;
            wordData = null;
        }
    }

    private Node root;

    private int charToIndex(char c) {
        return LOWER_CASE_ALPHABET.indexOf(c);
    }

    private char indexToChar(int index) {
        return UPPER_CASE_ALPHABET.charAt(index);
    }

    private String toLowerCaseWordName(String input) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);

            int index = UPPER_CASE_ALPHABET.indexOf(current);
            if (index != -1) {
                result.append(LOWER_CASE_ALPHABET.charAt(index));
            } else if (LOWER_CASE_ALPHABET.indexOf(current) != 1) {
                result.append(current);
            }
        }

        return result.toString();
    }

    private Node search(String wordName) {
        wordName = toLowerCaseWordName(wordName);
        Node current = root;
        for (char c : wordName.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }
        return current;
    }

    public Trie() {
        root = new Node();
    }

    public void insert(String wordName, String wordData) {
        wordName = toLowerCaseWordName(wordName);
        Node current = root;
        for (char c : wordName.toCharArray()) {
            current.children.putIfAbsent(c, new Node());
            current = current.children.get(c);
        }
        current.isEndOfWord = true;
        current.wordData = wordData;
    }

    public boolean containsWord(String wordName) {
        Node current = search(wordName);
        if (current == null) return false;
        return search(wordName).isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return search(prefix) != null;
    }

    private boolean delete(Node current, String wordName, int index) {
        if (index == wordName.length()) {
            if (!current.isEndOfWord) {
                return false;
            }
            current.isEndOfWord = false;
            return current.children.isEmpty();
        }

        char c = wordName.charAt(index);
        if (!current.children.containsKey(c)) {
            return false;
        }

        boolean shouldDelete = delete(current.children.get(c), wordName, index + 1);

        if (shouldDelete) {
            current.children.remove(c);
            return current.children.isEmpty();
        }

        return false;
    }

    public void delete(String wordName) {
        wordName = toLowerCaseWordName(wordName);
        delete(root, wordName, 0);
    }

    public String getWordData(String wordName) {
        wordName = toLowerCaseWordName(wordName);
        Node current = search(wordName);
        if (current != null) {
            return current.wordData;
        }
        return null;
    }

    //inorder traversal
    private void getSortedWords(Node current, StringBuilder currentWord, List<Word> sortedWords) {
        if (current == null) {
            return;
        }

        if (current.isEndOfWord) {
            sortedWords.add(new Word(currentWord.toString(), current.wordData));
        }

        for (char c : LOWER_CASE_ALPHABET.toCharArray()) {
            if (current.children.get(c) != null) {
                getSortedWords(current.children.get(c), currentWord.append(c), sortedWords);
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
        }
    }

    public List<Word> getAllWords() {
        List<Word> sortedWords = new ArrayList<>();
        getSortedWords(root, new StringBuilder(), sortedWords);
        return sortedWords;
    }

    public List<Word> getWordsHasPrefix(String prefix) {
        prefix = toLowerCaseWordName(prefix);
        Node current = search(prefix);
        List<Word> sortedWords = new ArrayList<>();
        if (current != null) {
            getSortedWords(current, new StringBuilder(prefix), sortedWords);
        }
        return sortedWords;
    }

    public boolean editWord(String wordName, String wordData) {
        wordName = wordName.toLowerCase();
        Node current = search(wordName);
        if (current != null) {
            current.wordData = wordData;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Trie wordTrie = new Trie();
        wordTrie.insert("apple", "a fruit");
    }
}

