package model;

public class ReportByType {
    public String appointByType;
    public int appointByTypeTotal;

    public ReportByType(String appointByType, int appointByTypeTotal) {
        this.appointByType = appointByType;
        this.appointByTypeTotal = appointByTypeTotal;
    }

    public String getAppointByType() {
        return appointByType;
    }

    public void setAppointByType(String appointByType) {
        this.appointByType = appointByType;
    }

    public int getAppointByTypeTotal() {
        return appointByTypeTotal;
    }

    public void setAppointByTypeTotal(int appointByTypeTotal) {
        this.appointByTypeTotal = appointByTypeTotal;
    }
}
