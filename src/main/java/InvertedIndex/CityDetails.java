package InvertedIndex;

import java.util.ArrayList;
import java.util.HashMap;

public class CityDetails {

    private String cityName;
    private String docId;
    private String countryName;
    private String currency;
    private String populationSize;

    private ArrayList<Integer> listOfPositions;

    public CityDetails(String cityName, String docId) {
        this.cityName = cityName;
        this.docId = docId;

        this.listOfPositions = new ArrayList<>();
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPopulationSize(String populationSize) {
        this.populationSize = populationSize;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setPosition(int position){
        this.listOfPositions.add(position);
    }

    public void setListOfPositions(ArrayList<Integer> listOfPositions) {
        this.listOfPositions = listOfPositions;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDocId() {
        return docId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPopulationSize() {
        return populationSize;
    }

    public ArrayList<Integer> getListOfPositions() {
        return listOfPositions;
    }
}
