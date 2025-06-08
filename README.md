# **Разработка приложений в распределенной среде**

## **Лабораторная работа №1** 
**Задача: разработать распределенное приложение с использованием технологии удаленного вызова методов.**

 - Клиент отправляет своё имя, сервер возвращает приветственное сообщение.

**Код:**

> Hello.java - удалённый интерфейс

    import java.rmi.Remote;  
    import java.rmi.RemoteException;  
    
    public interface Hello extends Remote {  
        String sayHello(String name) throws RemoteException;  
    }

> HelloImpl.java - реализация удаленного интерфейса	

    import java.rmi.server.UnicastRemoteObject;  
    import java.rmi.RemoteException;  
      
    public class HelloImpl extends UnicastRemoteObject implements Hello {  
      
        protected HelloImpl() throws RemoteException {  
            super();  
        }  
      
        @Override  
      public String sayHello(String name) throws RemoteException {  
            return "Привет, " + name + "! Добро пожаловать на сервер RMI.";  
        }  
    }

> Server.java - сервер создаёт объект HelloImpl, запускает реестр RMI на порту 1099, регистрирует сервис под именем HelloService

    import java.rmi.registry.LocateRegistry;  
    import java.rmi.registry.Registry;  
      
    public class Server {  
        public static void main(String[] args) {  
            try {  
                // Создание удалённого объекта  
      HelloImpl helloService = new HelloImpl();  
      
                // Запуск реестра RMI на порту  
      LocateRegistry.createRegistry(1099);  
      
                // Регистрация удалённого объекта в реестре  
      Registry registry = LocateRegistry.getRegistry();  
                registry.rebind("HelloService", helloService);  
      
                System.out.println("RMI сервер запущен и ждёт клиентов...");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }
 

> Client.java - клиент подключается к реестру RMI, получает удалённый объект по имени и вызывает его метод sayHello

    import java.rmi.registry.LocateRegistry;  
    import java.rmi.registry.Registry;  
      
    public class Client {  
        public static void main(String[] args) {  
            try {  
                // Подключение к реестру RMI по адресу 
      Registry registry = LocateRegistry.getRegistry("localhost", 1099);  
      
                // Получение объекта Hello из реестра по имени 
      Hello stub = (Hello) registry.lookup("HelloService");  
      
                // Вызыв удалённого метода  
      String response = stub.sayHello("Владимир");  
                System.out.println("Ответ от сервера: " + response);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }
    
**Результат:**

![1](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/1/Result/1.png?raw=true)

## **Лабораторная работа №2** 
**Задача: разработать консольное клиент-серверное приложение.**

 - Клиент подключается к серверу, отправляет текстовое сообщение, сервер получает его и отправляет ответ.

**Код:**

> Server.java - ожидает подключение клиента по сети и обрабатывает его сообщение

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

> Client.java - подключение к серверу по сети, отправление сообщения и ожидание ответа

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

**Результат:**

![1](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/2/Result/1.png?raw=true)

![2](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/2/Result/2.png?raw=true)
## **Лабораторная работа №3** 
**Задача: разработать клиент-серверное приложение с графическим пользовательским интерфейсом.**

 - Клиент с GUI подключается к серверу, отправляет сообщение, сервер получает его и отправляет ответ.

**Код:**

> Server.java - ожидает подключение клиента по сети и обрабатывает его сообщение

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

> ClientFX.java - клиентская часть с графическим интерфейсом JavaFX

    package app;  
      
    import javafx.application.Application;  
    import javafx.application.Platform;  
    import javafx.scene.Scene;  
    import javafx.scene.control.Button;  
    import javafx.scene.control.TextArea;  
    import javafx.scene.control.TextField;  
    import javafx.scene.layout.VBox;  
    import javafx.stage.Stage;  
      
    import java.io.BufferedReader;  
    import java.io.InputStreamReader;  
    import java.io.PrintWriter;  
    import java.net.Socket;  
      
    public class ClientFX extends Application {  
        private PrintWriter out;  
        private BufferedReader in;  
        private TextArea textArea;  
      
        public static void main(String[] args) {  
            launch(args);  
        }  
      
        @Override  
      public void start(Stage primaryStage) {  
            primaryStage.setTitle("JavaFX клиент");  
      
            TextField inputField = new TextField();  
            Button sendButton = new Button("Отправить");  
            textArea = new TextArea();  
            textArea.setEditable(false);  
      
            VBox root = new VBox(10, textArea, inputField, sendButton);  
            primaryStage.setScene(new Scene(root, 350, 300));  
            primaryStage.show();  
      
            new Thread(this::connectToServer).start();  
      
            sendButton.setOnAction(e -> {  
                String message = inputField.getText();  
                if (out != null && !message.isEmpty()) {  
                    out.println(message);  
                    inputField.clear();  
                }  
            });  
        }  
      
        private void connectToServer() {  
            try {  
                Socket socket = new Socket("localhost", 12345);  
                out = new PrintWriter(socket.getOutputStream(), true);  
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
      
                String response;  
                while ((response = in.readLine()) != null) {  
                    String finalResponse = response;  
                    Platform.runLater(() -> textArea.appendText(finalResponse + "\n"));  
                }  
      
                socket.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }

**Результат:**

![1](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/3/Result/1.png?raw=true)

![2](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/3/Result/2.png?raw=true)

## **Лабораторная работа №4** 
**Задача: разработать клиент-серверное приложение с графическим пользовательским интерфейсом для доступа к базе данных.**

 - Сервер принимает команды от клиента, работает с SQLite (добавляет, читает данные) и отправляет клиенту результаты.
 - Клиент с GUI с текстовым полем и кнопкой для добавления записи, и ListView для отображения списка из базы.

**Код:**

> ServerDB.java - сервер с базой данных

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

> ClientDBFX.java - клиентская часть с графическим интерфейсом JavaFX

    package app;  
      
    import javafx.application.Application;  
    import javafx.application.Platform;  
    import javafx.collections.FXCollections;  
    import javafx.collections.ObservableList;  
    import javafx.scene.Scene;  
    import javafx.scene.control.*;  
    import javafx.scene.layout.VBox;  
    import javafx.stage.Stage;  
      
    import java.io.*;  
    import java.net.Socket;  
      
    public class ClientDBFX extends Application {  
        private PrintWriter out;  
        private BufferedReader in;  
        private ListView<String> listView;  
      
        public static void main(String[] args) {  
            launch(args);  
        }  
      
        @Override  
        public void start(Stage primaryStage) {  
            primaryStage.setTitle("Клиент с БД");  
      
            TextField inputField = new TextField();  
            Button addButton = new Button("Добавить");  
            Button deleteButton = new Button("Удалить выбранное");  
            listView = new ListView<>();  
      
            VBox root = new VBox(10, new Label("Введите элемент:"), inputField, addButton, deleteButton, new Label("Список из базы:"), listView);  
            primaryStage.setScene(new Scene(root, 300, 400));  
            primaryStage.show();  
      
            new Thread(this::connectToServer).start();  
      
            addButton.setOnAction(e -> {  
                String name = inputField.getText();  
                if (out != null && !name.isEmpty()) {  
                    out.println("ADD:" + name);  
                    inputField.clear();  
                }  
            });  
      
            deleteButton.setOnAction(e -> {  
                String selected = listView.getSelectionModel().getSelectedItem();  
                if (selected != null && out != null) {  
                    out.println("DELETE:" + selected);  
                }  
            });  
        }  
      
        private void connectToServer() {  
            try (Socket socket = new Socket("localhost", 12345)) {  
                out = new PrintWriter(socket.getOutputStream(), true);  
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
      
                out.println("LIST");  
      
                String response;  
                while ((response = in.readLine()) != null) {  
                    ObservableList<String> items = FXCollections.observableArrayList();  
      
                    while (response != null && !response.equals("END")) {  
                        items.add(response);  
                        response = in.readLine();  
                    }  
      
                    Platform.runLater(() -> {  
                        listView.getItems().clear();  
                        listView.getItems().addAll(items);  
                    });  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }

**Результат:**

![1](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/4/Result/1.png?raw=true)
![2](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/4/Result/2.png?raw=true)
![3](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/4/Result/3.png?raw=true)
![4](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/4/Result/4.png?raw=true)
![5](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/4/Result/5.png?raw=true)
![6](https://github.com/GiveEnd/Developing-applications-in-a-distributed-environment/blob/main/4/Result/6.png?raw=true)
