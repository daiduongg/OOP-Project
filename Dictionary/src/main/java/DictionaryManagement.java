import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;

public class DictionaryManagement {

    private MyDictionary myDictionary;
    private FileManagement fileManagement;
    private DatabaseManagement databaseManagement;

    public DictionaryManagement() {
        myDictionary = new MyDictionary();
        fileManagement = new FileManagement(myDictionary);
        databaseManagement = new DatabaseManagement(myDictionary);
    }

    public boolean dictionaryExportToFile(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);

            for (Word word : myDictionary.getAllWords()) {
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

    public List<Word> dictionaryLookup(String prefix) {
        List<Word> wordsList = myDictionary.getWordsHasPrefix(prefix);
        if (wordsList.isEmpty()) {
            System.err.println("Word not found!");
            return null;
        }
        return wordsList;
    }

    public static void main(String[] args) {
        DictionaryManagement dictManager = new DictionaryManagement();
    }
}
