import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.print("Введите сообщение для сервера: ");
            String message = consoleInput.readLine();

            // Отправление сообщения серверу
            out.println(message);

            // Чтение ответа от сервера
            String response = in.readLine();
            System.out.println("Ответ от сервера: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
