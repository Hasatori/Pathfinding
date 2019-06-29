package pathfinding;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import pathfinding.controller.MainController;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new MainController(primaryStage);
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
