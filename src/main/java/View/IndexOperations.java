package View;

import Controller.Controller;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class IndexOperations extends View {
    public javafx.scene.control.ComboBox cb_languageSelect;
    private Controller controller;
    private Stage stage;

    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void setCb_languageSelect(SortedList languages){
        cb_languageSelect.setItems(languages);
    }

    public void resetValidation(ActionEvent actionEvent){
        //alert("Are you sure you want to reset all memory?", Alert.AlertType.CONFIRMATION);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
        alert.setContentText("Are you sure you want to reset all memory?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            // ... user chose OK
            controller.resetAll();
        else
            alert.close();
    }
}
