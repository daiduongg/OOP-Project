import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Week10 {
    public static void main(String[] args) {
        String fileContent = // Đoạn mã nguồn bạn muốn phân tích
                "public static void sayHello() {}\n" +
                        "public static int add(int a, int b) {}\n" +
                        "public static void printMessage(String message) {}\n" +
                        "public static void printMessages(String[] messages) {}\n" +
                        "public static String readFile(File file) {}\n" +
                        "// tệp này có 2 lớp\n" +
                        "package net.bqc.utils;\n" +
                        "class Message {}\n" +
                        "public static void printMessage(Message message) {}\n" +
                        "// tệp này có 2 lớp\n" +
                        "package net.bqc.utils;\n" +
                        "import java.util.List;\n" +
                        "class Message {}\n" +
                        "public static void printMessages(List<Message> messages) {}";

        List<String> methodSignatures = getAllFunctions(fileContent);

        for (String signature : methodSignatures) {
            System.out.println("=> " + signature);
        }
    }

    public static List<String> getAllFunctions(String fileContent) {
        List<String> methodSignatures = new ArrayList<>();
        String[] lines = fileContent.split("\n");
        String currentMethodSignature = "";
        boolean isInsideMethod = false;

        for (String line : lines) {
            if (isInsideMethod) {
                currentMethodSignature += line.trim();
                if (line.endsWith(") {")) {
                    // Kết thúc của phần chữ ký phương thức
                    methodSignatures.add(currentMethodSignature);
                    currentMethodSignature = "";
                    isInsideMethod = false;
                }
            } else if (line.startsWith("public static ")) {
                // Bắt đầu của chữ ký phương thức
                currentMethodSignature = line.trim().replaceAll("\\s+", " ");
                isInsideMethod = true;
            }
        }

        return methodSignatures;
    }
}
