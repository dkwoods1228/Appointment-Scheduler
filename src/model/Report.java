package model;

public class Report {
    private String country;
    private int totalAmountCountry;

    public Report(String countryName, int totalAmountCountry) {
        this.country = countryName;
        this.totalAmountCountry = totalAmountCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountryName(String countryName) {
        this.country = country;
    }

    public int getTotalAmountCountry() {
        return totalAmountCountry;
    }

    public void setTotalAmountCountry(int totalAmountCountry) {
        this.totalAmountCountry = totalAmountCountry;
    }
}
