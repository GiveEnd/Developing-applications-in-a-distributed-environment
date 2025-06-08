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
