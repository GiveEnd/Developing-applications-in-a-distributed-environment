package app;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port + ", ждёт клиента...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Получено от клиента: " + message);
                out.println("Сервер принял: " + message);
                if (message.equalsIgnoreCase("exit")) break;
            }

            clientSocket.close();
            System.out.println("Соединение закрыто.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
