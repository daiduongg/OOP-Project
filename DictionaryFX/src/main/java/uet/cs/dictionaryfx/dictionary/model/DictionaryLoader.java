package uet.cs.dictionaryfx.dictionary.model;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.nio.file.*;

public class DictionaryLoader {
    private Dictionary dictionary;

    public static final String VI_EN_FILE_PATH = "/uet/cs/dictionaryfx/dictionary/model/Assets/Vi-En.txt";
    public static final String EN_VI_FILE_PATH = "/uet/cs/dictionaryfx/dictionary/model/Assets/En-Vi.txt";
    public static final String EN_VI_DB_PATH = "uet/cs/dictionaryfx/dictionary/model/Assets/envidb.db";
    public static final String Vi_EN_DB_PATH = "uet/cs/dictionaryfx/dictionary/model/Assets/viendb.db";
    public static final String LOWER_CASE_ALPHABET = "aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz ";
    public static final String UPPER_CASE_ALPHABET = "AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ ";

    public DictionaryLoader(Dictionary dictionary) {
        this.dictionary = dictionary;
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
                        dictionary.insertWordFromDB(wordName,wordData);
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

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "SELECT * FROM tbl_edict";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String wordName = resultSet.getString("word");
                String wordData = resultSet.getString("detail");
                dictionary.insertWordFromDB(wordName, wordData);
            }

            sql = "SELECT * FROM tbl_history";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String wordName = resultSet.getString("word");
                dictionary.addHistoryWordFromDB(toLowerCaseWordName(wordName));
            }

            sql = "SELECT * FROM tbl_favorite";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String wordName = resultSet.getString("word");
                dictionary.addFavoriteWordFromDB(wordName);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertWordToDB(String filePath, Word word) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            StringBuilder sql = new StringBuilder("INSERT INTO tbl_edict (word, detail) VALUES (?, ?)");

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {
                String wordName = toLowerCaseWordName(word.getWordName()).trim();
                String wordData = word.getWordData().trim();

                preparedStatement.setString(1, wordName);
                preparedStatement.setString(2, wordData);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeWordFromDB(String filePath, String wordName) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "DELETE FROM tbl_edict WHERE word = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, toLowerCaseWordName(wordName));

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertWordToHistoryDB(String filePath, String wordName) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "INSERT INTO tbl_history (word) VALUES (?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, toLowerCaseWordName(wordName).trim());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeWordFromHistoryDB(String filePath, String wordName) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "DELETE FROM tbl_history WHERE word = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, toLowerCaseWordName(wordName).trim());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertWordToFavoriteDB(String filePath, String wordName) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "INSERT INTO tbl_favorite (word) VALUES (?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, toLowerCaseWordName(wordName).trim());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeWordFromFavoriteDB(String filePath, String wordName) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "DELETE FROM tbl_favorite WHERE word = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, toLowerCaseWordName(wordName).trim());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateWordToDB(String filePath, String wordName, String wordData) {
        try {
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path dbPath = resourcesPath.resolve(filePath);

            String jdbcUrl = "jdbc:sqlite:" + dbPath.toAbsolutePath();
            Connection connection = DriverManager.getConnection(jdbcUrl);

            String sql = "UPDATE tbl_edict SET detail = ? WHERE word = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, wordData);
                preparedStatement.setString(2, toLowerCaseWordName(wordName.trim()));

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEnViWordFromDB (String wordName, String wordData) {
        updateWordToDB(EN_VI_DB_PATH, wordName, wordData);
    }

    public void updateViEnWordFromDB (String wordName, String wordData) {
        updateWordToDB(Vi_EN_DB_PATH, wordName, wordData);
    }

    public void removeEnViWordFromDB(String word) {
        removeWordFromDB(EN_VI_DB_PATH, word);
    }

    public void removeViEnWordFromDB(String word) {
        removeWordFromDB(Vi_EN_DB_PATH, word);
    }

    public void removeEnViWordFromHistoryDB(String word) {
        removeWordFromHistoryDB(EN_VI_DB_PATH, word);
    }

    public void removeViEnWordFromHistoryDB(String word) {
        removeWordFromHistoryDB(Vi_EN_DB_PATH, word);
    }

    public void removeEnViWordFromFavoriteDB(String word) {
        removeWordFromFavoriteDB(EN_VI_DB_PATH, word);
    }

    public void removeViEnWordFromFavoriteDB(String word) {
        removeWordFromFavoriteDB(Vi_EN_DB_PATH, word);
    }

    public void insertEnViWordToDB(Word word) {
        insertWordToDB(EN_VI_DB_PATH, word);
    }

    public void insertViEnWordToDB(Word word) {
        insertWordToDB(Vi_EN_DB_PATH, word);
    }

    public void insertEnViWordToFavoriteDB(String word) {
        insertWordToFavoriteDB(EN_VI_DB_PATH, word);
    }

    public void insertViEnWordToFavoriteDB(String word) {
        insertWordToFavoriteDB(Vi_EN_DB_PATH, word);
    }

    public void insertEnViWordToHistoryDB(String word) {
        insertWordToHistoryDB(EN_VI_DB_PATH, word);
    }

    public void insertViEnWordToHistoryDB(String word) {
        insertWordToHistoryDB(Vi_EN_DB_PATH, word);
    }

    public void loadEnViFromDB() {
        loadFromDB(EN_VI_DB_PATH);
    }

    public void loadViEnFromDB() {
        loadFromDB(Vi_EN_DB_PATH);
    }

    public String getEnAudioURL(String word) {
        if (word != null && !word.contains(" ")) {
            HttpClient httpClient = HttpClient.newHttpClient();
            try {
                String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .build();

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.body().startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(response.body());

                    if (jsonArray.length() > 0) {
                        JSONObject firstEntry = jsonArray.getJSONObject(0);
                        JSONArray phoneticsArray = firstEntry.getJSONArray("phonetics");

                        for (int i = 0; i < phoneticsArray.length(); i++) {
                            JSONObject phonetic = phoneticsArray.getJSONObject(i);
                            String audioUrl = phonetic.optString("audio", "");
                            if (audioUrl.trim().length() > 0) {
                                httpClient.close();
                                return audioUrl;
                            }
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                httpClient.close();
            }
        }
        return null;
    }

    public boolean downloadEnAudio(String audioUrl) {
        String fileName = "en-word-audio.mp3";
        if (audioUrl != null && fileName != null) {
            HttpClient httpClient = HttpClient.newHttpClient();
            try {
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(audioUrl))
                        .build();

                HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
                try (InputStream inputStream = response.body();
                     FileOutputStream outputStream = new FileOutputStream(fileName)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    outputStream.close();
                }
                System.out.println("Audio downloaded successfully to: " + fileName);
                httpClient.close();
                return true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                httpClient.close();
            }
        }
        return false;
    }

    public boolean downloadViAudio(String word) throws IOException {
        String fileName = "vi-word-audio.wav";
        if (word != null) {
            String apiUrl = "https://viettelgroup.ai/voice/api/tts/v1/rest/syn";
            String datajson = "{\"text\":\"" + word + "\"," +
                    "\"voice\":\"doanngocle\"," +
                    "\"id\":\"3\"," +
                    "\"without_filter\":false," +
                    "\"speed\":1.0," +
                    "\"tts_return_option\":2}";

            HttpClient httpClient = HttpClient.newHttpClient();
            try {
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("content-type", "application/json;charset=UTF-8")
                        .header("token", "Cvdxhq23G9OaaYk7YdwbJu5N17PLpztFgs12WNKDQ3VW0LRGgcWGWQQ9A6fifXBd")
                        .POST(HttpRequest.BodyPublishers.ofString(datajson))
                        .build();

                HttpResponse<InputStream> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

                if (response.statusCode() == 200) {
                    try (InputStream inputStream = response.body();
                         FileOutputStream outputStream = new FileOutputStream(fileName)) {

                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        System.out.println("Audio downloaded successfully to: " + fileName);
                        return true;
                    }
                } else {
                    System.err.println("Error: " + response.statusCode() + ", " + response.body());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                httpClient.close();
            }
        }
        return false;
    }
}

