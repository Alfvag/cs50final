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

    // Constructor to initialize the ChatClient
    public ChatClient(String serverAddress, int port, Consumer<String> messageListener) {
        try {
            // Establish connection to the server
            this.socket = new Socket(serverAddress, port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.messageListener = messageListener;

            // Start a new thread to listen for incoming messages
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to set the username
    public void setUsername(String username) {
        this.username = username;
        // Send a command to the server to set the username
        out.println("/setname " + username);
    }

    // Method to listen for incoming messages from the server
    private void listenForMessages() {
        try {
            String message;
            // Continuously read messages from the server
            while ((message = in.readLine()) != null) {
                // Pass the message to the message listener
                messageListener.accept(message);
            }
        } catch (IOException e) {
            System.out.println("Connection closed.");
        }
    }

    // Method to send a message to the server
    public void sendMessage(String message) {
        if (username == null) {
            // If username is not set, show an error message
            messageListener.accept("Error: Set your username first using setUsername()");
            return;
        }
        // Send the message to the server
        out.println(message);
    }

    // Method to disconnect from the server
    public void disconnect() {
        try {
            // Send a command to the server to quit and close the socket
            out.println("/quit");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}