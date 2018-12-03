package Model;

import FilesOperation.ReadFile;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Observable;

public class Model extends Observable {
    private ReadFile readFile;
    private long startTime;
    private long endTime;
    private double totalSecondsToIndex;
    public Model() {

    }

    /**
     * create new index from given corpus path and save it to given index destination
     * @param pathOfCorpus
     * @param pathOfIndexDestination
     * @param stemming
     */
    public void loadPath(String pathOfCorpus, String pathOfIndexDestination, boolean stemming){
        //add filekind so it would know if its what corpus to parse or where to save the index

        startTime = System.nanoTime();
        //CHANGE
         readFile = new ReadFile(pathOfCorpus,pathOfIndexDestination,stemming);
         endTime = System.nanoTime();
         totalSecondsToIndex = (endTime-startTime) / 1000000000.0;
    }

    //this func will delete all posting and dictionary files that have been saved
    //the posting files will be at the same path as been given while creating them
    //also need to clear main memory in the program
    public void resetAll() {
        readFile.getParser().getIndexer().reset();
        readFile = null;
        System.gc();
    }

    /**
     * this func will return list of all documents languages sorted
     * @return
     */
    public ArrayList<String> getDocumentsLanguages() {
       return readFile.getParser().getCorpusLanguages();
    }

    public String dictionaryToString() {
        return readFile.getParser().getIndexer().toString();
        //create a func which return a string which represent the dictionary SORTED like this:
        // term, numberOfPerformances
        // dad, 50
        // family, 12
        // Mom, 52
    }


    public void uploadDictionaryToMem() {
    //create a func which will take the dictionary from path where posting files and dict is saved
    //and upload the dict itself to main memory
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

    public int getNumberOfDocs(){
       return readFile.getParser().getNumberOfDocuments();
    }

    public int getNumberOfTerms(){
        return readFile.getParser().getIndexer().dictionary.size();
    }

    public double getTotalTimeToIndex(){
        return totalSecondsToIndex;
    }
}
