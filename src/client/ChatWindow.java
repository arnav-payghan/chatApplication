package client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;

public class ChatWindow {
    private BufferedReader in;
    private PrintWriter out;

    private TextArea messageArea;
    private TextField inputField;

    public void start(Stage stage, String username) {
        messageArea = new TextArea();
        messageArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setOnAction(e -> sendMessage(username));

        VBox layout = new VBox(10, new ScrollPane(messageArea), inputField);
        Scene scene = new Scene(layout, 400, 400);

        stage.setTitle("JavaFX Chat - " + username);
        stage.setScene(scene);
        stage.show();

        connectToServer(username);
    }

    private void connectToServer(String username) {
        try {
            Socket socket = new Socket("localhost", 1276);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send username to server as first message
            out.println(username + " has joined the chat!");

            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        Platform.runLater(() -> 
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

    private void sendMessage(String username) {
        String message = inputField.getText();
        if (!message.isEmpty() && out != null) {
            out.println("[ "+ username +" ]" + " : " + message);
            db.DatabaseHelper.incrementMessageCount(username); // ✅ increase count
            inputField.clear();
        }
    }
}
