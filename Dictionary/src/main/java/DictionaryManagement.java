import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class DictionaryManagement {
    Dictionary dictionary;
    static final String filePath = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP" +
                                    "\\Dictionary\\src\\main\\resources\\dictionaries.txt";

    public DictionaryManagement() {
        //do not thing
    }

    public DictionaryManagement(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean insertFromFile() {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            int count_line = 0;
            while (scanner.hasNextLine()) {
                count_line++;
                String line = scanner.nextLine();
                String[] data = line.split("\t");
                if (data.length != 2) {
                    System.err.println("Line " + count_line + " is in wrong format!");
                } else {
                    dictionary.insertWord(data[0], data[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading data to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean dictionaryExportToFile() {
        try {
            FileWriter writer = new FileWriter(filePath);

            for (Word word : dictionary.getSortedWords()) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain() + "\n");
                System.out.println("Success!");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing data to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void dictionaryLookup() {

    }

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        dictionary.insertWord("apple", "a fruit");
        dictionary.insertWord("banana", "a yellow fruit");
        dictionary.insertWord("computer", "an electronic device");
        dictionary.insertWord("programming", "the process of writing code");
        dictionary.insertWord("language", "a means of communication");
        dictionary.insertWord("book", "a written or printed work");
        dictionary.insertWord("coffee", "a popular beverage");
        dictionary.insertWord("island", "a piece of land surrounded by water");
        dictionary.insertWord("sun", "the star at the center of our solar system");
        dictionary.insertWord("flower", "a colorful plant part");

        DictionaryManagement dictManager = new DictionaryManagement(dictionary);
        dictManager.insertFromFile();
        dictManager.dictionaryExportToFile();
    }
}
