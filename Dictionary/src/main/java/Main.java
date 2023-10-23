import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {
    /*
    public static void main(String[] args) {

        System.out.printf("%-15s %-15s %-15s%n", "Name", "Age", "Occupation");
        System.out.printf("%-15s %-15d %-15s%n", "John", 30, "Engineer");
        System.out.printf("%-15s %-15d %-15s%n", "Alice", 25, "Teacher");
        System.out.printf("%-15s %-15d %-15s%n", "Bob", 35, "Doctor");

    }
     */

    public static void main(String[] args) {
        String data = "         a à ả ã á ạ ă ằ ẳ ẵ ắ ặ â ầ ẩ ẫ ấ ậ b c d đ e è ẻ ẽ é ẹ ê ề ể ễ ế ệ " +
                "         f g h i ì ỉ ĩ í ị j k l m n o ò ỏ õ ó ọ ô ồ ổ ỗ ố ộ ơ ờ ở ỡ ớ ợ " +
                "         p q r s t u ù ủ ũ ú ụ ư ừ ử ữ ứ ự v w x y ỳ ỷ ỹ ý ỵ z ";
        String s = "Ò Ỏ Õ Ó Ọ Ô Ồ Ổ Ỗ Ố Ộ Ơ Ờ Ở Ỡ Ớ Ợ";
        for (char c : s.toCharArray()) {
            if (c != ' ') System.out.print(c);
        }
       // String c = "Ắ";
       // System.out.println(c.toLowerCase());
    }
}
