package model;
/** Class provided to obtain data appointment data by type for reports.*/
public class ReportByType {
    public String appointByType;
    public int appointByTypeTotal;

    /**
     * Lists information within Reports By Type.
     * @param appointByType
     * @param appointByTypeTotal
     */
    public ReportByType(String appointByType, int appointByTypeTotal) {
        this.appointByType = appointByType;
        this.appointByTypeTotal = appointByTypeTotal;
    }
    /**
     * @return appointByType
     */
    public String getAppointByType() {
        return appointByType;
    }

    /**
     * @param appointByType setter for appointByType
     */
    public void setAppointByType(String appointByType) {
        this.appointByType = appointByType;
    }

    /**
     * @return appointByTypeTotal
     */
    public int getAppointByTypeTotal() {
        return appointByTypeTotal;
    }

    /**
     * @param appointByTypeTotal setter for appointByTypeTotal
     */
    public void setAppointByTypeTotal(int appointByTypeTotal) {
        this.appointByTypeTotal = appointByTypeTotal;
    }
}
