import java.io.*;
import java.util.List;

public class DictionaryManagement {

    private MyDictionary enViDictionary;
    private MyDictionary viEnDictionary;

    public DictionaryManagement() {
        enViDictionary = new MyDictionary();
        viEnDictionary = new MyDictionary();
    }

    public void loadEnViFromFile()  {
        FileManagement fileManagement = new FileManagement(enViDictionary);
        fileManagement.loadEnVi();
    }

    public void loadViEnFromFile() {
        FileManagement fileManagement = new FileManagement(viEnDictionary);
        fileManagement.loadViEn();
    }

    public void loadEnViFromDatabase()  {
        DatabaseManagement databaseManagement = new DatabaseManagement(enViDictionary);
        databaseManagement.insertFromDatabase();
    }

    public boolean dictionaryExportToFile(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);

            for (Word word : enViDictionary.getAllWords()) {
                writer.write( word.getWord_data() + "\n");
                System.out.println("\"" + word.getWord_name() + "\" has been exported to file!");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing data to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    /*
    public List<Word> dictionaryLookup(String prefix) {
        List<Word> wordsList = myDictionary.getWordsHasPrefix(prefix);
        if (wordsList.isEmpty()) {
            System.err.println("Word not found!");
            return null;
        }
        return wordsList;
    }

     */

    public static void main(String[] args) {
        DictionaryManagement dictManager = new DictionaryManagement();
    }
}
