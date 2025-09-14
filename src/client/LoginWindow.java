package client;

import db.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginWindow {

    public void start(Stage stage) {
        stage.setTitle("Login / Register");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        Label status = new Label();

        loginBtn.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();
            if (DatabaseHelper.validateUser(user, pass)) {
                status.setText("✅ Login successful!");
                new ChatWindow().start(stage, user); // open chat
            } else {
                status.setText("❌ Invalid credentials.");
            }
        });

        registerBtn.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();
            if (DatabaseHelper.registerUser(user, pass)) {
                status.setText("✅ Registered successfully. Now login.");
            } else {
                status.setText("❌ Username already exists.");
            }
        });

        VBox layout = new VBox(10, usernameField, passwordField, loginBtn, registerBtn, status);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 300, 200));
        stage.show();
    }
}
