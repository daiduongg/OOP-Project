package uet.cs.dictionaryfx.dictionary.model;

public class Test {
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        DictionaryLoader dictionaryLoader = new DictionaryLoader(dictionary);
        dictionaryLoader.loadEnViFromFile();
        System.out.println(dictionary.getWordData("abandon"));
    }
}
