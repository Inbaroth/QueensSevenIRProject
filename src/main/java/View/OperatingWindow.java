package View;

import Controller.Controller;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javax.swing.*;
import javafx.event.ActionEvent;

import java.awt.*;
import java.util.Optional;

public class OperatingWindow extends View{
    public javafx.scene.control.Button btn_browseCorpus;
    public javafx.scene.control.Button btn_browseIndexDestination;
    public javafx.scene.control.Button btn_DisplayDictionary;
    public javafx.scene.control.Button btn_UploadDictToMem;
    public javafx.scene.control.Button btn_resetAll;
    public javafx.scene.control.CheckBox cb_stemming;
    public javafx.scene.control.ComboBox cb_languageSelect;
    public javafx.scene.control.TextField tf_browseCorpus;
    public javafx.scene.control.TextField tf_browseIndexDestination;
    private String CorpusPath;
    private String indexSavingPath;
    private Controller controller;
    private Stage stage;
    private DictionaryDisplay dictionaryDisplay;

    public void setController(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public void loadPath(ActionEvent actionEvent){
            try
            {
                JButton b = new JButton();
                JFileChooser f = new JFileChooser();
                f.setPreferredSize(new Dimension(1500,1000));
                f.setDialogTitle("File chooser");
                setFileChooserFont(f.getComponents());
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
            String massage = "Number of documents have been indexed: " +controller.getNumberOfDocs() +"\n" +"Number of terms: " +controller.getNumberOfTerms() +"\n" + "Total time of creating index (seconds): " + controller.getTotalTimeToIndex();
            alert(massage, Alert.AlertType.INFORMATION);
            btn_DisplayDictionary.setDisable(false);
            btn_UploadDictToMem.setDisable(false);
            btn_resetAll.setDisable(false);
            ObservableList<String> languages = controller.getDocumentsLanguages();
            cb_languageSelect.setItems(languages);
            cb_languageSelect.setVisibleRowCount(languages.size()/2);
        }
    }

    public void resetValidation(ActionEvent actionEvent){
        //alert("Are you sure you want to reset all memory?", Alert.AlertType.CONFIRMATION);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Yes");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");
        alert.setContentText("Are you sure you want to reset all memory?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            controller.resetAll();
            alert("All memory have been reset", Alert.AlertType.INFORMATION);
        }else
            alert.close();
    }


    public void displayDictionary(ActionEvent actionEvent){
        newStage("DictionaryDisplay.fxml", "", dictionaryDisplay, 350, 560, controller);
    }

    public void uploadDictionaryToMem(ActionEvent actionEvent){
        controller.uploadDictionaryToMem();
        alert("The dictionary have been uploaded to memory." , Alert.AlertType.INFORMATION);
    }
}
