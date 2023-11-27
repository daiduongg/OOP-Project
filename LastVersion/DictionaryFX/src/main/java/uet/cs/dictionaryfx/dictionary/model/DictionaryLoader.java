package uet.cs.dictionaryfx.dictionary.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DictionaryLoader {
    private Dictionary dictionary;

    public static final String VI_EN_FILE_PATH = "/uet/cs/dictionaryfx/dictionary/model/Assets/Vi-En.txt";
    public static final String EN_VI_FILE_PATH = "/uet/cs/dictionaryfx/dictionary/model/Assets/En-Vi.txt";
    public static final String EN_VI_DB_PATH = "uet/cs/dictionaryfx/dictionary/model/Assets/envidb.db";
    public static final String Vi_EN_DB_PATH = "uet/cs/dictionaryfx/dictionary/model/Assets/viendb.db";

    public DictionaryLoader(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    private void loadFromFile(String filePath) {
        InputStream inputStream = getClass().getResourceAsStream(filePath);
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

                        String wordName = data.substring(1, index).trim();
                        String wordData = data;
                        dictionary.insert(wordName,wordData);
                    }
                    data = line.trim() + "\n";
                } else {
                    data += line.trim() + "\n";
                }
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEnViFromFile() {
        loadFromFile(EN_VI_FILE_PATH);
    }

    public void loadViEnFromFile() {
        loadFromFile(VI_EN_FILE_PATH);
    }

    private void loadFromDB(String filePath) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);
            URL resourcesURL = dbPath.toUri().toURL();

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "SELECT * FROM tbl_edict";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String wordName = resultSet.getString("word");
                String wordData = resultSet.getString("detail");
                dictionary.insert(wordName, wordData);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void loadEnViFromDB() {
        loadFromDB(EN_VI_DB_PATH);
    }

    public void loadViEnFromDB() {
        loadFromDB(Vi_EN_DB_PATH);
    }

    public static void main(String[] args) {
        DictionaryLoader dictionaryLoader = new DictionaryLoader(new Dictionary());
        dictionaryLoader.loadEnViFromDB();
    }
}
