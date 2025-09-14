import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 90451;
    private static Set<PrintWriter> clientWriters = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);

        try ( ServerSocket serverSocket = new ServerSocket(PORT) ) {
            while ( true ) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                new Thread(new ClientHandler(socket)).start();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clientWriters.add(out);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) { }
                clientWriters.remove(out);
            }
        }

        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }
}