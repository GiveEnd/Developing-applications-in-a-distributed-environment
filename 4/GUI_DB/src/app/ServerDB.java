package app;

import java.io.*;
import java.net.*;
import java.sql.*;

public class ServerDB {
    private static final String URL = "jdbc:sqlite:data.db";

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Создание таблицы, если её нет
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS items (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер с БД запущен на порту " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("ADD:")) {
                    String itemName = message.substring(4);
                    addItem(itemName);
                } else if (message.startsWith("DELETE:")) {
                    String itemName = message.substring(7);
                    deleteItem(itemName);
                }

                // После любой команды отправление полного списка
                sendAllItems(out);
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addItem(String name) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO items (name) VALUES (?)");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteItem(String name) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM items WHERE name = ?");
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void sendAllItems(PrintWriter out) {
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM items");
            while (rs.next()) {
                out.println(rs.getString("name"));
            }
            out.println("END");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
