package cs50.alfvag;

import java.util.Scanner;

import cs50.alfvag.controllers.AppController;
import cs50.alfvag.models.AppModel;
import cs50.alfvag.models.ChatClient;
import cs50.alfvag.models.ChatServer;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Start (S)erver or (C)lient? ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if ("s".equals(choice)) {
            ChatServer.startServer();
        } else if ("c".equals(choice)) {
            ChatClient.startClient();
        } else {
            System.out.println("Invalid choice. Exiting.");
        }

        scanner.close();
    }

    @Override
    public void start(Stage primaryStage) {
        AppModel appModel = new AppModel();
        AppController appController = new AppController(primaryStage, appModel);
        appController.showMainView();
    }
}