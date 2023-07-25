package model;
/** Class provided to describe objects within Reports.*/
public class Report {
    private String country;
    private int totalAmountCountry;

    /**
     * Lists information within Reports.
     * @param country
     * @param totalAmountCountry
     */
    public Report(String country, int totalAmountCountry) {
        this.country = country;
        this.totalAmountCountry = totalAmountCountry;
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
    /**
     * @return totalAmountCountry
     */
    public int getTotalAmountCountry() {
        return totalAmountCountry;
    }

    /**
     * @param totalAmountCountry setter for totalAmountCountry
     */
    public void setTotalAmountCountry(int totalAmountCountry) {
        this.totalAmountCountry = totalAmountCountry;
    }
}
