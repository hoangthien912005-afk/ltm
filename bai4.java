import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class bai4{

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int port = 2208;

        String studentCode = "B23DCCN784";
        String qCode = "MLGlP3Dk"; // Thay bằng mã câu hỏi thật

        try {
            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(serverIP);

            // =====================================
            // a. Gửi ;studentCode;qCode
            // =====================================

            String request = ";" + studentCode + ";" + qCode;

            byte[] sendData =
                    request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket sendPacket =
                    new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            port
                    );

            socket.send(sendPacket);

            System.out.println("Đã gửi: " + request);

            // =====================================
            // b. Nhận requestId;data
            // =====================================

            byte[] receiveData = new byte[4096];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveData,
                            receiveData.length
                    );

            socket.receive(receivePacket);

            String message = new String(
                    receivePacket.getData(),
                    receivePacket.getOffset(),
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            ).trim();

            System.out.println("Server gửi: " + message);

            // Tách requestId và data
            String[] parts = message.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // =====================================
            // c. Chuẩn hóa chuỗi
            // =====================================

            StringBuilder normalized = new StringBuilder();

            // Chuyển toàn bộ về chữ thường trước
            data = data.toLowerCase();

            boolean capitalizeNext = true;

            for (int i = 0; i < data.length(); i++) {

                char c = data.charAt(i);

                if (Character.isLetter(c)) {

                    if (capitalizeNext) {
                        normalized.append(
                                Character.toUpperCase(c)
                        );
                        capitalizeNext = false;
                    } else {
                        normalized.append(c);
                    }

                } else {

                    normalized.append(c);

                    // Ký tự không phải chữ cái
                    // được xem là kết thúc một từ
                    capitalizeNext = true;
                }
            }

            String resultData = normalized.toString();

            // =====================================
            // Gửi requestId;data đã chuẩn hóa
            // =====================================

            String result =
                    requestId + ";" + resultData;

            byte[] resultBytes =
                    result.getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket =
                    new DatagramPacket(
                            resultBytes,
                            resultBytes.length,
                            receivePacket.getAddress(),
                            receivePacket.getPort()
                    );

            socket.send(resultPacket);

            System.out.println("Đã gửi: " + result);

            // =====================================
            // d. Đóng socket
            // =====================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản dưới đây:

a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.

Ví dụ: ;B15DCCN001;5B35BCC1

b. Nhận thông điệp từ server theo định dạng requestId;data

requestId là một chuỗi ngẫu nhiên duy nhất
data là chuỗi dữ liệu cần xử lý
c. Xử lý chuẩn hóa chuỗi đã nhận thành theo nguyên tắc

Ký tự đầu tiên của từng từ trong chuỗi là in hoa
Các ký tự còn lại của chuỗi là in thường Gửi thông điệp chứa chuỗi đã được chuẩn hóa lên server theo định dạng requestId;data
d. Đóng socket và kết thúc chương trình