package model;

public class Report {
    private String countryName;
    private int totalAmountCountry;

    public Report(String countryName, int totalAmountCountry) {
        this.countryName = countryName;
        this.totalAmountCountry = totalAmountCountry;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getTotalAmountCountry() {
        return totalAmountCountry;
    }

    public void setTotalAmountCountry(int totalAmountCountry) {
        this.totalAmountCountry = totalAmountCountry;
    }
}
