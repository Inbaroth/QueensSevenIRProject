package View;

import Controller.Controller;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * represent the stage on the dictionary display
 */
public class DictionaryDisplay extends View {


    public javafx.scene.control.TextArea ta_dictionaryDisplay;
    private Controller controller;
    private Stage stage;

    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        setText();
    }

    /**
     * sets the dictionary content in the right place in the stage
     */
    private void setText(){
        if(controller.dictionaryToString()!=null)
            ta_dictionaryDisplay.setText(controller.dictionaryToString());
    }
}
