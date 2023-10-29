import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManagement {
    private MyDictionary dictionary;

    public static final String viEnFileName = "Vi-En.txt";
    public static final String enViFileName = "En-Vi.txt";

    FileManagement() {
        dictionary = new MyDictionary();
    }

    FileManagement(MyDictionary dictionary) {
        this.dictionary = dictionary;
    }

    private void insertFromFile(String fileName) {
        ClassLoader classLoader = FileManagement.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        try {
            if (inputStream == null) {
                throw new Exception();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
                        if (dictionary.insert(word_name,word_data)) {
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
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEnVi() {
        insertFromFile(viEnFileName);
    }

    public void loadViEn() {
        insertFromFile(enViFileName);
    }



    public static final String TEMP_FILE_PATH = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\dictionaries.txt";

    public static void main(String[] args) {
        FileManagement fileManagement = new FileManagement();
        fileManagement.insertFromFile("Vi-En.txt");
    }
}
