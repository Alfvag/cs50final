package cs50.alfvag.models;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> messageListener;
    private String username;

    public ChatClient(String serverAddress, int port, Consumer<String> messageListener) {
        try {
            this.socket = new Socket(serverAddress, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.messageListener = messageListener;

            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to set username
    public void setUsername(String username) {
        this.username = username;
        out.println("/setname " + username);
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                messageListener.accept(message);
            }
        } catch (IOException e) {
            System.out.println("Connection closed.");
        }
    }

    public void sendMessage(String message) {
        if (username == null) {
            messageListener.accept("Error: Set your username first using setUsername()");
            return;
        }
        out.println(message);
    }

    public void disconnect() {
        try {
            out.println("/quit");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}