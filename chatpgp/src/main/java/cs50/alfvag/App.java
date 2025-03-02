package cs50.alfvag;

import cs50.alfvag.controllers.AppController;
import cs50.alfvag.models.AppModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AppModel appModel = new AppModel();
        AppController appController = new AppController(primaryStage, appModel);
        appController.showMainView();
    }
}