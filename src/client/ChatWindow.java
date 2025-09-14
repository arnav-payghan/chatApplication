package client;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.net.*;

public class ChatWindow {
    private BufferedReader in;
    private PrintWriter out;

    private TextFlow messageArea;   // ✅ Changed from TextArea to TextFlow for colored usernames
    private TextField inputField;

    public void start(Stage stage, String username) {
        messageArea = new TextFlow();
        messageArea.setPrefHeight(350); // ✅ set preferred height for chat area

        inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setOnAction(e -> sendMessage(username));

        ScrollPane scrollPane = new ScrollPane(messageArea); // ✅ wrap in ScrollPane
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        VBox layout = new VBox(10, scrollPane, inputField);
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

            // Send join message
            out.println(username + " has joined the chat!");

            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        Platform.runLater(() -> displayMessage(finalMessage)); // ✅ call custom display method
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

    // ✅ New method: separates username (colored) from message (black)
    private void displayMessage(String message) {
        String[] parts = message.split(":", 2); // split "username : message"
        
        if (parts.length == 2) {
            String usernamePart = parts[0].trim();
            String textPart = parts[1].trim();

            Text userText = new Text(usernamePart + ": ");
            userText.setStyle("-fx-fill: blue; -fx-font-weight: bold;");

            Text messageText = new Text(textPart + "\n");
            messageText.setStyle("-fx-fill: black;");

            messageArea.getChildren().addAll(userText, messageText);
        } else {
            // fallback: if message has no ":" (e.g., system messages)
            Text messageText = new Text(message + "\n");
            messageText.setStyle("-fx-fill: gray;");
            messageArea.getChildren().add(messageText);
        }
    }

    private void sendMessage(String username) {
        String message = inputField.getText();
        if (!message.isEmpty() && out != null) {
            out.println("[ " + username + " ] : " + message); // ✅ keep same format
            db.DatabaseHelper.incrementMessageCount(username); // increment count
            inputField.clear();
        }
    }
}
