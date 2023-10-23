import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManagement {
    public static final String TEMP_FILE_PATH = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\temp.txt";
    //File En to Vi
    public static final String DATA_USERS_FILE_PATH = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\BP OOP\\Dictionary\\src\\main\\resources\\users-data.txt";
    private List<User> users;

    public UserManagement() {
        users = new ArrayList<>();
    }

    private boolean loadDataFromFile(String filePath) {
        try {
            FileReader file = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(file);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                User user = new User(data[0], data[1]);
                if ((line = reader.readLine()) != null) {
                    data = line.split(",");
                    for (String s : data) {
                        user.addSearchToHistory(s);
                    }
                }
                users.add(user);
            }
            reader.close();
            file.close();
        } catch (IOException e) {
            System.err.println("Error reading data to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean userExportToFile(String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);

            for (User user : users) {
                writer.write(user.getUsername() + "\t" + user.getPassword() + "\n");
                for (String search : user.getSearchHistory()) {
                    writer.write(search + ",");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing data to file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public static void main(String[] args) {
        // Sample data
        User user1 = new User("user1", "password1");
        //user1.addSearchToHistory("query1");
        //user1.addSearchToHistory("query2");

        User user2 = new User("user2", "password2");
        user2.addSearchToHistory("query3");
        user2.addSearchToHistory("query4");

        UserManagement userManagement = new UserManagement();
        userManagement.addUser(user1);
        userManagement.addUser(user2);

        // Test userExportToFile
        String exportFilePath = "exported_user_data.txt"; // Replace with your file path
        boolean successExport = userManagement.userExportToFile(TEMP_FILE_PATH);

        if (successExport) {
            System.out.println("Data exported successfully.");
        } else {
            System.err.println("Error exporting data to file.");
        }

        // Test loadDataFromFile
        String filePath = TEMP_FILE_PATH; // Replace with your file path
        boolean successLoad = userManagement.loadDataFromFile(filePath);

        if (successLoad) {
            System.out.println("Data loaded successfully:");
            for (User user : userManagement.getUsers()) {
                System.out.println("Username: " + user.getUsername());
                System.out.println("Password: " + user.getPassword());
                System.out.println("Search History: " + user.getSearchHistory());
                System.out.println();
            }
        } else {
            System.err.println("Error loading data from file.");
        }
    }
}
