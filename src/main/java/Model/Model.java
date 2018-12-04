package Model;

import FilesOperation.ReadFile;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.Observable;
import java.util.TreeSet;

public class Model extends Observable {
    private ReadFile readFile;
    private long startTime;
    private long endTime;
    private double totalSecondsToIndex;
    public Model() {

    }

    /**
     * create new index from given corpus path and save it to given index destination
     * @param pathOfCorpus - path to the source file of the corpus & stop-words list
     * @param pathOfIndexDestination - path to the directory whre the user wants to save the file
     * @param stemming - boolean, if doing stemming when indexing or not
     */
    public void loadPath(String pathOfCorpus, String pathOfIndexDestination, boolean stemming){
        //calculate the time it takes to create the index
        startTime = System.nanoTime();
        //CHANGE
         readFile = new ReadFile(pathOfCorpus,pathOfIndexDestination,stemming);
         endTime = System.nanoTime();
         totalSecondsToIndex = (endTime-startTime) / 1000000000.0;
    }



    /**
     * this func will delete all posting and dictionary files that have been saved
     *  the posting files will be at the same path as been given while creating them
     *  also need to clear main memory in the program
     */
    public void resetAll() {
        readFile.getParser().getIndexer().reset();
        readFile = null;
        System.gc();
    }

    /**
     * this func will return list of all documents languages sorted
     * @return
     */
    public TreeSet<String> getDocumentsLanguages() {
       return readFile.getParser().getCorpusLanguages();
    }

    /**
     * a func which return a string which represent the dictionary SORTED like this:
     * term, numberOfPerformances
     * dad, 50
     * family, 12
     * Mom, 52
     * @return
     */
    public String dictionaryToString() {
        return readFile.getParser().getIndexer().toString();

    }

    /**
     * upload the dictionary from theDestinationPath where posting files and dict is saved to RAM
     */
    public void uploadDictionaryToMem() {
        readFile.getParser().getIndexer().setDictionary();
    }

    /**
     * @return number of documents in corpus
     */
    public int getNumberOfDocs(){
       return readFile.getParser().getNumberOfDocuments();
    }

    /**
     * @return number of terms in dictionary
     */
    public int getNumberOfTerms(){
        return readFile.getParser().getIndexer().getDictionary().size();
    }

    /**
     * @return the time it takes to create the index in seconds
     */
    public double getTotalTimeToIndex(){
        return totalSecondsToIndex;
    }
}
