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
