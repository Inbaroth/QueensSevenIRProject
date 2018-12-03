package InvertedIndex;

public class TermDetails {

    private int rowNumber;
    private int numberOfAppearance;
    private int documentFrequency;

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setNumberOfApperance(int numberOfAppearance) {
        this.numberOfAppearance = numberOfAppearance;
    }

    public void setDocumentFrequency(int documentFrequency) {
        this.documentFrequency = documentFrequency;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getNumberOfAppearance() {
        return numberOfAppearance;
    }

    public int getDocumentFrequency() {
        return documentFrequency;
    }
}

