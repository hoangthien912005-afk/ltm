import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class bai2 {

    public static void main(String[] args) {

        // Địa chỉ server
        // Nếu hệ thống cung cấp IP server thì thay localhost bằng IP đó
        String serverAddress = "36.50.135.242";

        // Port theo đề bài
        int port = 2206;

        // Thay bằng mã sinh viên và mã câu hỏi của bạn
        String studentCode = "B23DCCN784";
        String qCode = "b7rXiCnH";

        try {

            // ==========================================
            // 1. Kết nối tới Server
            // ==========================================

            Socket socket = new Socket(serverAddress, port);

            // Thời gian chờ tối đa 5 giây
            socket.setSoTimeout(5000);

            System.out.println("Đã kết nối tới Server.");

            // Lấy luồng vào và luồng ra
            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();


            // ==========================================
            // 2. Gửi mã sinh viên và mã câu hỏi
            // ==========================================

            String request = studentCode + ";" + qCode;

            output.write(request.getBytes());
            output.flush();

            System.out.println("Đã gửi: " + request);


            // ==========================================
            // 3. Nhận dữ liệu từ Server
            // ==========================================

            byte[] buffer = new byte[4096];

            int length = input.read(buffer);

            if (length == -1) {
                System.out.println("Server đã đóng kết nối.");
                socket.close();
                return;
            }

            String response = new String(buffer, 0, length).trim();

            System.out.println("Dữ liệu Server gửi:");
            System.out.println(response);


            // ==========================================
            // 4. Tách chuỗi thành các số nguyên
            // ==========================================

            String[] parts = response.split(",");

            List<Integer> numbers = new ArrayList<>();

            for (String part : parts) {
                numbers.add(Integer.parseInt(part.trim()));
            }


            // ==========================================
            // 5. Tìm khoảng cách nhỏ nhất
            // ==========================================

            int minDistance = Integer.MAX_VALUE;
            int number1 = 0;
            int number2 = 0;

            for (int i = 0; i < numbers.size(); i++) {

                for (int j = i + 1; j < numbers.size(); j++) {

                    int a = numbers.get(i);
                    int b = numbers.get(j);

                    int distance = Math.abs(a - b);

                    if (distance < minDistance) {

                        minDistance = distance;

                        number1 = a;
                        number2 = b;
                    }
                }
            }


            // ==========================================
            // 6. Tạo kết quả
            // ==========================================

            String result = minDistance + "," + number1 + "," + number2;

            System.out.println("Kết quả:");
            System.out.println(result);


            // ==========================================
            // 7. Gửi kết quả về Server
            // ==========================================

            output.write(result.getBytes());
            output.flush();

            System.out.println("Đã gửi kết quả về Server.");


            // ==========================================
            // 8. Đóng kết nối
            // ==========================================

            socket.close();

            System.out.println("Đã đóng kết nối.");

        } catch (Exception e) {

            System.out.println("Có lỗi xảy ra:");
            System.out.println(e.getMessage());
        }
    }
}
// Một chương trình server hỗ trợ kết nối qua giao thức TCP tại cổng 2206 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu xây dựng chương trình client thực hiện kết nối tới server trên sử dụng luồng byte dữ liệu (InputStream/OutputStream) để trao đổi thông tin theo thứ tự:

a. Gửi mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

Ví dụ: B16DCCN999;FF49DC02

b. Nhận dữ liệu từ server là một chuỗi các giá trị số nguyên được phân tách nhau bởi ký tự ,

Ví dụ: 1,3,9,19,33,20

c. Thực hiện tìm giá trị khoảng cách nhỏ nhất của các phần tử nằm trong chuỗi và hai giá trị lớn nhất tạo nên khoảng cách đó. Gửi lên server chuỗi gồm "khoảng cách nhỏ nhất, số thứ nhất, số thứ hai".

Ví dụ: 1,19,20

d. Đóng kết nối và kết thúc