package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class ChatClient extends Application {
    private BufferedReader in;
    private PrintWriter out;

    private TextArea messageArea;
    private TextField inputField;

    @Override
    public void start(Stage stage) {
        messageArea = new TextArea();
        messageArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setOnAction(e -> sendMessage());

        VBox layout = new VBox(10, new ScrollPane(messageArea), inputField);
        Scene scene = new Scene(layout, 400, 400);

        stage.setTitle("JavaFX Chat Client");
        stage.setScene(scene);
        stage.show();

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 90451);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        javafx.application.Platform.runLater(() -> 
                            messageArea.appendText(finalMessage + "\n")
                        );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.setDaemon(true);
            readerThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            inputField.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
