package View;

import Controller.Controller;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DictionaryDisplay extends View {


    public javafx.scene.control.TextArea ta_dictionaryDisplay;
    private Controller controller;
    private Stage stage;

    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        setText();
    }


    private void setText(){
        ta_dictionaryDisplay.setText(controller.dictionaryToString());
    }
}
