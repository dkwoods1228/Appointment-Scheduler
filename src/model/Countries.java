package model;
/** Class provided to describe objects within Countries.*/
public class Countries {

    private String country;
    private int countryID;

    /**
     * List information within Countries.
     * @param countryID
     * @param country
     */
    public Countries(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    /**
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @param countryID setter for countryID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country setter for country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
