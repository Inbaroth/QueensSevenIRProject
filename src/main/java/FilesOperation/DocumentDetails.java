package FilesOperation;

/**
 * This class saves all the details of each document in the corpus
 */
public class DocumentDetails {

    String docId;
    String fileName;
    String cityName;
    int termFrequency;
    int maxTermFrequency;

    /**
     * Constructor
     * @param docId
     * @param fileName
     */
    public DocumentDetails(String docId, String fileName) {
        this.docId = docId;
        this.fileName = fileName;
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

    public int getTermFrequency() {
        return termFrequency;
    }

    public int getMaxTermFrequency() {
        return maxTermFrequency;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public void setMaxTermFrequency(int maxTermFrequency) {
        this.maxTermFrequency = maxTermFrequency;
    }
    //</editor-fold>

}
