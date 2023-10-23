import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;

public class DictionaryManagement {
    //File to test wirte
    static final String tempFile = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\dictionaries.txt";
    //File En to Vi
    static final String dataEVFilePath = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\En-Vi.txt";
    //File Vi to En
    static final String dataVEFilePath = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\Vi-En.txt";

    private DictionaryDataBase dictionary;

    public DictionaryManagement() {
        dictionary = new DictionaryDataBase();
    }

    public DictionaryManagement(DictionaryDataBase dictionary) {
        this.dictionary = dictionary;
    }

    public boolean insertFromFile(String filePath) {
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file);

            String line;
            String data = null;

            while ((line = reader.readLine()) != null) {
                //Format file: Word start with "@"
                if (line.startsWith("@")) {
                    //Skip the first line of file
                    if (data != null) {
                        //Format file: Word's name end before "/" or "\n"
                        int index = data.indexOf('/');
                        if (index == -1) {
                            index = data.indexOf('\n');
                        }

                        String word_name = data.substring(1, index).trim();
                        String word_data = data;
                        //System.out.println(data);
                        if (dictionary.insert(word_name, word_data)) {
                            System.out.println(word_name + " has been inserted from file!");
                        } else {
                            System.out.println(word_name + " cut!");
                        }
                    }
                    data = line + "\n";
                } else {
                    data += line + "\n";
                }
            }
            reader.close();
            file.close();
        } catch (IOException e) {
            System.err.println("Error reading data to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean dictionaryExportToFile(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);

            for (Word word : dictionary.getAllWords()) {
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

    public void dictionaryLookup(String prefix) {
        List<Word> wordsList = dictionary.getWordsHasPrefix(prefix);
        if (wordsList.isEmpty()) {
            System.err.println("Word not found!");
        } else {
            for (Word word : wordsList) {
                System.out.println("@" + word.getWord_name() + " " + word.getWord_data());
            }
        }
    }

    public static void main(String[] args) {
        DictionaryManagement dictManager = new DictionaryManagement();
        dictManager.insertFromFile(dataVEFilePath);
        //dictManager.dictionaryLookup("try");
        dictManager.dictionaryExportToFile(tempFile);
    }
}
