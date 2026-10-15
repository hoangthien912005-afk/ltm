import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class bai6 {

    public static void main(String[] args) {

        String serverIP = "36.50.135.242";
        int port = 2207;

        String studentCode = "B23DCCN784";
        String qCode = "bX4KEHC7";

        try (Socket socket = new Socket(serverIP, port)) {

            // Thời gian giao tiếp tối đa 5 giây
            socket.setSoTimeout(5000);

            DataInputStream in =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream out =
                    new DataOutputStream(socket.getOutputStream());

            // =====================================
            // a. Gửi mã sinh viên và mã câu hỏi
            // =====================================

            String request = studentCode + ";" + qCode;

            out.writeUTF(request);
            out.flush();

            System.out.println("Đã gửi: " + request);

            // =====================================
            // b. Nhận chuỗi mã hóa và giá trị s
            // =====================================

            String encrypted = in.readUTF();
            int s = in.readInt();

            System.out.println("Chuỗi mã hóa: " + encrypted);
            System.out.println("Giá trị dịch chuyển: " + s);

            // =====================================
            // c. Giải mã Caesar
            // =====================================

            StringBuilder decrypted = new StringBuilder();

            for (char c : encrypted.toCharArray()) {

                if (c >= 'A' && c <= 'Z') {

                    char result = (char) (
                            (c - 'A' - s + 26) % 26 + 'A'
                    );

                    decrypted.append(result);

                } else if (c >= 'a' && c <= 'z') {

                    char result = (char) (
                            (c - 'a' - s + 26) % 26 + 'a'
                    );

                    decrypted.append(result);

                } else {

                    // Giữ nguyên số, khoảng trắng, ký tự đặc biệt
                    decrypted.append(c);
                }
            }

            String result = decrypted.toString();

            System.out.println("Chuỗi sau khi giải mã: " + result);

            // =====================================
            // Gửi chuỗi ban đầu về Server
            // =====================================

            out.writeUTF(result);
            out.flush();

            System.out.println("Đã gửi kết quả về Server.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
// Mật mã caesar, còn gọi là mật mã dịch chuyển, để giải mã thì mỗi ký tự nhận được sẽ được thay thế bằng một ký tự cách nó một đoạn s.

Ví dụ: với s = 3 thì ký tự A sẽ được thay thế bằng ký tự D

Một chương trình server cho phép kết nối qua giao thức TCP tại cổng 2207 (hỗ trợ thời gian giao tiếp tối đa cho mỗi yêu cầu là 5s). Yêu cầu là xây dựng chương trình client tương tác với server trên, sử dụng các luồng byte (DataInputStream/DataOutputStream) để trao đổi thông tin theo thứ tự:

a. Gửi một chuỗi gồm mã sinh viên và mã câu hỏi theo định dạng studentCode;qCode.

Ví dụ: B15DCCN999;D68C93F7

b. Nhận lần lượt chuỗi đã bị mã hóa caesar và giá trị dịch chuyển s nguyên

c. Thực hiện giải mã ra thông điệp ban đầu và gửi lên Server

d. Đóng kết nối và kết thúc chương trình.