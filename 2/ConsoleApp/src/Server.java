import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен, ожидает клиента на порту " + port + "...");

            // Ожидание подключения клиента
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

            // Потоки для обмена сообщениями
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Чтение сообщения от клиента
            String messageFromClient = in.readLine();
            System.out.println("Получено от клиента: " + messageFromClient);

            // Отправка ответа
            out.println("Сервер получил: " + messageFromClient);

            // Закрытие соединения
            clientSocket.close();
            System.out.println("Соединение закрыто.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
