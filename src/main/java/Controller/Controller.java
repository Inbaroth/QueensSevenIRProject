package Controller;

import Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

public class Controller extends Observable implements Observer {

    private Model model;
    public Controller(Model model) { this.model = model; }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model){
            setChanged();
            notifyObservers(arg);
        }
    }

    /**
     * load the path of the corpus&stop-words and the path of where to save the index
     * @param pathOfCorpus - path to source  file of the corpus and stop-words list
     * @param pathOfIndexDestination path to where the index should be saved
     * @param stemming - boolean if doing stemming on index terms or not
     */
    public void loadPath(String pathOfCorpus, String pathOfIndexDestination, boolean stemming){
        try{
            model.loadPath(pathOfCorpus, pathOfIndexDestination, stemming);
        }
        catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Oops..");
            alert.setContentText("The chosen file isn't in the right format");
            alert.showAndWait();
            alert.close();
        }
    }

    /**
     * @return the corpus's documents languages
     */
    public ObservableList<String> getDocumentsLanguages(){
        TreeSet<String> docsLang = model.getDocumentsLanguages();
        //casting
        ObservableList<String> docLangObservable = FXCollections.observableArrayList(docsLang);
        return docLangObservable;
    }

    /**
     * clear all memory of the program, ram and disk
     */
    public void resetAll(){
        model.resetAll();
    }

    /**
     * @return the dictionary in the format "term, numberOfAppearance" as a continues string
     */
    public String dictionaryToString() {
       return model.dictionaryToString();
    }

    /**
     * upload the dictionary of index from it's source file in disk to the ram
     */
    public void uploadDictionaryToMem() {
        model.uploadDictionaryToMem();
    }

    /**
     * @return number of docs have been indexed
     */
    public int getNumberOfDocs(){
        return model.getNumberOfDocs();
    }

    /**
     * @return number of terms in index
     */
    public int getNumberOfTerms(){
       return model.getNumberOfTerms();
    }

    /**
     * @return total time in seconds to create index
     */
    public double getTotalTimeToIndex(){
        return model.getTotalTimeToIndex();
    }
}
