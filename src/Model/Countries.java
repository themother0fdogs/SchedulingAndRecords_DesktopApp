package Model;

import DAO.DBDivisions;
import javafx.collections.ObservableList;

/** This is the Countries class that stores the data for all of the countries from the mySQL database. */
public class Countries {

    int countryId;
    String countryName;

    ObservableList<Divisions> allDivisions = DBDivisions.getAllDivisions();

    public Countries(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }
    /**@return countryId*/
    public int getCountryId() {
        return countryId;
    }

    /**@param countryId countryId to set*/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**@return country's name*/
    public String getCountryName() {
        return countryName;
    }

    /**@param countryName country name to set*/
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**@return string of all the country's names */
    public String toString() {
        return (countryName);
    }
}
