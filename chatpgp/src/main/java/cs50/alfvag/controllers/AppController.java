package cs50.alfvag.controllers;

import cs50.alfvag.models.AppModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AppController {
    private final Stage primaryStage;
    private final AppModel appModel;

    public AppController(Stage primaryStage, AppModel appModel) {
        this.primaryStage = primaryStage;
        this.appModel = appModel;
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
            Parent root = loader.load();
            MainViewController mainController = loader.getController();
            mainController.setAppController(this);
            mainController.setAppModel(appModel);
            primaryStage.setScene(new Scene(root));
            //primaryStage.getIcons().add(new Image("logo.png"));
            primaryStage.setTitle("JavaChat");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reciveMessage(String message) {
        //TODO
    }

    public void sendMessage(String message) {
        //TODO
    }
}