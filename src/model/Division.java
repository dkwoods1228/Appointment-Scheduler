package model;
/** Class provided to describe objects within First Level Divisions.*/
public class Division {
    public int divisionID;
    public String division;
    public int countryID;

    /**
     * Lists information within First Level Divisions.
     * @param divisionID
     * @param division
     * @param countryID
     */
    public Division(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }
    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID setter for divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }
    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division setter for division
     */
    public void setDivision(String division) {
        this.division = division;
    }
    /**
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
