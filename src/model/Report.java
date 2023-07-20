package model;

public class Report {
    private String country;
    private int totalAmountCountry;

    public Report(String country, int totalAmountCountry) {
        this.country = country;
        this.totalAmountCountry = totalAmountCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotalAmountCountry() {
        return totalAmountCountry;
    }

    public void setTotalAmountCountry(int totalAmountCountry) {
        this.totalAmountCountry = totalAmountCountry;
    }
}
