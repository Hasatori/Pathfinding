package pathfinding.view;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

/**
 * Simple class for creating dialog windows.
 */
@SuppressWarnings("Duplicates")
public class DialogFactory {

    private DialogFactory(){

    }

    /**
     * Creates alert dialog window.
     * @param alertType alert type
     * @param title dialog window title
     * @param content content of dialog window
     * @return alert window
     */
    public static Alert getAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Creates confirm dialog window.
     * @param title dialog window title
     * @param content content of dialog window
     * @param header header text of dialog window
     * @return alert window
     */
    public static Alert getConfirmDialog(String title, String content, String header) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        return alert;
    }

    /**
     * Creates text input dialog window.
     * @param title dialog window title
     * @param headerText header text of dialog window
     * @param contextText content text of dialog window
     * @return alert window
     */
    public static TextInputDialog getTextInputDialog(String title,String headerText,String contextText){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contextText);
        return dialog;
    }
}
