package cs50.alfvag.controllers;

import cs50.alfvag.models.AppModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AppController {
    private final Stage primaryStage;
    private final AppModel appModel;
    private MainViewController mainViewController;

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
            this.mainViewController = mainController;
            mainController.setAppController(this);
            mainController.setAppModel(appModel);
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("JavaChat");
            primaryStage.show();
            showUsernameView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showUsernameView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/UsernameView.fxml"));
            Stage modalStage = new Stage();
            modalStage.setScene(new Scene(loader.load()));
            
            UsernameViewController controller = loader.getController();
            controller.setAppModel(appModel);
            //controller.setAppModel(appModel);

            modalStage.setTitle("Select username");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            Stage currStage = (Stage) primaryStage.getScene().getWindow();
            modalStage.initOwner(currStage);
            modalStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}