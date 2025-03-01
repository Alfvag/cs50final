package cs50.alfvag.models;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Map<String, PrintWriter> clients = new HashMap<>();

    public static void startServer() {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Enter your username:");
                username = in.readLine().trim();

                synchronized (clients) {
                    while (clients.containsKey(username) || username.isEmpty()) {
                        out.println("Username taken. Enter another:");
                        username = in.readLine().trim();
                    }
                    clients.put(username, out);
                }

                broadcast("User " + username + " joined!", "Server");

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("@")) {
                        sendPrivateMessage(message);
                    } else {
                        broadcast(message, username);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private void sendPrivateMessage(String message) {
            int firstSpace = message.indexOf(" ");
            if (firstSpace == -1) {
                out.println("Invalid format. Use @username message");
                return;
            }
            String targetUser = message.substring(1, firstSpace);
            String privateMessage = message.substring(firstSpace + 1);

            synchronized (clients) {
                PrintWriter targetOut = clients.get(targetUser);
                if (targetOut != null) {
                    targetOut.println("[Private] " + username + ": " + privateMessage);
                } else {
                    out.println("User not found.");
                }
            }
        }

        private void broadcast(String message, String sender) {
            synchronized (clients) {
                for (PrintWriter writer : clients.values()) {
                    writer.println(sender + ": " + message);
                }
            }
        }

        private void disconnect() {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clients) {
                clients.remove(username);
                broadcast("User " + username + " left.", "Server");
            }
        }
    }
}