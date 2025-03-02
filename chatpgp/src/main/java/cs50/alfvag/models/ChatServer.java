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

                // Wait for client to send username command before accepting messages
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/setname ")) {
                        setUsername(message.substring(9).trim());
                    } else if (username == null) {
                        out.println("Error: Set your username using /setname <username>");
                    } else if (message.equalsIgnoreCase("/quit")) {
                        break;
                    } else if (message.startsWith("@")) {
                        sendPrivateMessage(message);
                    } else {
                        broadcast(message, username);
                    }
                }

                disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set the client's username
        private void setUsername(String newUsername) {
            synchronized (clients) {
                if (clients.containsKey(newUsername) || newUsername.isEmpty()) {
                    out.println("Error: Username taken or invalid.");
                    return;
                }

                if (this.username != null) {
                    clients.remove(this.username);
                }

                this.username = newUsername;
                clients.put(username, out);
                out.println("Username set to: " + username);
                broadcast("User " + username + " joined!", "Server");
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