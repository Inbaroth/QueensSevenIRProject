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


    public ObservableList<String> getDocumentsLanguages(){


        TreeSet<String> docsLang = model.getDocumentsLanguages();
        ObservableList<String> docLangObservable = FXCollections.observableArrayList(docsLang);

        //HERE


        //ObservableList<String> docLangObserv = FXCollections.observableArrayList(Lang);
        //ObservableSet<String> docLangObservable= FXCollections.observableSet(docsLang);

        return docLangObservable;
    }

    public void resetAll(){
        model.resetAll();
    }


    public String dictionaryToString() {
        return model.dictionaryToString();
    }

    public void uploadDictionaryToMem() {
        model.uploadDictionaryToMem();
    }

    /**
     * return number of docs have been indexed
     * @return
     */
    public int getNumberOfDocs(){
        return model.getNumberOfDocs();
    }

    /**
     * return number of terms in index
     * @return
     */
    public int getNumberOfTerms(){
        return model.getNumberOfTerms();
    }

    /**
     * return total time in seconds to create index
     * @return
     */
    public double getTotalTimeToIndex(){
        return model.getTotalTimeToIndex();
    }
}
