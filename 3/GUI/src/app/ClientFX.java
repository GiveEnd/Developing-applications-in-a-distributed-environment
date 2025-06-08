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
