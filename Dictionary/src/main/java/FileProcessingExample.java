import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileProcessingExample {

    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\format.txt";
        String outputFilePath = "C:\\Users\\daidu\\OneDrive - vnu.edu.vn\\Desktop\\last.txt";

        try {
            // Đọc file
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

            // Ghi kết quả vào file
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            String line;
            while ((line = reader.readLine()) != null) {
                // Xử lý dòng đọc được (ở đây chỉ in ra console)
                //System.out.println("Đã đọc: " + line);
                StringBuilder s = new StringBuilder(line);

                // Xử lý: Xóa các chuỗi bắt đầu bằng "<" và kết thúc bằng ">"
                int i;
                while ((i = s.indexOf("<")) != -1) {
                    int j = s.indexOf(">", i);
                    if (j != -1) {
                        s.delete(i, j + 1);
                    } else {
                        break; // Tránh vòng lặp vô hạn nếu thiếu ký tự ">"
                    }
                }

                // Ghi kết quả vào file
                System.out.println(s);
                writer.write(s.toString());
                writer.newLine(); // Thêm dòng mới
            }

            // Đóng luồng đọc và ghi
            reader.close();
            writer.close();

            System.out.println("Xử lý và ghi kết quả thành công.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
