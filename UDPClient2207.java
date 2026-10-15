import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class UDPClient2207 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int port = 2207;

        String studentCode = "B23DCCN784";
        String qCode = "gCoXvDuM"; // Thay bằng mã câu hỏi thật

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
            // b. Nhận requestId;n;A1,A2,...,Am
            // =========================================

            byte[] receiveData = new byte[4096];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveData,
                            receiveData.length
                    );

            socket.receive(receivePacket);

            String data = new String(
                    receivePacket.getData(),
                    receivePacket.getOffset(),
                    receivePacket.getLength(),
                    StandardCharsets.UTF_8
            ).trim();

            System.out.println("Server gửi: " + data);

            // =========================================
            // Tách dữ liệu
            // =========================================

            String[] parts = data.split(";", 3);

            String requestId = parts[0];
            int n = Integer.parseInt(parts[1]);
            String numberList = parts[2];

            // =========================================
            // c. Tìm các giá trị còn thiếu
            // =========================================

            Set<Integer> numbers = new HashSet<>();

            if (!numberList.isEmpty()) {
                String[] values = numberList.split(",");

                for (String value : values) {
                    numbers.add(Integer.parseInt(value.trim()));
                }
            }

            StringBuilder missing = new StringBuilder();

            // Các giá trị cần tìm nằm từ 1 đến n
            for (int i = 1; i <= n; i++) {

                if (!numbers.contains(i)) {

                    if (missing.length() > 0) {
                        missing.append(",");
                    }

                    missing.append(i);
                }
            }

            // =========================================
            // Tạo kết quả:
            // requestId;B1,B2,...,Bm
            // =========================================

            String result =
                    requestId + ";" + missing;

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

            // =========================================
            // d. Đóng socket
            // =========================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}