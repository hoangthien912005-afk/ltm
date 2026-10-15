import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class bai2 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int port = 2208;

        String studentCode = "B23DCCN784";
        String qCode = "mRi4P5h4"; // Thay bằng mã câu hỏi thật

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

            // =====================================
            // Tách requestId và data
            // =====================================

            String[] parts = message.split(";", 2);

            String requestId = parts[0];
            String data = parts[1];

            // =====================================
            // c. Tìm ký tự xuất hiện nhiều nhất
            // =====================================

            int[] count = new int[Character.MAX_VALUE + 1];

            for (char c : data.toCharArray()) {
                count[c]++;
            }

            char maxChar = data.charAt(0);
            int maxCount = count[maxChar];

            for (char c : data.toCharArray()) {
                if (count[c] > maxCount) {
                    maxCount = count[c];
                    maxChar = c;
                }
            }

            // =====================================
            // Tìm các vị trí xuất hiện
            // Vị trí tính từ 0
            // =====================================

            StringBuilder positions = new StringBuilder();

            for (int i = 0; i < data.length(); i++) {

    if (data.charAt(i) == maxChar) {
        positions.append(i + 1).append(",");
    }
}
            // =====================================
            // Tạo kết quả
            // requestId;ký_tự:vị_trí
            // =====================================

            String result =
                    requestId + ";" +
                    maxChar + ":" +
                    positions;

            byte[] resultData =
                    result.getBytes(StandardCharsets.UTF_8);

            DatagramPacket resultPacket =
                    new DatagramPacket(
                            resultData,
                            resultData.length,
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
//Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client tương tác với server kịch bản dưới đây:

a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.

Ví dụ: ;B15DCCN001;EE29C059

b. Nhận thông điệp từ server theo định dạng requestId; data

requestId là một chuỗi ngẫu nhiên duy nhất
data là chuỗi dữ liệu đầu vào cần xử lý
Ví dụ: requestId;Qnc8d5x78aldSGWWmaAAjyg3

c. Tìm kiếm ký tự xuất hiện nhiều nhất trong chuỗi và gửi lên server theo định dạng “requestId;ký tự xuất hiện nhiều nhất: các vị trí xuất hiện ký tự đó”

Ví dụ: requestId;8:4,9,

d. Đóng socket và kết thúc chương trình


