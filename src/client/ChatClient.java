package client;

import db.DatabaseHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class ChatClient extends Application {

    @Override
    public void start(Stage stage) {
        // ✅ Start with Login Window instead of chat
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.start(stage);
    }

    public static void main(String[] args) {
        // ✅ Initialize SQLite database first
        DatabaseHelper.initialize();

        // ✅ Launch JavaFX app
        launch(args);
    }
}
