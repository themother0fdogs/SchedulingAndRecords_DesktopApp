package Model;


/** This is the Divisions class that stores all divisions from the mySQL database. */
public class Divisions {

    int divisionId;
    String divisionName;
    int countryId;

    public Divisions(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**@return division Id*/
    public int getDivisionId() {
        return divisionId;
    }

    /**@param divisionId divisionId to set*/
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**@return division name*/
    public String getDivisionName() {
        return divisionName;
    }

    /**@param divisionName division name to set*/
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**@return country Id*/
    public int getCountryId() {
        return countryId;
    }

    /**@param countryId country Id to set*/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**@return list of division names */
    public String toString(){
        return(divisionName);
    }

}
