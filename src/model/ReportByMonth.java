package model;
/** Class provided to obtain data appointment data by month for reports.*/
public class ReportByMonth {
    public String appointByMonth;
    public int appointByMonthTotal;

    /**
     * Lists information within Reports By Month.
     * @param appointByMonth
     * @param appointByMonthTotal
     */
    public ReportByMonth(String appointByMonth, int appointByMonthTotal) {
        this.appointByMonth = appointByMonth;
        this.appointByMonthTotal = appointByMonthTotal;
    }
    /**
     * @return appointByMonth
     */
    public String getAppointByMonth() {
        return appointByMonth;
    }

    /**
     * @param appointByMonth setter for appointByMonth
     */
    public void setAppointByMonth(String appointByMonth) {
        this.appointByMonth = appointByMonth;
    }
    /**
     * @return appointByMonthTotal
     */
    public int getAppointByMonthTotal() {
        return appointByMonthTotal;
    }

    /**
     * @param appointByMonthTotal setter for appointByMonthTotal
     */
    public void setAppointByMonthTotal(int appointByMonthTotal) {
        this.appointByMonthTotal = appointByMonthTotal;
    }
}
