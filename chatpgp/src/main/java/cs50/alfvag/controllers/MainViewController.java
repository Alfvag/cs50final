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

    enum Mode {
        SERVER,
        CLIENT,
        NOTSELECTED
    }

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

    @FXML
    private void initialize() {
        modeToggleGroup = new ToggleGroup();
        serverToggle.setToggleGroup(modeToggleGroup);
        clientToggle.setToggleGroup(modeToggleGroup);
    }

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

    @FXML
    private void buttonClickSend() {
        // Handle send button click
        String message = textFieldMessage.getText();
        
        if (!message.isEmpty() && mode == Mode.CLIENT) {
            textFieldMessage.clear();

            if (message.startsWith("@")) {
                textAreaChat.appendText(appModel.getUsername() + ": [Private] " + message + "\n");
            }

            chatClient.sendMessage(message);
        }
    }

    private void updateChat(String message) {
        Platform.runLater(() -> textAreaChat.appendText(message + "\n"));
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    private void startServerInNewThread() {
        Thread serverThread = new Thread(() -> {
            ChatServer.startServer();
        });

        serverThread.setDaemon(true); // Ensures the server thread stops when the app closes
        serverThread.start();
    }
}
