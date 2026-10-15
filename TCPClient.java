import java.io.*;
import java.net.*;
import java.util.*;

public class TCPClient {

    public static void main(String[] args) {

        // Thay bằng địa chỉ server được cung cấp
        String serverAddress = "36.50.135.242";
        int port = 2208;

        // Thay bằng mã sinh viên và mã câu hỏi của bạn
        String studentCode = "B23DCCN784";
        String qCode = "Q9CazF8r";

        try (
            Socket socket = new Socket(serverAddress, port);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())
            )
        ) {

            // Đặt thời gian timeout tối đa 5 giây
            socket.setSoTimeout(5000);

            // =========================================
            // BƯỚC 1: Gửi mã sinh viên và mã câu hỏi
            // =========================================

            String request = studentCode + ";" + qCode;

            writer.write(request);
            writer.newLine();
            writer.flush();

            System.out.println("Đã gửi: " + request);


            // =========================================
            // BƯỚC 2: Nhận danh sách tên miền từ Server
            // =========================================

            String response = reader.readLine();

            System.out.println("Server gửi:");
            System.out.println(response);


            // =========================================
            // BƯỚC 3: Tìm các tên miền .edu
            // =========================================

            StringBuilder result = new StringBuilder();

            String[] domains = response.split(",");

            for (String domain : domains) {

                // Xóa khoảng trắng thừa
                domain = domain.trim();

                // Kiểm tra tên miền kết thúc bằng .edu
                if (domain.endsWith(".edu")) {

                    if (result.length() > 0) {
                        result.append(", ");
                    }

                    result.append(domain);
                }
            }

            String eduDomains = result.toString();

            System.out.println("Các tên miền .edu:");
            System.out.println(eduDomains);


            // =========================================
            // BƯỚC 4: Gửi kết quả về Server
            // =========================================

            writer.write(eduDomains);
            writer.newLine();
            writer.flush();

            System.out.println("Đã gửi kết quả về Server.");


            // =========================================
            // Kết thúc
            // =========================================

            System.out.println("Đóng kết nối.");

        } catch (SocketTimeoutException e) {

            System.out.println("Quá thời gian 5 giây.");

        } catch (IOException e) {

            System.out.println("Lỗi kết nối: " + e.getMessage());
        }
    }
}