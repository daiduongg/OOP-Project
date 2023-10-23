public class Word {
    private String word_name;
    private String word_data;

    public Word() {
        word_name = null;
        word_data = null;
    }

    public Word(String word_name, String word_data) {
        this.word_name = word_name;
        this.word_data = word_data;
    }

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public String getWord_data() {
        return word_data;
    }

    public void setWord_data(String word_data) {
        this.word_data = word_data;
    }
}
