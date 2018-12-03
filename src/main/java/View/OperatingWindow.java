package View;

import Controller.Controller;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.event.ActionEvent;

import java.awt.*;
import javafx.*;

public class OperatingWindow extends View{
    public javafx.scene.control.Button btn_browseCorpus;
    public javafx.scene.control.Button btn_browseIndexDestination;
    public javafx.scene.control.CheckBox cb_stemming;
    public javafx.scene.control.TextField tf_browseCorpus;
    public javafx.scene.control.TextField tf_browseIndexDestination;
    private String CorpusPath;
    private String indexSavingPath;
    private Controller controller;
    private Stage stage;
    private IndexOperations indexOperations;

    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void loadPath(ActionEvent actionEvent){
            try
            {
                JButton b = new JButton();
                JFileChooser f = new JFileChooser();
                //f.setPreferredSize(new Dimension(1500,1000));
                f.setDialogTitle("File chooser");
                setFileChooserFont(f.getComponents());
                f.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (f.showOpenDialog(b) == JFileChooser.APPROVE_OPTION) {
                    System.out.println(f.getSelectedFile().getAbsolutePath());
                    Button sourceButton = (Button) actionEvent.getSource();
                    String btnID = sourceButton.getId();
                    if (btnID.equals("btn_browseCorpus")) {
                        tf_browseCorpus.setText(f.getSelectedFile().getAbsolutePath());
                    }
                    if (btnID.equals("btn_browseIndexDestination")) {
                        tf_browseIndexDestination.setText(f.getSelectedFile().getAbsolutePath());
                    }
                }
            }
            catch(
                    IllegalArgumentException e)
            {
                throw e;
            }
    }


    private void setFileChooserFont(Component[] comp)
    {
        for(int x = 0; x < comp.length; x++)
        {
            if(comp[x] instanceof Container) setFileChooserFont(((Container)comp[x]).getComponents());
            try{comp[x].setFont(new Font("calibri light", Font.PLAIN,40));}
            catch(Exception e){}//do nothing
        }
    }

    public void createIndex(ActionEvent actionEvent){
        tf_browseCorpus.setDisable(true);
        tf_browseIndexDestination.setDisable(true);
        if(tf_browseCorpus == null || tf_browseIndexDestination == null || tf_browseCorpus.getText().trim().isEmpty() || tf_browseIndexDestination.getText().trim().isEmpty()) {
            alert("One required Path or more is empty", Alert.AlertType.INFORMATION);
            tf_browseCorpus.setDisable(false);
            tf_browseIndexDestination.setDisable(false);
        } else{
            controller.loadPath(tf_browseCorpus.getText(), tf_browseIndexDestination.getText(), cb_stemming.isSelected());
            newStage("IndexOperations.fxml", "", indexOperations, 600, 400);
        }
    }
}
