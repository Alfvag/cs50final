package cs50.alfvag.controllers;

import cs50.alfvag.models.AppModel;
import cs50.alfvag.models.ChatClient;
import cs50.alfvag.models.ChatServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MainViewController {
    private AppModel appModel;
    private ChatClient chatClient = null;

    // Enum to represent the mode of the application
    enum Mode {
        SERVER,
        CLIENT,
        NOTSELECTED
    }

    // Initial mode is NOTSELECTED
    Mode mode = Mode.NOTSELECTED;

    @FXML
    private TextArea textAreaChat;

    @FXML
    private TextField textFieldMessage;

    @FXML
    private Button buttonSend;

    @FXML
    private RadioMenuItem serverToggle;

    @FXML
    private RadioMenuItem clientToggle;

    @FXML
    private Label labelMode;

    private ToggleGroup modeToggleGroup;

    // Initialize method to set up the ToggleGroup for the RadioMenuItems
    @FXML
    private void initialize() {
        modeToggleGroup = new ToggleGroup();
        serverToggle.setToggleGroup(modeToggleGroup);
        clientToggle.setToggleGroup(modeToggleGroup);
    }

    // Method to handle server toggle selection
    @FXML
    private void serverToggleSelected() {
        if (serverToggle.isSelected()) {
            labelMode.setText("Mode: Server");

            textAreaChat.clear();
            textAreaChat.appendText("Server started...\n");
            textAreaChat.setDisable(true);
            textFieldMessage.clear();
            textFieldMessage.setDisable(true);
            buttonSend.setDisable(true);

            mode = Mode.SERVER;
            startServerInNewThread();
        } else {
            return;
        }
    }

    // Method to handle client toggle selection
    @FXML
    private void clientToggleSelected() {
        if (clientToggle.isSelected()) {
            labelMode.setText("Mode: Client Username: " + appModel.getUsername());

            textAreaChat.appendText("Client started...\n");
            textAreaChat.setDisable(false);
            textFieldMessage.setDisable(false);
            buttonSend.setDisable(false);

            mode = Mode.CLIENT;

            chatClient = new ChatClient("localhost", 12345, this::updateChat);
            chatClient.setUsername(appModel.getUsername());
        } else {
            return;
        }
    }

    // Method to handle send button click
    @FXML
    private void buttonClickSend() {
        String message = textFieldMessage.getText();

        // Check that the message is not empty and the mode is CLIENT
        
        if (!message.isEmpty() && mode == Mode.CLIENT) {
            textFieldMessage.clear();

            // Check if the message is a private message and "send" it to yourself, since it isnt broadcasted back to you

            if (message.startsWith("@")) {
                textAreaChat.appendText(appModel.getUsername() + ": [Private] " + message + "\n");
            }

            chatClient.sendMessage(message);
        }
    }

    // Method to update the chat area with new messages
    private void updateChat(String message) {
        Platform.runLater(() -> textAreaChat.appendText(message + "\n"));
    }

    // Method to set the AppModel instance
    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    // Method to start the server in a new thread
    private void startServerInNewThread() {
        Thread serverThread = new Thread(() -> {
            ChatServer.startServer();
        });

        serverThread.setDaemon(true); // Ensures the server thread stops when the app closes
        serverThread.start();
    }
}
