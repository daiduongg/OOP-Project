import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<String> searchHistory;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        searchHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getSearchHistory() {
        return searchHistory;
    }

    public void addSearchToHistory(String query) {
        searchHistory.add(query);
    }
}
