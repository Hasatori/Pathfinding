package pathfinding.controller;


import pathfinding.view.DialogFactory;
import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Abstract controller class.
 */
public abstract class Controller {

    protected final Stage stage;

    /**
     * @param stage stage
     */
    public Controller(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(a -> {
            a.consume();
            Optional<ButtonType> result = DialogFactory.getConfirmDialog("Quiting application", "Do you really want to close the application?", "").showAndWait();
            if (result.get() == ButtonType.OK) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    /**
     * Method for loading a javafx view and starting a wait for messages.
     */
    public abstract void loadView();
}
