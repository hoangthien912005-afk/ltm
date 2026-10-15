import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class bai3 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int port = 2207;

        String studentCode = "B23DCCN784";
        String qCode = "UPVyJanc"; // Thay bằng mã câu hỏi thật

        try {
            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(serverIP);

            // =========================================
            // a. Gửi ;studentCode;qCode
            // =========================================

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

            // =========================================
            // b. Nhận requestId;a1,a2,...,a50
            // =========================================

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

            // =========================================
            // Tách requestId và danh sách số
            // =========================================

            String[] parts = message.split(";", 2);

            String requestId = parts[0];
            String numberList = parts[1];

            String[] numbers = numberList.split(",");

            // =========================================
            // c. Tìm max và min
            // =========================================

            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (String number : numbers) {

                int value = Integer.parseInt(number.trim());

                if (value > max) {
                    max = value;
                }

                if (value < min) {
                    min = value;
                }
            }

            // =========================================
            // Tạo kết quả:
            // requestId;max,min
            // =========================================

            String result =
                    requestId + ";" + max + "," + min;

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

            System.out.println("Đã gửi kết quả: " + result);

            // =========================================
            // d. Đóng socket
            // =========================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:

a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.

Ví dụ: ;B15DCCN001;DC73CA2E

b. Nhận thông điệp là một chuỗi từ server theo định dạng requestId;a1,a2,...,a50

requestId là chuỗi ngẫu nhiên duy nhất
a1 -> a50 là 50 số nguyên ngẫu nhiên
c. Thực hiện tìm giá trị lớn nhất và giá trị nhỏ nhất thông điệp trong a1 -> a50 và gửi thông điệp lên lên server theo định dạng requestId;max,min

d. Đóng socket và kết thúc chương trình