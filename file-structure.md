ChatApp/
│
├── src/
│   ├── server/
│   │   ├── ChatServer.java
│   │   ├── ClientHandler.java
│   │   └── DatabaseManager.java
│   │
│   ├── client/
│   │   ├── ChatClient.java
│   │   ├── LoginController.java
│   │   ├── RegisterController.java
│   │   ├── ChatController.java
│   │   └── EncryptionUtils.java
│   │
│   ├── model/
│   │   └── User.java
│   │
│   ├── util/
│   │   ├── Message.java
│   │   └── FileUtils.java
│   │
│   └── Main.java
│
├── resources/
│   ├── fxml/
│   │   ├── login.fxml
│   │   ├── register.fxml
│   │   └── chat.fxml
│   │
│   └── styles/
│       └── style.css
│
├── lib/   (optional: sqlite-jdbc, gson jars if not using Maven/Gradle)
│
├── chat.db   (SQLite database file, auto-created)
│
└── README.md
