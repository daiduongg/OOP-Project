package uet.cs.dictionaryfx.dictionary.model;

public class DictionaryManager {
    private static Dictionary enDictionary;
    private static Dictionary viDictionary;

    public static Dictionary getEnDictionary() {
        return enDictionary;
    }

    public static void setEnDictionary(Dictionary enDictionary) {
        DictionaryManager.enDictionary = enDictionary;
    }

    public static Dictionary getViDictionary() {
        return viDictionary;
    }

    public static void setViDictionary(Dictionary viDictionary) {
        DictionaryManager.viDictionary = viDictionary;
    }
}
