package FilesOperation;

import java.util.ArrayList;

/**
 * This class saves all the details of each document in the corpus
 */
public class DocumentDetails {

    String docId;
    String fileName;
    String cityName;
    String language;
    String date;
    int termFrequency;
    int maxTermFrequency;
    int numberOfDistinctWords;
    ArrayList<Integer> listOfPositions;

    /**
     * Constructor
     * @param docId
     * @param fileName
     */
    public DocumentDetails(String docId, String fileName) {
        this.docId = docId;
        this.fileName = fileName;
        this.listOfPositions = new ArrayList<>();
    }


    //<editor-fold desc="Getters">
    public String getDocId() {
        return docId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getLanguage() {
        return language;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public int getMaxTermFrequency() {
        return maxTermFrequency;
    }

    public int getNumberOfDistinctWords() {
        return numberOfDistinctWords;
    }

    public ArrayList<Integer> getListOfPositions() {
        return listOfPositions;
    }

    //</editor-fold>


    //<editor-fold desc="Setters">
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public void setMaxTermFrequency(int maxTermFrequency) {
        this.maxTermFrequency = maxTermFrequency;
    }

    public void setNumberOfDistinctWords(int numberOfDistinctWords) {
        this.numberOfDistinctWords = numberOfDistinctWords;
    }

    public void setPosition (int position){
        this.listOfPositions.add(position);
    }

    public void setDate(String date) {
        this.date = date;
    }

    //</editor-fold>
}
