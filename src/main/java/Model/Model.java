package Model;

import FilesOperation.ReadFile;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {
    private ReadFile readFile;
    public Model() {

    }

    /**
     * create new index from given corpus path and save it to given index destination
     * @param path
     * @param fileKind
     * @param stemming
     */
    public void loadPath(String path, String fileKind, boolean stemming){
        //add filekind so it would know if its what corpus to parse or where to save the index
        //CHANGE
         readFile = new ReadFile(path,stemming);

    }

    //this func will delete all posting and dictionary files that have been saved
    //the posting files will be at the same path as been given while creating them
    //also need to clear main memory in the program
    public void resetAll() {
    }

    //DO THIS:
    //create a sorted list of the languages od the documents have been indexed
    //while parsing add each new language to the list and sort it by the A-Z order
    //mark each doc with his own language so it can be retrieve easily
    // then the func under should return this list of document languages
    //then the choice box needed to set all this list before "IndexOperations" window appearing

    //public SortedList getDocumentsLanguages() {
        //CHANGEEEEEEEEEEE CHANGE THIS HERE
        //SortedList<String>  languages= new SortedList<String>();
        //return languages;
    //}
}
