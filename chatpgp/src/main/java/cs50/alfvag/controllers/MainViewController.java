package cs50.alfvag.controllers;

import cs50.alfvag.models.AppModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MainViewController {
    private AppModel appModel;
    private AppController appController;
    private String username;

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

    @FXML
    private TextField textFieldUsername;

    @FXML
    private Button buttonSelectUsername;

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
            
        } else {
            return;
        }
    }

    @FXML
    private void clientToggleSelected() {

        if (clientToggle.isSelected()) {
            labelMode.setText("Mode: Client");

            textAreaChat.appendText("Client started...\n");
            textAreaChat.setDisable(false);
            textFieldMessage.setDisable(false);
            buttonSend.setDisable(false);

            mode = Mode.CLIENT;

        } else {
            return;
        }
    }

    @FXML
    private void buttonClickSend() {
        // Handle send button click
        String message = textFieldMessage.getText();
        if (!message.isEmpty() && mode == Mode.CLIENT) {
            textAreaChat.appendText("You: " + message + "\n");
            textFieldMessage.clear();
            //TODO Send message to server
            appController.sendMessage(message);
        }
    }

    @FXML
    private void buttonClickSelectUsername() {
        // Handle select username button click
        username = textFieldUsername.getText();
        if (!username.isEmpty() && username != null) {
            //TODO Send username to server
            //appController.sendMessage(username);
            textFieldUsername.setDisable(true);
            buttonSelectUsername.setDisable(true);
        }
    }

    public void reciveMessage(String message) {
        textAreaChat.appendText(message + "\n");
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }
}
