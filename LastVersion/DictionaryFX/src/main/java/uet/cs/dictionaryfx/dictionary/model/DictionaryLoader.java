package uet.cs.dictionaryfx.dictionary.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
                System.out.println(wordData);
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

    public String getAudioUrl(String word) {
        if (!word.contains(" ")) {
            try {
                String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
                HttpClient httpClient = HttpClient.newHttpClient();
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
                                return audioUrl;
                            }
                        }
                    }
                }
                httpClient.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean downloadAudio(String audioUrl, String fileName) {
        if (audioUrl != null && fileName != null) {
            try {
                HttpClient httpClient = HttpClient.newHttpClient();
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
            }
        }
        return false;
    }
}
