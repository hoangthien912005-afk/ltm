import UDP.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class bai5 {

    // =========================================
    // Chuẩn hóa tên
    // =========================================
    public static String normalizeName(String name) {

        String[] words = name.trim().toLowerCase().split("\\s+");

        StringBuilder result = new StringBuilder();

        for (String word : words) {

            if (!word.isEmpty()) {

                result.append(
                        Character.toUpperCase(word.charAt(0))
                );

                if (word.length() > 1) {
                    result.append(word.substring(1));
                }

                result.append(" ");
            }
        }

        return result.toString().trim();
    }

    // =========================================
    // Tạo email
    // =========================================
    public static String createEmail(String name) {

        String[] words = name.toLowerCase().trim().split("\\s+");

        if (words.length == 0) {
            return "";
        }

        // Tên là từ cuối cùng
        String firstName = words[words.length - 1];

        StringBuilder email = new StringBuilder();

        // Thêm tên
        email.append(firstName);

        // Thêm chữ cái đầu của họ và tên đệm
        for (int i = 0; i < words.length - 1; i++) {
            email.append(words[i].charAt(0));
        }

        email.append("@ptit.edu.vn");

        return email.toString();
    }

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int port = 2209;

        String studentCode = "B23DCCN784";
        String qCode = "3htD2KNP"; // Thay bằng mã câu hỏi thật

        try {

            DatagramSocket socket = new DatagramSocket();

            InetAddress serverAddress =
                    InetAddress.getByName(serverIP);

            // =========================================
            // a. Gửi ;studentCode;qCode
            // =========================================

            String request =
                    ";" + studentCode + ";" + qCode;

            byte[] requestBytes =
                    request.getBytes(StandardCharsets.UTF_8);

            DatagramPacket requestPacket =
                    new DatagramPacket(
                            requestBytes,
                            requestBytes.length,
                            serverAddress,
                            port
                    );

            socket.send(requestPacket);

            System.out.println("Đã gửi: " + request);

            // =========================================
            // b. Nhận requestId + Student
            // =========================================

            byte[] receiveBuffer = new byte[65507];

            DatagramPacket receivePacket =
                    new DatagramPacket(
                            receiveBuffer,
                            receiveBuffer.length
                    );

            socket.receive(receivePacket);

            int packetLength = receivePacket.getLength();

            byte[] packetData =
                    receivePacket.getData();

            // -----------------------------------------
            // 8 byte đầu tiên = requestId
            // -----------------------------------------

            String requestId =
                    new String(
                            packetData,
                            0,
                            8,
                            StandardCharsets.UTF_8
                    );

            System.out.println("Request ID: " + requestId);

            // -----------------------------------------
            // Các byte còn lại = Student object
            // -----------------------------------------

            ByteArrayInputStream byteInput =
                    new ByteArrayInputStream(
                            packetData,
                            8,
                            packetLength - 8
                    );

            ObjectInputStream objectInput =
                    new ObjectInputStream(byteInput);

            Student student =
                    (Student) objectInput.readObject();

            System.out.println(
                    "Tên ban đầu: " + student.getName()
            );

            // =========================================
            // c. Chuẩn hóa tên
            // =========================================

            String normalizedName =
                    normalizeName(student.getName());

            student.setName(normalizedName);

            // =========================================
            // Tạo email
            // =========================================

            String email =
                    createEmail(normalizedName);

            student.setEmail(email);

            System.out.println(
                    "Tên sau chuẩn hóa: "
                    + student.getName()
            );

            System.out.println(
                    "Email: "
                    + student.getEmail()
            );

            // =========================================
            // Serialize Student
            // =========================================

            ByteArrayOutputStream byteOutput =
                    new ByteArrayOutputStream();

            ObjectOutputStream objectOutput =
                    new ObjectOutputStream(byteOutput);

            objectOutput.writeObject(student);
            objectOutput.flush();

            byte[] studentBytes =
                    byteOutput.toByteArray();

            // =========================================
            // Ghép:
            // 8 byte requestId + Student object
            // =========================================

            byte[] resultBytes =
                    new byte[8 + studentBytes.length];

            // 8 byte đầu
            byte[] requestIdBytes =
                    requestId.getBytes(StandardCharsets.UTF_8);

            System.arraycopy(
                    requestIdBytes,
                    0,
                    resultBytes,
                    0,
                    8
            );

            // Các byte còn lại
            System.arraycopy(
                    studentBytes,
                    0,
                    resultBytes,
                    8,
                    studentBytes.length
            );

            // =========================================
            // Gửi về server
            // =========================================

            DatagramPacket resultPacket =
                    new DatagramPacket(
                            resultBytes,
                            resultBytes.length,
                            receivePacket.getAddress(),
                            receivePacket.getPort()
                    );

            socket.send(resultPacket);

            System.out.println("Đã gửi Student đã xử lý.");

            // =========================================
            // d. Đóng socket
            // =========================================

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}