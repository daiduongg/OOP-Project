import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class DatabaseManagement {
    private MyDictionary dictionary;

    public static final String URL = "jdbc:mysql://localhost:3306/evdictionary";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";

    DatabaseManagement() {
        dictionary = new MyDictionary();
    }

    DatabaseManagement(MyDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void insertFromDatabase() {
        try {
            //Class.forName("com.mysqk.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from dictionary");

            while (resultSet.next()) {

                String word_name = resultSet.getString(2);
                String word_data = resultSet.getString(3);
                dictionary.insert(word_name, word_data);



                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final String TEMP_FILE_PATH = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\dictionaries.txt";

    public static void main(String[] args) {
        MyDictionary myDictionary = new MyDictionary();
        FileManagement fileManagement = new FileManagement(myDictionary);
        DatabaseManagement databaseManagement = new DatabaseManagement(myDictionary);
        databaseManagement.insertFromDatabase();
    }
}


