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