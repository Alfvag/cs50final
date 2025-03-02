package cs50.alfvag.controllers;

import cs50.alfvag.App;
import cs50.alfvag.models.AppModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UsernameViewController {
    private AppModel appModel;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private Button buttonSelect;

    private MainViewController mainViewController;

    @FXML
    private void buttonClickSelect() {
        // Handle select button click
        String username = textFieldUsername.getText();
        if (username != null && !username.isEmpty()) {
            // Pass the username to the main view controller
            appModel.setUsername(username);

            // Close the username selection window
            Stage stage = (Stage) buttonSelect.getScene().getWindow();
            stage.close();
        }
    }

    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }
}